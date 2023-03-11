<template>
  <HeaderDirection
      v-if="showHeader"
      @to-login="toLogin"
      @to-register="toRegister"
      @to-user-detail = "toUserDetail"
      @to-contest-list="toContestList" ref="test"
      @to-contest-detail="toContestDetail"
      @to-question-repo="toQuestionRepo"/>
  <router-view
      @to-register="toRegister"
      @to-login="toLogin"
      @to-modify="toModify"
      @to-index="toIndex"
      @decr-notify-count="decreaseNotifyCount"
      ref="page"/>
</template>

<script>

import HeaderDirection from "@/page/header/HeaderNav";
import router from "@/router/router";
import cookies from "js-cookie";
import {request} from "@/util/request";
import {result} from "@/common/request.result";
import {saveToken} from "@/common/token.store";
import {store} from "@/store";
import {mutationName} from "@/store/mutations/const.name";
import screenfull from "screenfull"

export default {
  name: 'App',
  components: {
    HeaderDirection
  },
  data() {
    return {
      key: 1
    }
  },
  methods: {
    toRegister: () => {
      router.push('/usr/register')
    },
    toLogin: () => {
      router.push("/login")
    },
    toModify: () => {
      router.push("/usr/modify")
    },
    toIndex: () => {
      router.push("/contest")
    },
    toContestList: () => {
      router.push('/contest/list')
    },
    toQuestionRepo: () => {
      router.push('/question/repo')
    },
    toUserDetail: () => {
      router.push('/usr/detail')
    },
    toContestDetail: (contestId) => {
      router.push('/contest/detail/'.concat(contestId))
    },
    decreaseNotifyCount: function () {
      this.$refs.test.decreaseNotifyCount()
    },
    setFullScreen: function () {
      screenfull.toggle()
    }
  },
  watch: {
    "$store.state.fullScreen":{
      handler: function (newVal) {
        if(newVal === true){
          screenfull.toggle()
        }else{
          screenfull.exit()
        }
      }
    }
  },
  computed: {
    showHeader: function () {
      return this.$route.path.indexOf('/question/repo/detail') === -1 && this.$route.path.indexOf('/contest/online') === -1
    }
  },
  mounted() {
    let token = cookies.get('token');
    if(token != null){
      store.commit(mutationName.SET_TOKEN,token)
      request.post('/login/auth/token').then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          saveToken(resp.data['data'])
          // router.push('/main')
          // router.push('/contest/online/page/424399885819711488')
          router.push('/contest/online/tip/425331313520676864')
          // router.push('/contest/online/tip/424399885819711488')
        }else{
          router.push("/login")
        }
      })
    }else{
      router.push("/login")
    }
  }
}
</script>

<style>

</style>
