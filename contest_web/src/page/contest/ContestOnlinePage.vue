<template>
  <div class="header">
    <t-link @click="complete" style="margin-left: 13em;margin-top: 1.1em" hover="color">
      <ChevronLeftIcon style="width: 1.7em;height: 1.7em"/>
      <span style="font-size: 17px;font-weight: bold">退出答题</span>
    </t-link>
    <div style="font-size: 17px;font-weight: bold;text-align: center;margin-top: -1.6em">{{contest.contestDetail.contestSubject}}</div>
    <div style="float: right;font-weight: bold;margin-right: 9em;margin-top: -1.35em;white-space:nowrap">剩余时间：{{ getTime }}</div>
  </div>
  <ContestQuestionList @on-question-selected="onPaperQuestionSelected"
                       @to-complete="complete"
                       ref="questionList"
                       :contest-detail="contest.contestDetail"
                       :question-list="contest.questionList"/>
  <PageContest v-if="contest.contestDetail.contestTypeId === 1"
               :current-question="contest.current"
               @on-option-selected="onOptionSelected"
               @on-judge-selected="onJudgeSelected"
               @on-supply-change="onSupplyChange"
               @on-answer-change="onAnswerChange"
               @to-next-question="toNextQuestion"
               :key="key"/>
  <ProgramContest v-if="contest.contestDetail.contestTypeId === 2"
                  @flag-question-success="flagQuestionSuccess"
                  :current-question="contest.current"/>

</template>

<script>

import ContestQuestionList from "@/page/contest/component/ContestQuestionList";
import {getContestDetailById} from "@/api/contest";
import router from "@/router/router";
import {result} from "@/common/request.result";
import {
  checkContestStatus,
  getAnswer,
  getProgram,
  savePaperQuestionAnswer,
  setEndStatus,
  setIngStatus
} from "@/api/online";
import {QUESTION_TYPE} from "@/common/question";
import PageContest from "@/page/contest/component/PaperContest"
import {ChevronLeftIcon} from 'tdesign-icons-vue-next'
import {MessagePlugin} from "tdesign-vue-next";
import ProgramContest from "@/page/contest/component/ProgramContest"

