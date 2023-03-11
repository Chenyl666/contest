<template>
  <ContestQuestionList :contest-detail="contest.contestDetail" :question-list="contest.questionList"/>
</template>

<script>

import ContestQuestionList from "@/page/contest/component/ContestQuestionList";
import {getContestDetailById} from "@/api/contest";
import router from "@/router/router";
import {result} from "@/common/request.result";
import {getAnswer, getProgram} from "@/api/online";
import {QUESTION_TYPE} from "@/common/question";

export default {
  name: "ContestOnlinePage",
  components: {ContestQuestionList},
  data() {
    return {
      contest: {
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
          ANSWER_QUESTION: [],
          SINGLE_OPTION_QUESTION: [],
          SUPPLEMENT_QUESTION: [],
          JUDGE_QUESTION: [],
          PROGRAMMING_QUESTION: []
        }
      }
    }
  },
  methods: {},
  async mounted() {
    await getContestDetailById(router.currentRoute.value.params.contestId).then(resp => {
      if (resp.data.resultCode === result.code.SUCCESS && resp.data.data != null) {
        this.contest.contestDetail = resp.data.data
      }
    })
    switch (this.contest.contestDetail.contestTypeId){
      case 1: {
        getAnswer(this.$route.params.contestId).then(resp => {
          if (resp.data.resultCode === result.code.SUCCESS && resp.data.data != null) {
            let list = resp.data.data
            console.log(list)
            for(let key in list){
              for(let i=0;i<list[key].length;i++){
                this.contest.questionList[key].push(list[key][i])
              }
            }
          }
        })
        break
      }
      case 2:{
        await getProgram(this.$route.params.contestId).then(resp => {
          if (resp.data.resultCode === result.code.SUCCESS && resp.data.data != null) {
            let list = resp.data.data
            console.log(list)
            for(let i=0;i<list.length;i++){
              this.contest.questionList[QUESTION_TYPE.PROGRAMMING_QUESTION].push(list[i])
            }
          }
        })
        break
      }
    }
  }
}
</script>

<style scoped>

</style>