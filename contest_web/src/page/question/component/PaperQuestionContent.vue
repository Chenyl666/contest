<template>
  <div style="width: 100%;height: 5em;">
    <t-button @click="toSingleOptionQuestion" :class="{addBtnHighLight: isSingleOptionQuestion&&!questionRepo.load}" style="margin-left: 3.7em" class="addBtn" variant="outline">选择题</t-button>
    <t-button @click="toSupplementQuestion" :class="{addBtnHighLight: isSupplementOptionQuestion&&!questionRepo.load}" class="addBtn" variant="outline">填空题</t-button>
    <t-button @click="toJudgeQuestion" :class="{addBtnHighLight: isJudgeQuestion&&!questionRepo.load}" class="addBtn" variant="outline">判断题</t-button>
    <t-button @click="toAnswerQuestion" :class="{addBtnHighLight: isAnswerQuestion&&!questionRepo.load}" class="addBtn" variant="outline">问答题</t-button>
    <t-input v-model="questionRepo.score" style="width: 20%;margin-right: 11em;margin-top: 2.7em;float: right" label="分值：" placeholder=""/>
  </div>
  <div :key="questionRepo.key.singleOptionQuestionKey" v-if="isSingleOptionQuestion" style="width: 100%;margin-bottom: 8em">
      <h3 style="margin-left: 3em;margin-top: 2em">题干：</h3>
      <MyEditor width="80%"
                height="20em"
                :content="singleOptionQuestionForm.questionContent"
                @content-change="v => singleOptionQuestionForm.questionContent = v"
                :save-to-store="`setQuestionContentText`"
                style="margin-left: 3.5em;"/>
      <h3 style="margin-left: 3em;margin-top: 5em">选项（目前仅支持4个选项）:</h3>
      <div :key="questionOption.option" v-for="(questionOption) in singleOptionQuestionForm.questionOption" style="padding-left: 4em;margin-bottom: 10em">
        <h4 style="margin-top: 2em">{{questionOption.option}}</h4>
        <MyEditor width="78.5%"
                  height="7em"
                  :content="questionOption.content"
                  @content-change="v => questionOption.content = v"
                  style="margin-left: 3.5em;margin-top: -2.7em"/>
      </div>
      <h3 style="margin-left: 3em">正确答案：</h3>
      <t-radio-group style="margin-left: 4em"
                     v-model="singleOptionQuestionForm.questionReferenceAns">
        <t-radio value="A">A</t-radio>
        <t-radio value="B">B</t-radio>
        <t-radio value="C">C</t-radio>
        <t-radio value="D">D</t-radio>
      </t-radio-group>
    <h3 style="margin-left: 3em;margin-top: 2em">答案解析：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="singleOptionQuestionForm.questionExplain"
              @content-change="v => singleOptionQuestionForm.questionExplain = v"
              style="margin-left: 3.5em;"/>
  </div>

  <div :key="questionRepo.key.supplementQuestionKey" v-if="isSupplementOptionQuestion" style="width: 100%;margin-bottom: 8em">
    <h3 style="margin-left: 3em;margin-top: 2em">题干：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="supplementQuestionForm.questionContent"
              @content-change="v => supplementQuestionForm.questionContent = v"
              :save-to-store="`setQuestionContentText`"
              style="margin-left: 3.5em;"/>
    <h3 style="margin-left: 3em;margin-top: 5em">正确答案：</h3>
    <t-input
        v-model="supplementQuestionForm.questionReferenceAns[index]"
        :key="index"
        v-for="(ans,index) in supplementQuestionForm.questionReferenceAns"
        style="width: 80%;margin-left: 3.5em;margin-bottom: 1em"
        :label="`第${index+1}空：`"
    />
    <t-link @click="supplementQuestionForm.questionReferenceAns.push('')" style="margin-left: 62em" theme="primary">添加</t-link>
    <t-link @click="supplementQuestionForm.questionReferenceAns.pop()" style="margin-left: 2em" theme="primary">删除</t-link>
    <h3 style="margin-left: 3em;margin-top: 1em">答案解析：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="supplementQuestionForm.questionExplain"
              @content-change="v => supplementQuestionForm.questionExplain = v"
              style="margin-left: 3.5em;"/>
  </div>

  <div :key="questionRepo.key.judgeQuestionKey" v-if="isJudgeQuestion" style="width: 100%;margin-bottom: 8em">
    <h3 style="margin-left: 3em;margin-top: 2em">题干：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="judgeQuestionForm.questionContent"
              @content-change="v => judgeQuestionForm.questionContent = v"
              :save-to-store="`setQuestionContentText`"
              style="margin-left: 3.5em;"/>
    <h3 style="margin-left: 3em;margin-top: 5em">正确答案：</h3>
    <t-radio-group style="margin-left: 4em" v-model="judgeQuestionForm.questionReferenceAns">
      <t-radio :value="true">正确</t-radio>
      <t-radio :value="false">错误</t-radio>
    </t-radio-group>
    <h3 style="margin-left: 3em;margin-top: 2em">答案解析：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="judgeQuestionForm.questionExplain"
              @content-change="v => judgeQuestionForm.questionExplain = v"
              style="margin-left: 3.5em;"/>
  </div>

  <div :key="questionRepo.key.answerQuestionKey" v-if="isAnswerQuestion" style="width: 100%;margin-bottom: 8em">
    <h3 style="margin-left: 3em;margin-top: 2em">题干：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="answerQuestionForm.questionContent"
              @content-change="v => answerQuestionForm.questionContent = v"
              :save-to-store="`setQuestionContentText`"
              style="margin-left: 3.5em;"/>
    <h3 style="margin-left: 3em;margin-top: 5em">正确答案：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="answerQuestionForm.questionReferenceAns"
              @content-change="v => answerQuestionForm.questionReferenceAns = v"
              style="margin-left: 3.5em;"/>
    <h3 style="margin-left: 3em;margin-top: 5em">答案解析：</h3>
    <MyEditor width="80%"
              height="20em"
              :content="answerQuestionForm.questionExplain"
              @content-change="v => answerQuestionForm.questionExplain = v"
              style="margin-left: 3.5em;"/>
  </div>


  <t-form-item v-if="questionRepo.questionSelected !== null">
    <t-button @click="save" size="large" style="margin-left: 3.5em;width: 80%">保存</t-button>
  </t-form-item>

