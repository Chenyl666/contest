<template>
  <div>
    <!-- @cancel 和 :onCancel 两种写法都支持； :onOverlayClick 和 @overlay-click 两种写法都支持-->
    <t-drawer
        v-model:visible="visible"
        :on-overlay-click="onClose"
        :placement="`left`"
        @cancel="onClose"
        size="medium"
        preventScrollThrough
    >
      <template #header>
        <h3>{{props.header}}</h3>
      </template>
      <template #footer>
        <t-input v-model="data.prepareMsg" size="large"/>
        <br>
        <t-button style="float: right;margin-left: 1em" @click="sendMessage">发送</t-button>
        <t-button style="float: right" variant="outline" @click="onClose"> 关闭 </t-button>
<!--        <t-button style="float: right" variant="outline" @click="test"> 发送 </t-button>-->
      </template>
      <t-list>
        <t-list-item :key="index" v-for="(chatRecord,index) in data.chatRecords">
            <div style="display: inline-block">
              <span style="margin-right: 2em;font-weight: bold;color: black">{{chatRecord.sourceUserName}}:</span>
              <div>{{chatRecord.msg}}</div>
            </div>
        </t-list-item>
      </t-list>
    </t-drawer>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, reactive, toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";
import {getTimeStr} from "@/util/date.util";
// import {request} from "@/util/request";
// import {getUserDetail, getUserDetailById} from "@/api/user";
// import {getChatRecordsListByUserId} from "@/api/online";

const props = defineProps({
  visitable: {
    required: true,
    type: Boolean
  },
  userId: {
    required: true,
    type: String
  },
  selfId: {
    required: true,
    type: String
  },
  userName: {
    required: true,
    type: String
  },
  selfName: {
    required: true,
    type: String
  },
  websocket: {
    required: true
  },
  chatRecords: {
    required: true,
    type: Array
  },
  header: {
    type: String,
    default: '聊天框'
  }
})


const data = reactive({
  prepareMsg: '',
  chatRecords: toRef(props,'chatRecords')
  // chatRecords: [

  // ]
})

const emits = defineEmits(['on-close','send-message'])

const visible = toRef(props,'visitable');

const onClose = () => {
  emits('on-close')
}

const sendMessage = () => {
  if(data.prepareMsg === '' || data.prepareMsg === null){
    return
  }
  let chatDto = {
    sourceUserId: props.selfId,
    sourceUserName: props.selfName,
    targetUserId: props.userId,
    targetUserName: props.userName,
    msg: data.prepareMsg,
    createdDate: getTimeStr(new Date())
  }
  emits('send-message',chatDto)
  data.prepareMsg = ''
}

// const test = () => {
//   request.post('/contest/chat/try',true)
// }

onMounted(async () => {

})

onUnmounted(() => {})

</script>
