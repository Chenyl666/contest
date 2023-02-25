<template>

  <t-header class="header">
    <div style="margin-left: 1em;margin-top: 1.5em;display: inline-block;float: left">
      <span style="font-size: 26px;">标题：</span>
    </div>
    <t-input style="width: 30%;display: inline-block;margin-top: 1.7em" v-model="questionRepo.questionRepoName"/>
    <t-button theme="default" class="right-top-btn" style="margin-left: 1.5em;margin-right: 1em">保存并返回</t-button>
    <t-button theme="default" class="right-top-btn">预览</t-button>
  </t-header>
  <div class="left">
    <div style="margin-left: 1.5em;margin-top: 1.5em">
      <span style="font-size: 14px">题量：</span>
      <span style="color: red;font-size: 14px">10</span>
      <span style="margin-left: 5em;font-size: 14px">总分：</span>
      <span style="color: red;font-size: 14px">100.0</span>
    </div>
    <QuestionIndex/>
  </div>
  <div class="right">
    <PaperQuestionContent/>
  </div>
</template>

<script>
import QuestionIndex from "@/page/question/component/QuestionIndex";
import PaperQuestionContent from '@/page/question/component/PaperQuestionContent'
export default {
  name: "QuestionRepositoryDetail",
  components: {QuestionIndex,PaperQuestionContent},
  data() {
    return {
      questionRepo: {
        questionRepoName: this.$route.params.questionRepoId,
        questionRepoId: null,
        questionTagId: null,
        questionRepoType: null
      }
    }
  },
  methods: {

  },
  computed: {

  },
  mounted() {
    // this.$route.params.questionRepoId
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