</template>
<script setup>
import {computed, onMounted, reactive} from 'vue';
import MyEditor from "@/page/component/editor/MyEditor";
import router from "@/router/router";
import {StringUtil} from "@/util/string.util";
import {QUESTION_TYPE} from "@/common/question";
import {addQuestion, getQuestionById} from "@/api/question";
import {result} from "@/common/request.result";
import {store} from "@/store";
import {defineEmits} from "vue";
import {defineExpose} from "vue";
import {MessagePlugin} from "tdesign-vue-next";

const emits = defineEmits(['load-question','clear-index-highlight'])

const loadQuestion = (questionId) => {
  getQuestionById(questionId).then(resp => {
    if(resp.data['resultCode'] === result.code.SUCCESS){
      questionRepo.load = false
      questionRepo.questionId = null
      let questionDetailDto = resp.data.data
      questionRepo.load = true
      questionRepo.questionSelected = questionDetailDto.questionType
      questionRepo.questionId = questionDetailDto.questionId
      questionRepo.score = questionDetailDto.score
      switch (questionDetailDto.questionType) {
        case QUESTION_TYPE.SINGLE_OPTION_QUESTION: {
          questionRepo.key.singleOptionQuestionKey++
          singleOptionQuestionForm.questionOption = questionDetailDto.questionOption
          singleOptionQuestionForm.questionExplain = questionDetailDto.questionExplain
          singleOptionQuestionForm.questionContent = questionDetailDto.questionContent
          singleOptionQuestionForm.questionReferenceAns = questionDetailDto.questionReferenceAnswer
          break
        }
        case QUESTION_TYPE.SUPPLEMENT_QUESTION: {
          questionRepo.key.supplementQuestionKey++
          supplementQuestionForm.questionContent = questionDetailDto.questionContent
          supplementQuestionForm.questionExplain = questionDetailDto.questionExplain
          supplementQuestionForm.questionReferenceAns = JSON.parse(questionDetailDto.questionReferenceAnswer)
          break
        }
        case QUESTION_TYPE.JUDGE_QUESTION: {
          questionRepo.key.judgeQuestionKey++
          judgeQuestionForm.questionExplain = questionDetailDto.questionExplain
          judgeQuestionForm.questionContent = questionDetailDto.questionContent
          judgeQuestionForm.questionReferenceAns = questionDetailDto.questionReferenceAnswer === 'true'
          break
        }
        case QUESTION_TYPE.ANSWER_QUESTION: {
          questionRepo.key.answerQuestionKey++
          answerQuestionForm.questionContent = questionDetailDto.questionContent
          answerQuestionForm.questionExplain = questionDetailDto.questionExplain
          answerQuestionForm.questionReferenceAns = questionDetailDto.questionReferenceAnswer
          break
        }
      }
    }
  })
}

