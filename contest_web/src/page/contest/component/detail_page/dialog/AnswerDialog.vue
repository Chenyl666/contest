<template>
  <div>
    <t-dialog
        v-model:visible="visit"
        attach="body"
        width="50em"
        header="题目详细"
        destroy-on-close
        @close="onClose"
        :top="`2em`"
        @confirm="onConfirm">
      <template #body>
        <div style="overflow-y: auto;height: 30em;border: #cccccc solid 1px;padding-left: 1em;padding-right: 1em">
          <h3>题目描述：</h3>
          <div v-html="answer.questionDetailDto.questionContent"/>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.JUDGE_QUESTION">
            <p>正确</p>
            <p>错误</p>
          </div>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.SINGLE_OPTION_QUESTION">
            <div :key="option" v-for="option in answer.questionDetailDto.questionOption">
              <div style="display: inline-block">{{option.option + '. '}}</div>
              <div style="display: inline-block" v-html="option.content"/>
            </div>
          </div>
          <h3>参赛者回答：</h3>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.ANSWER_QUESTION" v-html="answer.answerContent"/>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.SUPPLEMENT_QUESTION">
            <p :key="index" v-for="(ans,index) in JSON.parse(answer.answerContent)">
              第{{index+1}}空：{{ans}}
            </p>
          </div>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.JUDGE_QUESTION">
            {{answer.answerContent === 'true'?'正确':answer.answerContent === 'false'?'错误':'空'}}
          </div>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.SINGLE_OPTION_QUESTION">
            {{answer.answerContent}}
          </div>
          <h3>参考答案</h3>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.ANSWER_QUESTION" v-html="answer.questionDetailDto.questionReferenceAnswer"/>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.SUPPLEMENT_QUESTION">
            <p :key="index" v-for="(ans,index) in JSON.parse(answer.questionDetailDto.questionReferenceAnswer)">
              第{{index+1}}空：{{ans}}
            </p>
          </div>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.JUDGE_QUESTION">
            {{answer.questionDetailDto.questionReferenceAnswer === 'true'?'正确':'错误'}}
          </div>
          <div v-if="answer.questionDetailDto.questionType === QUESTION_TYPE.SINGLE_OPTION_QUESTION">
            {{answer.questionDetailDto.questionReferenceAnswer}}
          </div>
        </div>
        <div style="float: right">
          分值（总分：{{answer.questionDetailDto.score}}）
          <t-input v-model="data.score" style="width: 10em"/>
        </div>
      </template>
    </t-dialog>
  </div>
</template>
<script setup>
import {reactive, toRef} from 'vue';
import {defineProps,defineEmits} from "vue";

const QUESTION_TYPE = {
  SINGLE_OPTION_QUESTION: 'SINGLE_OPTION_QUESTION',
  SUPPLEMENT_QUESTION: 'SUPPLEMENT_QUESTION',
  JUDGE_QUESTION: 'JUDGE_QUESTION',
  ANSWER_QUESTION: 'ANSWER_QUESTION',
  PROGRAMMING_QUESTION: 'PROGRAMMING_QUESTION',
  NULL: 'NULL'
}

const props = defineProps({
  visitable: {
    type: Boolean,
    required: true
  },
  answer: {
    type: Object,
    required: true
  }
})

const data = reactive({
  score: 0
})

const emits = defineEmits(['on-confirm','on-close'])

const visit = toRef(props,'visitable')

const onConfirm = () => {
  emits('on-confirm',props.answer.answerId,data.score)
}

const onClose = () => {
  emits('on-close')
}


</script>
<style scoped>
body {
  width: 3000px;
}
</style>
