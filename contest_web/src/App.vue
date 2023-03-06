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
      :key="$route.path"
      @to-register="toRegister"
      @to-login="toLogin"
      @to-modify="toModify"
      @to-index="toIndex"
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
    }
  },
  computed: {
    showHeader: function () {
      return this.$route.path.indexOf('/question/repo/detail') === -1
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
          router.push('/contest/detail/430478969909809152')
          // router.push('/usr/detail')
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
