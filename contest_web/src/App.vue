<template>
  <HeaderDirection @to-login="toLogin" @to-register="toRegister" ref="test"/>
  <router-view
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
import {result} from "@/const/request.result";
import {saveToken} from "@/common/login/token.store";
import {store} from "@/store";
import {mutationName} from "@/store/mutation/const.name";

export default {
  name: 'App',
  components: {
    HeaderDirection
  },
  data() {
    return {

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
    }
  },
  mounted() {
    let token = cookies.get('token');
    if(token != null){
      store.commit(mutationName.SET_TOKEN,token)
      request.post('/login/auth/token').then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          saveToken(resp.data['data'])
          router.push('/main')
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
