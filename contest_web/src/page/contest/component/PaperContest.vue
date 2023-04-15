<template>
  <div class="board">
    <img src="../../../assets/answer-board.png" style="width: 100%" alt="">
    <t-tag theme="default"  size="large" style="margin-left: 2em;color: #0075FF;background-color: #E3F3FF;">
      {{questionTypeMap[currentQuestion.currentPaperAns.questionDetail.questionType]}}
    </t-tag>
    <div style="width: 70em;margin-left: 2em;margin-top: 1em;font-size: 18px">
      <div style="display: inline-block" v-html="currentQuestion.currentPaperAns.questionDetail.questionContent"/>
    </div>
    <t-list v-if="currentQuestion.currentPaperAns.questionDetail.questionType === 'SINGLE_OPTION_QUESTION'" style="margin-top: 2em">
      <t-list-item :class="{'option-item-selected': singleOptionQuestion.ans === questionOption.option}"
                   class="option-item"
                   style="margin-left: 2em;"
                   :key="index"
                   @click="onSingleOptionSelected(questionOption.option)"
                   v-for="(questionOption,index) in currentQuestion.currentPaperAns.questionDetail.questionOption">
        <template #action>
          <t-tag :theme="singleOptionQuestion.ans === questionOption.option?'primary':'default'" size="large" style="border-radius: 50%">{{questionOption.option}}</t-tag>
          <div v-html="questionOption.content" style="margin-left: 0.8em;font-size: 16px;display: inline-block"/>
        </template>
      </t-list-item>
    </t-list>
    <t-list v-if="currentQuestion.currentPaperAns.questionDetail.questionType === 'JUDGE_QUESTION'">
      <t-list-item :class="{'option-item-selected': judgeQuestion.ans === true}"
                   class="option-item"
                   @click="onJudgeOptionSelected(true)"
                   style="margin-left: 2em;">
        <template #action>
          <t-tag :theme="judgeQuestion.ans === true?'primary':'default'" size="large" style="border-radius: 50%">√</t-tag>
          <div style="margin-left: 0.8em;font-size: 16px;display: inline-block">正确</div>
        </template>
      </t-list-item>
      <t-list-item :class="{'option-item-selected': judgeQuestion.ans === false}"
                   class="option-item"
                   @click="onJudgeOptionSelected(false)"
                   style="margin-left: 2em">
        <template #action>
          <t-tag :theme="judgeQuestion.ans === false?'primary':'default'" size="large" style="border-radius: 50%">×</t-tag>
          <div style="margin-left: 0.8em;font-size: 16px;display: inline-block">错误</div>
        </template>
      </t-list-item>
    </t-list>
    <t-list v-if="currentQuestion.currentPaperAns.questionDetail.questionType === 'SUPPLEMENT_QUESTION'">
      <t-list-item style="width: 60em;margin-left: 1.5em" :key="index" v-for="(ans,index) in supplementQuestion.ans">
        <t-input @change="onSupply" v-model="supplementQuestion.ans[index]" :placeholder="`第${index+1}空`"/>
      </t-list-item>
    </t-list>
    <MyEditor v-if="currentQuestion.currentPaperAns.questionDetail.questionType === 'ANSWER_QUESTION'"
              :content="answerQuestion.ans"
              :key="answerQuestion.key"
              @content-change="onAnswerQuestionChange"
              class="editor"/>
    <t-button @click="toNextQuestion" size="large" style="width: 30em;margin-left: 23em;margin-top: 6em">下一题</t-button>
  </div>
</template>

<script>

import {QUESTION_TYPE} from "@/common/question";
import MyEditor from "@/page/component/editor/MyEditor";
import {StringUtil} from "@/util/string.util";

