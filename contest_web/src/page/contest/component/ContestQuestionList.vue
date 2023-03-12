<template>
  <div>
    <t-tag class="openBtn" style="margin-top: 5em;width: 7em;height: 3em;cursor: pointer" @click="onOpen" shape="mark" theme="primary">
      <span style="font-size: 16px;margin-left: -0.2em">
        {{'< 答题卡'}}
      </span>
    </t-tag>
    <br><br>
    <t-tag class="submitBtn" style="width: 7em;height: 3em;cursor: pointer" @click="end" shape="mark" theme="success">
      <span style="font-size: 16px;margin-left: -0.2em">
        {{'< 交卷'}}
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
      <p style="display: none">{{contestDetail.contestSubject}}</p>
      <p style="display: none" :key="index" v-for="(questionIndexList,key,index) in list">{{questionIndexList[0]}}</p>
      <div :key="index" v-for="(questionIndexList,key,index) in list">
        <h3 style="color: black" v-if="questionIndexList.length !== 0">{{questionTypeMap[key]}}</h3>
        <t-tag @click="onQuestionSelected({currentPaperAns: questionIndex,currentIndex: index})"
               :key="index" class="tg" v-for="(questionIndex,index) in questionIndexList"
               :class="{complete: questionIndex.answerContent !== null}">
          {{index+1}}
        </t-tag>
      </div>
    </t-drawer>
  </div>
</template>

<script setup>
import {onMounted, ref, toRef} from "vue";
import {defineProps} from "vue";
import {defineEmits} from "vue";
import {defineExpose} from "vue";
import {MessagePlugin} from "tdesign-vue-next";

const visible = ref(false)
const emits = defineEmits(['on-question-selected','to-complete'])

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
const contestDetail = toRef(props,'contestDetail')
const list = toRef(props,'questionList')

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
  emits('on-question-selected',q)
  onClose()
}

const toNextQuestion = (index,questionType) => {
  let ans = null
  let newIndex = index+1
  if(newIndex >= props.questionList[questionType].length){
    let keys = Object.keys(props.questionList)
    let newType = null
    let i = 0
    for(;i<keys.length;i++){
      if(keys[i] === questionType){
        break
      }
    }
    if(i+1!==keys.length && keys[i+1] !== "PROGRAMMING_QUESTION"){
      newType = keys[i+1]
      newIndex = 0
      ans = props.questionList[newType][0]
    }else{
      MessagePlugin.info('该题目已经是最后一题！')
      return
    }
  }else{
    ans = props.questionList[questionType][index+1]
  }
  onQuestionSelected({currentPaperAns: ans,currentIndex: newIndex})
}

defineExpose({toNextQuestion})

onMounted(async () => {
})

</script>
<style scoped>
  .openBtn:hover{
      background-color: dodgerblue;
      color: #f9f9f9;
  }
  .tg{
    margin: 0.5em;
    cursor: pointer;
    font-weight: bold;
  }
  .tg:hover{
    background-color: dodgerblue;
    color: white;
  }
  .submitBtn:hover{
    background-color: green;
  }
  .complete{
    background-color: limegreen;
    color: white;
  }
</style>