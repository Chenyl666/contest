<template>
  <div style="margin-left: 18.4em;margin-top: 3em">
    <InfoCircleFilledIcon v-if="this.createdContestPage.createContestStep===2" style="width: 1.8em;height: 1.8em;margin-top: -0.45em;margin-right: 0.3em;color: #2c9fe5"/>
    <span style="font-size: 22px">{{getMessage}}</span> <br><br>
    <t-link v-if="this.createdContestPage.createContestStep===2"><span style="font-size: 15px;color: #2c9fe5">点击查看竞赛信息</span></t-link>
  </div>
</template>

<script>
import {InfoCircleFilledIcon} from "tdesign-icons-vue-next";
import {mapState} from "vuex";

export default {
  components: {
    InfoCircleFilledIcon,
  },
  data() {
    return {
    }
  },
  name: "ContestChecking",
  computed: {
    ...mapState(['createdContestPage']),
    getMessage: function () {
        return '竞赛信息已经提交成功，等待审核中！'
    }
  }
}
</script>

<style scoped>

</style>