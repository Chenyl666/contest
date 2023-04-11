<template>

  <t-header class="header">
    <div style="margin-left: 1em;margin-top: 1.5em;display: inline-block;float: left">
      <span style="font-size: 26px;">标题：</span>
    </div>
    <t-input style="width: 30%;display: inline-block;margin-top: 1.7em"
             @blur="titleChange"
             v-model="questionRepo.questionRepoName"/>
    <t-button theme="default"
              class="right-top-btn"
              @click="back"
              style="margin-left: 1.5em;margin-right: 1em">返回</t-button>
<!--    <t-button theme="default" class="right-top-btn">预览</t-button>-->
  </t-header>
  <div class="left">
    <div style="margin-left: 1.5em;margin-top: 1.5em">
      <span style="font-size: 14px">题量：</span>
      <span style="color: red;font-size: 14px">{{questionRepo.questionNum}}</span>
      <span style="margin-left: 5em;font-size: 14px">总分：</span>
      <span style="color: red;font-size: 14px">{{questionRepo.score}}</span>
    </div>
    <QuestionIndex ref="index"
                   @onload-question-index="loadQuestionIndex"
                   @load-question="loadQuestion"
                   :question-index-list="questionIndexList"/>
  </div>
  <div v-if="questionRepo.questionRepoType === 'PAPER'" class="right">
    <PaperQuestionContent @clear-index-highlight="clearIndexHighLight" ref="paper" @load-question="loadQuestionIndex"/>
  </div>
  <div v-if="questionRepo.questionRepoType === 'PROGRAMMING'" class="right">
    <ProgrammingQuestionContent  ref="program" @clear-index-highlight="clearIndexHighLight"  @load-question="loadQuestionIndex"/>
  </div>
</template>

<script>
import QuestionIndex from "@/page/question/component/QuestionIndex";
import PaperQuestionContent from '@/page/question/component/PaperQuestionContent'
import {
  getQuestionIndex,
  getQuestionProgramIndex,
  getQuestionRepoById,
  updateQuestionRepoNameById
} from "@/api/question";
import {result} from "@/common/request.result";
import {toRef} from "vue";
import {StringUtil} from "@/util/string.util";
import {MessagePlugin} from "tdesign-vue-next";
import ProgrammingQuestionContent from "@/page/question/component/ProgrammingQuestionContent";

