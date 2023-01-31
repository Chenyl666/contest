<template>
  <HeaderDirection @to-login="toLogin" @to-register="toRegister" ref="test"/>
  <router-view
      @to-register="toRegister"
      @to-login="toLogin"
      @to-modify="toModify"
      ref="page"/>
</template>

<script>

import HeaderDirection from "@/page/header/HeaderNav";
import router from "@/router/router";
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
    }
  },
  mounted() {
    router.push("/login")
  }
}
</script>

<style>

</style>