export default {
  name: "PaperContest",
  components: {MyEditor},
  props: {
    currentQuestion: {
      required: true
    }
  },
  emits: ['on-option-selected','on-judge-selected','on-supply-change','on-answer-change'],
  data() {
    return {
      questionTypeMap: {
        ANSWER_QUESTION: '问答题',
        SINGLE_OPTION_QUESTION: '选择题',
        SUPPLEMENT_QUESTION: '填空题',
        JUDGE_QUESTION: '判断题',
        PROGRAMMING_QUESTION: '编程题'
      },
      singleOptionQuestion: {
        ans: null
      },
      judgeQuestion: {
        ans: null
      },
      supplementQuestion: {
        ans: []
      },
      answerQuestion:{
        ans: null,
        key: 1
      }
    }
  },
  methods: {
    onSingleOptionSelected(option){
      if(this.singleOptionQuestion.ans === option){
        this.singleOptionQuestion.ans = null
        this.$emit('on-option-selected',null)
        return
      }
      this.singleOptionQuestion.ans = option
      this.$emit('on-option-selected',option)
    },
    onJudgeOptionSelected(ans){
      if(this.judgeQuestion.ans === ans){
        this.judgeQuestion.ans = null
        this.$emit('on-judge-selected',null)
        return
      }
      this.judgeQuestion.ans = ans
      this.$emit('on-judge-selected',ans)
    },
    onSupply(){
      this.$emit('on-supply-change',this.supplementQuestion.ans)
    },
    onAnswerQuestionChange(content){
      if(StringUtil.parseFromHtml(content) === ''){
        content = ''
      }
      this.$emit('on-answer-change',content)
    },
    toNextQuestion(){
      this.$emit('to-next-question')
    }
  },
  mounted() {
    if(QUESTION_TYPE.SUPPLEMENT_QUESTION === this.currentQuestion.currentPaperAns.questionDetail.questionType){
      if(this.currentQuestion.currentPaperAns.answerContent === null){
        let count = parseInt(this.currentQuestion.currentPaperAns.questionDetail.questionReferenceAnswer)
        for(let i=0;i<count;i++){
          this.supplementQuestion.ans.push(null)
        }
      }else{
        let list = JSON.parse(this.currentQuestion.currentPaperAns.answerContent)
        for(let i=0;i<list.length;i++){
          this.supplementQuestion.ans.push(list[i])
        }
      }
    }
    if(QUESTION_TYPE.SINGLE_OPTION_QUESTION === this.currentQuestion.currentPaperAns.questionDetail.questionType){
      this.singleOptionQuestion.ans = this.currentQuestion.currentPaperAns.answerContent
    }
    if(QUESTION_TYPE.JUDGE_QUESTION === this.currentQuestion.currentPaperAns.questionDetail.questionType){
      if(this.currentQuestion.currentPaperAns.answerContent === "true"){
        this.judgeQuestion.ans = true
      }else if(this.currentQuestion.currentPaperAns.answerContent === "false"){
        this.judgeQuestion.ans = false
      }else{
        this.judgeQuestion.ans = null
      }
    }
    if(QUESTION_TYPE.ANSWER_QUESTION === this.currentQuestion.currentPaperAns.questionDetail.questionType){
      let content = StringUtil.parseFromHtml(this.currentQuestion.currentPaperAns.answerContent);
      this.answerQuestion.ans = StringUtil.isEmpty(content)?null:this.currentQuestion.currentPaperAns.answerContent
      this.answerQuestion.key = (this.answerQuestion.key+1)%50
    }
  }
}
</script>

<style scoped>
  .board{
    background-color: white;
    width: 76em;
    height: 60em;
    margin-bottom: 5em;
    margin-left: 11.5em;
    margin-top: -9em;
    box-shadow: 0 0 10px 0 rgb(0 0 0 / 10%);
  }
  .option-item{
    border-radius: 5px;
    width: 80.5em;
  }
  .option-item:hover{
    background-color: #E3F3FF;
    cursor: pointer;
  }
  .option-item-selected{
    background-color: #E3F3FF;
  }
  .editor{
    width: 70em;
    margin-left: 2em;
  }
</style>