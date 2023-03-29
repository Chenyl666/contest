<template>
  <t-space direction="vertical">
    <t-loading attach="#alice" size="small" :loading="loading"></t-loading>
    <div v-if="!loading && isPublish" id="alice" style="width: 70em;height: 50em">
      <h2><span style="color: #0075FF">{{getUserName+' '}}</span>您好！您的成绩如下：</h2>
      <t-list style="border: #cccccc solid 1px">
        <t-list-item style="border-bottom: #cccccc solid 1px">
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">用户昵称：</t-list-item>
            <t-list-item style="display: inline-block">{{contestUserResult.userName}}</t-list-item>
          </t-list>
        </t-list-item>
        <t-list-item style="border-bottom: #cccccc solid 1px">
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">用户ID：</t-list-item>
            <t-list-item style="display: inline-block">{{contestUserResult.userId}}</t-list-item>
          </t-list>
        </t-list-item>
        <t-list-item style="border-bottom: #cccccc solid 1px">
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">身份证号：</t-list-item>
            <t-list-item style="display: inline-block">{{contestUserResult.identify}}</t-list-item>
          </t-list>
        </t-list-item>
        <t-list-item style="border-bottom: #cccccc solid 1px">
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">所属单位：</t-list-item>
            <t-list-item style="display: inline-block">{{contestUserResult.unit}}</t-list-item>
          </t-list>
        </t-list-item>
        <t-list-item style="border-bottom: #cccccc solid 1px">
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">竞赛名称：</t-list-item>
            <t-list-item style="display: inline-block">{{contestUserResult.contestSubject}}</t-list-item>
          </t-list>
        </t-list-item>
        <t-list-item style="border-bottom: #cccccc solid 1px">
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">竞赛id：</t-list-item>
            <t-list-item style="display: inline-block">{{contestUserResult.contestId}}</t-list-item>
          </t-list>
        </t-list-item>
        <t-list-item style="border-bottom: #cccccc solid 1px">
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">竞赛成绩：</t-list-item>
            <t-list-item style="display: inline-block">{{contestUserResult.sumScore}}</t-list-item>
          </t-list>
        </t-list-item>
        <t-list-item>
          <t-list>
            <t-list-item style="display: inline-block;width: 5em">竞赛名次：</t-list-item>
            <t-list-item style="display: inline-block">4</t-list-item>
          </t-list>
        </t-list-item>
      </t-list>
    </div>
    <div v-if="!loading && !isPublish" id="alice" style="width: 70em;height: 50em">
      <h1>本场比赛的结果还未公布，请耐心等待！</h1>
    </div>
  </t-space>
</template>

<script>
import {mapGetters} from "vuex";
import {gettersName} from "@/store/getters/getters.name";
import {getContestDetailById, getContestUserResult} from "@/api/contest";

export default {
  name: "ContestResultOfUser",
  data() {
    return {
      loading: false,
      isPublish: false,
      contestDetail: {},
      contestUserResult: {}
    }
  },
  computed: {
    ...mapGetters([gettersName.GET_USER_ID,gettersName.GET_USER_NAME])
  },
  mounted() {
    this.loading = true
    setTimeout(async () => {
      await getContestDetailById(this.$route.params.contestId).then(resp => {
        this.contestDetail = resp.data.data
      })
      await getContestUserResult(this.contestDetail.contestId).then(resp => {
        this.contestUserResult = resp.data.data
      })
      this.isPublish = this.contestDetail.contestStatus === 'PUBLISH'
      this.loading = false
    },500)
  }
}
</script>

<style scoped>

</style>