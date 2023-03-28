<template>
  <div>
    <br><br><br>
    <t-badge :count="data.messageTips">
      <t-tag class="communicationBtn" style="width: 7.7em;height: 3em;cursor: pointer" @click="onOpenChatDialog" shape="mark" theme="primary">
      <span style="font-size: 16px;margin-left: -0.2em">
        {{ '< 联系考官' }}
      </span>
      </t-tag>
    </t-badge>
    <br><br>
    <t-tag class="openBtn" style="width: 7.7em;height: 3em;cursor: pointer" @click="onOpen" shape="mark"
           theme="primary">
      <span style="font-size: 16px;margin-left: -0.2em">
        {{ '< 答题卡' }}
      </span>
    </t-tag>
    <br><br>
    <t-tag class="submitBtn" style="width: 7.7em;height: 3em;cursor: pointer" @click="end" shape="mark" theme="success">
      <span style="font-size: 16px;margin-left: -0.2em">
        {{ '< 交卷' }}
      </span>
    </t-tag>

    <t-drawer
        :closeOnEscKeydown="false"
        closeOnOverlayClick
        :footer="true"
        :closeBtn="true"
        preventScrollThrough
        :confirmBtn="`切换`"
        :cancelBtn="null"
        placement="left"
        v-model:visible="visible"
        @onClose="onClose"
        header="答题卡">
      <p style="display: none">{{ contestDetail.contestSubject }}</p>
      <p style="display: none" :key="index" v-for="(questionIndexList,key,index) in list">{{ questionIndexList[0] }}</p>
      <div :key="index" v-for="(questionIndexList,key,index) in list">
        <h3 style="color: black" v-if="questionIndexList.length !== 0">{{ questionTypeMap[key] }}</h3>
        <t-tag @click="onQuestionSelected({currentPaperAns: questionIndex,currentIndex: index})"
               :key="index" class="tg" v-for="(questionIndex,index) in questionIndexList"
               :class="{complete: questionIndex.answerContent !== null}">
          {{ index + 1 }}
        </t-tag>
      </div>
    </t-drawer>
  </div>
  <ChatDialog :visitable="data.chatDialog.visible"
              ref="chat"
              @on-close="onChatDialogClose"
              :self-id="data.selfId"
              :key="data.chatKey"
              header="联系考官"
              :user-id="data.userId"
              :self-name="data.selfName"
              :user-name="data.userName"
              @send-message="sendMessage"
              :chat-records="data.chatRecords"
              :websocket="websocket"/>
</template>

<script setup>
import {onMounted, reactive, ref, toRef} from "vue";
import {defineProps} from "vue";
import {defineEmits} from "vue";
import {defineExpose} from "vue";
import {MessagePlugin} from "tdesign-vue-next";
import ChatDialog from "@/page/contest/component/ChatDialog";
import {getContestDetailById} from "@/api/contest";
import router from "@/router/router";
import {getUserDetail, getUserDetailById} from "@/api/user";
import {getTimeStr} from "@/util/date.util";
import {getChatRecordsListByUserId} from "@/api/online";

const visible = ref(false)
const emits = defineEmits(['on-question-selected', 'to-complete'])

const data = reactive({
  chatDialog: {
    visible: false
  },
  selfId: null,
  userId: null,
  userName: null,
  selfName: null,
  chatKey: 1,
  messageTips: '',
  chatRecords: [
    {
      sourceUserId: 'noyoga',
      sourceUserName: '考官',
      targetUserId: '',
      targetUserName: '',
      msg: '考生您好，请对着摄像头举起身份证，需要后台审查',
      createdDate: getTimeStr(new Date())
    }
  ]
})

let websocket = reactive(null)

const questionTypeMap = {
  ANSWER_QUESTION: '问答题',
  SINGLE_OPTION_QUESTION: '选择题',
  SUPPLEMENT_QUESTION: '填空题',
  JUDGE_QUESTION: '判断题',
  PROGRAMMING_QUESTION: '编程题'
}

const props = defineProps({
  questionList: {
    required: true
  },
  contestDetail: {
    required: true
  }
})
const contestDetail = toRef(props, 'contestDetail')
const list = toRef(props, 'questionList')

const onOpen = () => {
  visible.value = true
}

const onClose = () => {
  visible.value = false
}

const end = () => {
  emits('to-complete')
}

const onQuestionSelected = (q) => {
  emits('on-question-selected', q)
  onClose()
}

const toNextQuestion = (index, questionType) => {
  let ans = null
  let newIndex = index + 1
  if (newIndex >= props.questionList[questionType].length) {
    let keys = Object.keys(props.questionList)
    let newType = null
    let i = 0
    for (; i < keys.length; i++) {
      if (keys[i] === questionType) {
        break
      }
    }
    if (i + 1 !== keys.length && keys[i + 1] !== "PROGRAMMING_QUESTION") {
      newType = keys[i + 1]
      newIndex = 0
      ans = props.questionList[newType][0]
    } else {
      MessagePlugin.info('该题目已经是最后一题！')
      return
    }
  } else {
    ans = props.questionList[questionType][index + 1]
  }
  onQuestionSelected({currentPaperAns: ans, currentIndex: newIndex})
}

const sendMessage = (chatDto) => {
  websocket.send(JSON.stringify({
    request: '/chat',
    data: chatDto
  }))
  data.chatRecords.push(chatDto)
}

const onChatDialogClose = () => {
  data.chatDialog.visible = false
}

const onOpenChatDialog = () => {
  data.chatKey = (data.chatKey + 1)%50
  data.chatDialog.visible = true
  data.messageTips = ''
}

defineExpose({toNextQuestion})

onMounted(async () => {
  await getUserDetail().then(resp => {
    data.selfId = resp.data.data.userId
    data.selfName = resp.data.data.userName
  })
  await getContestDetailById(router.currentRoute.value.params.contestId).then(resp => {
    data.userId = resp.data.data.createdBy
  })
  await getUserDetailById(data.userId).then(resp => {
    data.userName = resp.data.data.userName
  })
  websocket = new WebSocket('ws://127.0.0.1:8596/ws/'.concat(data.selfId))
  websocket.onopen = () => {
    console.log('成功连接到WebSocket服务器')
    getChatRecordsListByUserId(data.userId).then(resp => {
      let list = resp.data.data
      data.chatRecords.length = 0
      for (let i = 0; i < list.length; i++) {
        data.chatRecords.push(list[i])
      }
    })
  }
  websocket.onmessage = (m) => {
    if(data.chatDialog.visible === false){
      data.messageTips = '!'
      MessagePlugin.info('您有新消息，请点击联系考官查看！')
    }
    data.chatRecords.push(JSON.parse(m.data).data)
  }
  websocket.onerror = () => {
    console.log('WebSocket连接异常')
  }
  websocket.onclose = () => {
    console.log('与WebSocket服务器断开连接')
  }
})

</script>
<style scoped>
.openBtn:hover {
  background-color: dodgerblue;
  color: #f9f9f9;
}

.communicationBtn:hover{
  background-color: dodgerblue;
  color: #f9f9f9;
}

.tg {
  margin: 0.5em;
  cursor: pointer;
  font-weight: bold;
}

.tg:hover {
  background-color: dodgerblue;
  color: white;
}

.submitBtn:hover {
  background-color: limegreen;
}
.communicationBtn{

}
.complete {
  background-color: limegreen;
  color: white;
}
</style>