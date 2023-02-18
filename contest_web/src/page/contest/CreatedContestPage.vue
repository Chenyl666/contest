<template>
  <div style="width: 55em;margin-left: 18em;margin-top: 2em">
    <span style="font-size: 1.5em;font-weight: bold;margin-left: 1em;border-left: #2c9fe5 solid 4px;padding-left: 1em">创建竞赛</span>
    <t-steps style="margin-top: 2em;margin-left: 2em" :current="this.step" readonly>
      <t-step-item title="比赛信息" />
      <t-step-item title="报名信息" />
      <t-step-item title="提交审核" />
    </t-steps>
    <router-view @last-step="toLastStep" @next-step="toNextStep" style="margin-top: 2em;margin-left: 2em"/>
  </div>
</template>

<script>
import {store} from "@/store";
import {mutationName} from "@/store/mutations/const.name";
import router from "@/router/router";
export default {
  name: "CreatedContestPage",
  components: {},
  data() {
    return {
      steps: ['first','second','third'],
      step: store.state.createdContestPage.createContestStep
    }
  },
  methods: {
    toLastStep: function () {
      store.commit(mutationName.SET_CREATE_CONTEST_STEP,store.state.createdContestPage.createContestStep - 1)
      this.toStepPage()
    },
    toNextStep: function () {
      store.commit(mutationName.SET_CREATE_CONTEST_STEP,store.state.createdContestPage.createContestStep + 1)
      this.toStepPage()
    },
    toStepPage: function () {
      router.push('/contest/create/'.concat(this.steps[store.state.createdContestPage.createContestStep]))
    }
  },
  mounted() {
    store.commit(mutationName.SET_CREATE_CONTEST_STEP,0)
    router.push('/contest/create/'.concat(this.steps[store.state.createdContestPage.createContestStep]))
  },
  watch: {
    "$store.state.createdContestPage.createContestStep": {
      handler: function(newVal) {
        this.step = newVal
      }
    }
  }
}
</script>

<style scoped>

</style>