defineExpose({loadQuestion})

const questionRepo = reactive({
  questionRepoId: null,
  questionSelected: null,
  questionId: null,
  score: 0,
  load: false,
  key: {
    singleOptionQuestionKey: 1,
    judgeQuestionKey: 1,
    supplementQuestionKey: 1,
    answerQuestionKey: 1
  }
})
const singleOptionQuestionForm = reactive({
  questionContent: null,
  questionOption: [
    {option: 'A',content: null},
    {option: 'B',content: null},
    {option: 'C',content: null},
    {option: 'D',content: null},
  ],
  questionReferenceAns: null,
  questionExplain: null
});

const supplementQuestionForm = reactive({
  questionContent: null,
  questionReferenceAns: ['',''],
  questionExplain: null
})

const judgeQuestionForm = reactive({
  questionContent: null,
  questionReferenceAns: null,
  questionExplain: null
})

const answerQuestionForm = reactive({
  questionContent: null,
  questionReferenceAns: null,
  questionExplain: null
})

const isSingleOptionQuestion = computed(() => {
  return questionRepo.questionSelected === QUESTION_TYPE.SINGLE_OPTION_QUESTION
})

const isSupplementOptionQuestion = computed(() => {
  return questionRepo.questionSelected === QUESTION_TYPE.SUPPLEMENT_QUESTION
})

const isJudgeQuestion = computed(() => {
  return questionRepo.questionSelected === QUESTION_TYPE.JUDGE_QUESTION
})

const isAnswerQuestion = computed(() => {
  return questionRepo.questionSelected === QUESTION_TYPE.ANSWER_QUESTION
})

const toSingleOptionQuestion = () => {
  questionRepo.load = false
  questionRepo.questionId = null
  questionRepo.questionSelected = QUESTION_TYPE.SINGLE_OPTION_QUESTION
  emits('clear-index-highlight')
  singleOptionQuestionForm.questionOption[0].content = null
  singleOptionQuestionForm.questionOption[1].content = null
  singleOptionQuestionForm.questionOption[2].content = null
  singleOptionQuestionForm.questionOption[3].content = null
  singleOptionQuestionForm.questionContent = null
  singleOptionQuestionForm.questionExplain = null
  singleOptionQuestionForm.questionReferenceAns = null
  questionRepo.score = 0
  questionRepo.key.singleOptionQuestionKey++
}

const toSupplementQuestion = () => {
  questionRepo.load = false
  questionRepo.questionId = null
  questionRepo.questionSelected = QUESTION_TYPE.SUPPLEMENT_QUESTION
  emits('clear-index-highlight')
  supplementQuestionForm.questionReferenceAns = ['']
  supplementQuestionForm.questionExplain = null
  supplementQuestionForm.questionContent = null
  questionRepo.score = 0
  questionRepo.key.supplementQuestionKey++
}

const toJudgeQuestion = () => {
  questionRepo.load = false
  questionRepo.questionId = null
  questionRepo.questionSelected = QUESTION_TYPE.JUDGE_QUESTION
  emits('clear-index-highlight')
  judgeQuestionForm.questionReferenceAns = null
  judgeQuestionForm.questionContent = null
  judgeQuestionForm.questionExplain = null
  questionRepo.score = 0
  questionRepo.key.judgeQuestionKey++
}

