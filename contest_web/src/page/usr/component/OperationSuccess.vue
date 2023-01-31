<template>
  <img src="../../../assets/operation_success.png" alt="" class="logo"/>
  <p style="margin-left: 9.2em"><span class="tips">{{operation}}</span></p>
  <p style="margin-left: 7em;margin-top: 2em"><span style="color: red">{{this.seconds + ' '}}</span><span>秒后将跳转至登录页</span></p>
  <p style="margin-left: 6.5em;margin-top: 1.5em">
    <span style="color: gray;font-size: 0.8em">如果页面没有跳转，</span>
    <t-link @click="$emit('to-login')" style="font-size: 0.8em;color: #2c9fe5;">请点击这里</t-link>
  </p>
</template>

<script>
import router from "@/router/router";

export default {
  name: "OperationSuccess",
  created() {
    this.$watch(
        () => router.currentRoute.value.query.operation,
        (to) => {
          this.operation = to
        }
    )
  },
  data() {
    return {
      operation: router.currentRoute.value.query.operation,
      seconds: 0,
      clock: null
    }
  },
  methods: {
  },
  beforeRouteEnter: function (to,from,next) {
    next(vm => {
      vm.seconds = 3
      vm.clock = setInterval(() => {
        vm.seconds--
        if(vm.seconds < 0){
          vm.seconds = 0
          clearInterval(vm.clock)
          vm.$emit('to-login')
        }
      },1000)
    })
  }
}
</script>

<style scoped>
  .logo{
    width: 7em;
    height: 7em;
    margin-left: 8.5em;
    margin-top: 4em;
  }
  .tips{
    font-size: 1.4em;
    color: #2c9fe5;
    font-weight: bold
  }
</style>