export default {
  name: "ContestOnlinePage",
  components: {ContestQuestionList, PageContest, ChevronLeftIcon,ProgramContest},
  emits: ['toRegister', 'toLogin', 'toModify', 'toIndex', 'decrNotifyCount'],
  data() {
    return {
      key: 0,
      saveInterval: null,
      contest: {
        interval: null,
        currentTime: new Date(),
        visitable: false,
        contestDetail: {
          contestId: '',
          contestSubject: '',
          contestPrice: '',
          requiredContestPaying: '',
          contestDescription: '',
          enrollStartTime: '',
          enrollEndTime: '',
          contestStartTime: '',
          contestEndTime: '',
          contestStatus: '',
          createdBy: '',
          contestTypeId: '',
          contestLevel: '',
          contestPicture: '',
          groupingContest: '',
          groupingMaxNum: '',
          groupingMinNum: '',
          autoPrise: '',
          usePercent: '',
          contestPriseDistributes: [],
          organizeUnit: '',
          questionRepoId: ''
        },
        questionList: {
          SINGLE_OPTION_QUESTION: [],
          JUDGE_QUESTION: [],
          SUPPLEMENT_QUESTION: [],
          ANSWER_QUESTION: [],
          PROGRAMMING_QUESTION: []
        },
        current: {
          currentIndex: 1,
          currentPaperAns: {
            answerId: '',
            contestId: '',
            questionDetail: {
              questionId: '',
              questionType: QUESTION_TYPE.SINGLE_OPTION_QUESTION,
              questionContent: '',
              questionReferenceAnswer: '',
              questionExplain: '',
              questionRepoId: '',
              createdBy: '',
              score: '',
              questionOption: []
            },
            answerContent: '',
            createdBy: '',
            questionType: ''
          }
        },
      }
    }
  },
  methods: {
    setCurrentTime: function () {
      this.contest.interval = setInterval(() => {
        let date = new Date()
        let endTime = new Date(this.contest.contestDetail.contestEndTime)
        if(endTime.getTime() <= date.getTime()){
          clearInterval(this.contest.interval)
          savePaperQuestionAnswer(this.contest.questionList).catch(() => MessagePlugin.error('请检查网络问题！'))
          setEndStatus(this.$route.params.contestId,2)
          router.push('/end')
        }else{
          this.contest.currentTime = date
        }
      }, 1000)
    },
    onPaperQuestionSelected: function (ans) {
      this.contest.current = ans
      this.key = (this.key+1)%1000
    },
    onOptionSelected: function (option) {
      this.contest.current.currentPaperAns.answerContent = option
      this.contest.questionList[this.contest.current.currentPaperAns.questionType][this.contest.current.currentIndex].answerContent = option
    },
    onJudgeSelected: function (ans) {
      this.contest.current.currentPaperAns.answerContent = ans
      this.contest.questionList[this.contest.current.currentPaperAns.questionType][this.contest.current.currentIndex].answerContent = ans
    },
    onSupplyChange: function (list) {
      this.contest.current.currentPaperAns.answerContent = JSON.stringify(list)
      this.contest.questionList[this.contest.current.currentPaperAns.questionType][this.contest.current.currentIndex].answerContent = JSON.stringify(list)
    },
    onAnswerChange: function (content) {
      this.contest.current.currentPaperAns.answerContent = content
      this.contest.questionList[this.contest.current.currentPaperAns.questionType][this.contest.current.currentIndex].answerContent = content
    },
    toNextQuestion: function () {
      this.$refs.questionList.toNextQuestion(this.contest.current.currentIndex,this.contest.current.currentPaperAns.questionType)
    },
    save: function () {
      savePaperQuestionAnswer(this.contest.questionList).catch(() => MessagePlugin.error('请检查网络问题！'))
    },
    complete: function () {
      if(confirm('交卷后将无法继续答题，确认继续吗？')){
        clearInterval(this.saveInterval)
        clearInterval(this.contest.interval)
        savePaperQuestionAnswer(this.contest.questionList).catch(() => MessagePlugin.error('请检查网络问题！'))
        setEndStatus(this.$route.params.contestId,2)
        router.push('/end')
      }
    },
    flagQuestionSuccess: function () {
      this.contest.questionList.PROGRAMMING_QUESTION[this.contest.current.currentIndex].answerContent = "true"
    }
  },
  computed: {
    getTime() {
      let timestamp = new Date(this.contest.contestDetail.contestEndTime).getTime() - this.contest.currentTime.getTime()
      let day = parseInt(timestamp / (1000 * 3600 * 24))
      timestamp = timestamp % (1000 * 3600 * 24)
      let hour = parseInt(timestamp / (1000 * 3600))
      timestamp = timestamp % (1000 * 3600)
      let minutes = parseInt(timestamp / (1000 * 60))
      timestamp = timestamp % (1000 * 60)
      let seconds = parseInt(timestamp / (1000))
      if (day < 10) {
        day = '0' + day
      }
      if (hour < 10) {
        hour = '0' + hour
      }
      if (minutes < 10) {
        minutes = '0' + minutes
      }
      if (seconds < 10) {
        seconds = '0' + seconds
      }
      return day + '天' + ' ' + hour + ':' + minutes + ':' + seconds
    }
  },
  beforeRouteLeave: function () {
    clearInterval(this.saveInterval)
    clearInterval(this.contest.interval)
  },
  beforeRouteUpdate: function () {
    clearInterval(this.saveInterval)
    clearInterval(this.contest.interval)
  },
  async mounted() {
    let checkResult = false
    await checkContestStatus(router.currentRoute.value.params.contestId).then(resp => {
      if(resp.data.resultCode === result.code.SUCCESS){
        checkResult = true
      }else{
        router.push('/404')
      }
    })
    if(!checkResult){
      router.push('/end')
      return
    }
    await getContestDetailById(router.currentRoute.value.params.contestId).then(resp => {
      if (resp.data.resultCode === result.code.SUCCESS && resp.data.data != null) {
        this.contest.contestDetail = resp.data.data
      }
    })
    switch (this.contest.contestDetail.contestTypeId) {
      case 1: {
        await getAnswer(this.$route.params.contestId).then(resp => {
          if (resp.data.resultCode === result.code.SUCCESS && resp.data.data != null) {
            let list = resp.data.data
            for (let key in list) {
              for (let i = 0; i < list[key].length; i++) {
                this.contest.questionList[key].push(list[key][i])
              }
            }
          }
        })
        let key = Object.keys(this.contest.questionList)[0];
        this.contest.current.currentPaperAns = this.contest.questionList[key][0]
        this.contest.current.currentIndex = 0
        break
      }
      case 2: {
        await getProgram(this.$route.params.contestId).then(resp => {
          if (resp.data.resultCode === result.code.SUCCESS && resp.data.data != null) {
            let list = resp.data.data
            console.log(list)
            for (let i = 0; i < list.length; i++) {
              this.contest.questionList[QUESTION_TYPE.PROGRAMMING_QUESTION].push(list[i])
            }
          }
        })
        this.contest.current.currentPaperAns = this.contest.questionList[QUESTION_TYPE.PROGRAMMING_QUESTION][0]
        this.contest.current.currentIndex = 0
        break
      }
    }
    await setIngStatus(this.$route.params.contestId, 1)
    this.setCurrentTime()
    if(this.contest.contestDetail.contestTypeId === 1){
      this.saveInterval = setInterval(() => {
        this.save()
      },30000)
      this.key = (this.key+1)%1000
    }
  }
}
</script>

<style scoped>
.header {
  /*margin-top: -1.3em;*/
  width: 96em;
  height: 3.5em;
  /*background-color: red;*/
  box-shadow: 0 1px 4px 0 rgb(0 0 0 / 2%), 0 2px 12px 0 rgb(0 0 0 / 4%);
  overflow: initial;
}
</style>