export default {
  name: "QuestionRepositoryDetail",
  components: {ProgrammingQuestionContent, QuestionIndex,PaperQuestionContent},
  data() {
    return {
      questionRepo: {
        questionRepoName: null,
        questionRepoId: null,
        questionTagId: null,
        questionRepoType: null,
        questionNum: 0,
        score: 0
      },
      questionIndexList: {
        SINGLE_OPTION_QUESTION: [],
        SUPPLEMENT_QUESTION: [],
        JUDGE_QUESTION: [],
        ANSWER_QUESTION: [],
        PROGRAMMING_QUESTION: []
      }
    }
  },
  methods: {
    loadQuestion: function (questionId) {
      if(this.questionRepo.questionRepoType === 'PAPER'){
        this.$refs.paper.loadQuestion(questionId)
      }
      if(this.questionRepo.questionRepoType === 'PROGRAMMING'){
        this.$refs.program.loadQuestion(questionId)
      }
    },
    clearIndexHighLight: function () {
      this.$refs.index.clearIndexHighLight()
    },
    loadQuestionIndex: function () {
      if(this.questionRepo.questionRepoType === 'PAPER'){
        getQuestionIndex(this.$route.params.questionRepoId).then(resp => {
          if(resp.data.resultCode === result.code.SUCCESS){
            this.questionIndexList = toRef(resp.data,'data')
            this.questionRepo.questionNum = 0
            this.questionRepo.score = 0
            for(let key in this.questionIndexList){
              for(let i=0;i<this.questionIndexList[key].length;i++){
                this.questionIndexList[key][i].questionContent = StringUtil.parseFromHtml(this.questionIndexList[key][i].questionContent)
                this.questionRepo.score += Number.parseFloat(this.questionIndexList[key][i].score)
              }
              this.questionRepo.questionNum += this.questionIndexList[key].length
            }
          }
        })
      }
      if (this.questionRepo.questionRepoType === 'PROGRAMMING') {
        getQuestionProgramIndex(this.$route.params.questionRepoId).then(resp => {
          if (resp.data.resultCode === result.code.SUCCESS) {
            this.questionIndexList = toRef(resp.data, 'data')
            this.questionRepo.questionNum = 0
            this.questionRepo.score = 0
            for (let key in this.questionIndexList) {
              for (let i = 0; i < this.questionIndexList[key].length; i++) {
                this.questionIndexList[key][i].questionContent = StringUtil.parseFromHtml(this.questionIndexList[key][i].questionContent)
                this.questionRepo.score += Number.parseFloat(this.questionIndexList[key][i].score)
              }
              this.questionRepo.questionNum += this.questionIndexList[key].length
            }
          }
        })
      }
    },
    back: function () {
      if(confirm('确定所有数据已经保存了吗？退出后未保存的数据将会丢失')){
        this.$router.push('/question/repo/')
      }
    },
    titleChange: function () {
      if(StringUtil.isEmpty(this.questionRepo.questionRepoName)){
        MessagePlugin.error('题库名称不能为空！')
      }else{
        updateQuestionRepoNameById({
          questionRepoId: this.questionRepo.questionRepoId,
          questionRepoName: this.questionRepo.questionRepoName
        })
      }
    }
  },
  computed: {
  },
  async mounted() {
    await getQuestionRepoById(this.$route.params.questionRepoId).then(resp => {
      if (resp.data.resultCode === result.code.SUCCESS) {
        if(resp.data.data === null){
          this.$router.push('/404')
        }
        this.questionRepo.questionRepoType = resp.data.data.questionRepoType
        this.questionRepo.questionRepoName = resp.data.data.questionRepoName
        this.questionRepo.questionRepoId = this.$route.params.questionRepoId
      }else{
        this.$router.push('/404')
      }
    }).catch(() => {
      this.$router.push('/404')
    })
    if (this.questionRepo.questionRepoType === 'PAPER') {
      getQuestionIndex(this.$route.params.questionRepoId).then(resp => {
        if (resp.data.resultCode === result.code.SUCCESS) {
          this.questionIndexList = toRef(resp.data, 'data')
          for (let key in this.questionIndexList) {
            for (let i = 0; i < this.questionIndexList[key].length; i++) {
              this.questionIndexList[key][i].questionContent = StringUtil.parseFromHtml(this.questionIndexList[key][i].questionContent)
              this.questionRepo.score += Number.parseFloat(this.questionIndexList[key][i].score)
            }
            this.questionRepo.questionNum += this.questionIndexList[key].length
          }
        }
      })
    }
    if (this.questionRepo.questionRepoType === 'PROGRAMMING') {
      getQuestionProgramIndex(this.$route.params.questionRepoId).then(resp => {
        if (resp.data.resultCode === result.code.SUCCESS) {
          this.questionIndexList = toRef(resp.data, 'data')
          for (let key in this.questionIndexList) {
            for (let i = 0; i < this.questionIndexList[key].length; i++) {
              this.questionIndexList[key][i].questionContent = StringUtil.parseFromHtml(this.questionIndexList[key][i].questionContent)
              this.questionRepo.score += Number.parseFloat(this.questionIndexList[key][i].score)
            }
            this.questionRepo.questionNum += this.questionIndexList[
                key].length
          }
        }
      })
    }
  }
}
</script>

<style scoped>
  .header{
    width: 100%;
    height: 5em;
    margin-top: -0.7em;
    background-color: #f6f5f7;
    /*white-space:nowrap*/
  }
  .left{
    margin-top: 2em;
    width: 21%;
    height: 60em;
    background-color: #f6f5f7;
    border-radius: 5px;
    overflow-y: auto;
    overflow-x: hidden;
    border-left: 3px solid #f6f5f7;
    border-right: 3px solid #f6f5f7;
    display: inline-block;
  }
  .right-top-btn{
    float: right;
    margin-top: 1.9em;
    border: #cccccc 1px solid;
  }
  .right{
    width: 75%;
    height: 60em;
    margin-left: 2em;
    margin-top: 2em;
    background-color: #f6f5f7;
    display: inline-block;
    border-radius: 5px;
    overflow-y: scroll;
    /*float: right;*/
  }
</style>