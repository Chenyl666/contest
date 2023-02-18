<template>
  <div style="width: 95em;margin-top: -0.7em">
    <img style="width: 100%;display: block" src="../../assets/blue2.jpg" alt="">
    <t-space style="border: solid white 3px;border-radius: 50%;z-index: 1;cursor: pointer;position: absolute;margin-top: -4em;margin-left: 43.8em" :size="40">
      <t-avatar shape="circle" :image="user.userPic" size="110px" :hide-on-load-failed="false"/>
    </t-space>
    <p style="margin-top: 4em;text-align: center"><span style="font-size: 22px">{{user.userName}}</span></p>
  </div>
  <t-link v-if="this.getUserType === `ORGANIZER`" @click="toCreateContest" theme="primary" style="margin-bottom: 2em;margin-left: 52.2em">
    <span style="font-size: 15px;font-weight: bold">创建竞赛</span>
  </t-link>
  <ContestList v-if="this.getUserType === `ORGANIZER`"/>
</template>

<script>
import {mapGetters, mapState} from "vuex";
import {gettersName} from "@/store/getters/getters.name";
import ContestList from "@/page/contest/component/ContestList";
import router from "@/router/router";

export default {
  name: "ContestListPage",
  components: {ContestList},
  data() {
    return {
      user: {
        userName: '',
        userPic: '',
        userType: '',
      }
    }
  },
  methods: {
    toCreateContest: function () {
      router.push('/contest/create')
    }
  },
  computed: {
    ...mapState(['userDto']),
    ...mapGetters([
        gettersName.GET_USER_PIC,
        gettersName.GET_USER_TYPE,
        gettersName.GET_USER_NAME
    ])
  },
  watch: {
    "$store.state.userDto": {
      handler: function (newVal) {
        if (newVal !== undefined) {
          this.user.userPic = this.getUserPic
          this.user.userName = this.getUserName
          this.user.userType = this.getUserType
        }
      }
    }
  },
  mounted() {
    this.user.userPic = this.getUserPic
    this.user.userName = this.getUserName
    this.user.userType = this.getUserType
  }
}
</script>

<style scoped>

</style>