const toAnswerQuestion = () => {
  questionRepo.load = false
  questionRepo.questionId = null
  questionRepo.questionSelected = QUESTION_TYPE.ANSWER_QUESTION
  emits('clear-index-highlight')
  answerQuestionForm.questionReferenceAns = null
  answerQuestionForm.questionExplain = null
  answerQuestionForm.questionContent = null
  questionRepo.score = 0
  questionRepo.key.answerQuestionKey++
}

const save = () => {
  let formData = {
    questionContent: null,
    questionReferenceAnswer: null,
    questionExplain: null,
    questionId: questionRepo.questionId,
    questionRepoId: questionRepo.questionRepoId,
    questionType: questionRepo.questionSelected,
    score: questionRepo.score
  }
  switch (questionRepo.questionSelected) {
    case QUESTION_TYPE.SINGLE_OPTION_QUESTION:{
      formData.questionContent = singleOptionQuestionForm.questionContent
      formData.questionReferenceAnswer = singleOptionQuestionForm.questionReferenceAns
      formData.questionExplain = singleOptionQuestionForm.questionExplain
      formData.questionOption = singleOptionQuestionForm.questionOption
      addQuestion(formData).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          emits('load-question',{
            questionId: questionRepo.questionRepoId,
            score: questionRepo.score,
            questionContent: store.state.questionContentText,
            questionType: QUESTION_TYPE.SINGLE_OPTION_QUESTION
          })
          MessagePlugin.success("保存成功！")
        }else{
          MessagePlugin.error("系统繁忙！")
        }
      })
      break
    }
    case QUESTION_TYPE.SUPPLEMENT_QUESTION: {
      formData.questionContent = supplementQuestionForm.questionContent
      formData.questionReferenceAnswer = JSON.stringify(supplementQuestionForm.questionReferenceAns)
      formData.questionExplain = supplementQuestionForm.questionExplain
      addQuestion(formData).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          emits('load-question',{
            questionId: questionRepo.questionRepoId,
            score: questionRepo.score,
            questionContent: store.state.questionContentText,
            questionType: QUESTION_TYPE.SUPPLEMENT_QUESTION
          })
          MessagePlugin.success("保存成功！")
        }else{
          MessagePlugin.error("系统繁忙！")
        }
      })
      break
    }
    case QUESTION_TYPE.JUDGE_QUESTION: {
      formData.questionContent = judgeQuestionForm.questionContent
      formData.questionReferenceAnswer = judgeQuestionForm.questionReferenceAns
      formData.questionExplain = judgeQuestionForm.questionExplain
      addQuestion(formData).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          emits('load-question',{
            questionId: questionRepo.questionRepoId,
            score: questionRepo.score,
            questionContent: store.state.questionContentText,
            questionType: QUESTION_TYPE.JUDGE_QUESTION
          })
          MessagePlugin.success("保存成功！")
        }else{
          MessagePlugin.error("系统繁忙！")
        }
      })
      break
    }
    case QUESTION_TYPE.ANSWER_QUESTION: {
      formData.questionContent = answerQuestionForm.questionContent
      formData.questionReferenceAnswer = answerQuestionForm.questionReferenceAns
      formData.questionExplain = answerQuestionForm.questionExplain
      addQuestion(formData).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          emits('load-question',{
            questionId: questionRepo.questionRepoId,
            score: questionRepo.score,
            questionContent: store.state.questionContentText,
            questionType: QUESTION_TYPE.ANSWER_QUESTION
          })
          MessagePlugin.success('保存成功！')
        }else{
          MessagePlugin.error("系统繁忙！")
        }
      })
      break
    }
  }
}

onMounted(() => {
  if(StringUtil.isEmpty(router.currentRoute.value.params['questionRepoId'])){
    alert('error')
  }
  questionRepo.questionRepoId = router.currentRoute.value.params['questionRepoId']
})

</script>

<style scoped>
  .addBtn{
    margin-top: 3em;
    margin-right: 1em;
  }
  .addBtn:hover{
    background-color: #2c9fe5;
    color: white;
  }
  .addBtnHighLight{
    background-color: #2c9fe5;
    color: white;
  }
</style>
