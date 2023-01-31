<template>
  <div class="board">
    <div class="form">
      <div>
        <div>
          <div class="method" style="margin-top: 3em;margin-left: 5em">
            <span style="cursor: pointer;" :class="isEmailCodeMethodBtnHighLight" @click="toEmailCodeLogin">邮箱登录</span>
          </div>
          <div class="method" style="margin-top: -1.35em;margin-left: 13em;">
            <span style="cursor: pointer;" :class="isPasswordMethodBtnHighLight" @click="toPasswordLogin">账号登录</span>
          </div>
        </div>
        <router-view
            @to-register="$emit('to-register')"
            @to-modify="$emit('to-modify')"
            style="margin-left: 4em;margin-top: 2em"/>
      </div>
      <div style="margin-left: 29em;margin-top: -20em;border-left: 2px solid #f6f5f7;height: 22em">
        <img src="../../assets/logo.png" style="width:17em;height: 5em;margin-left: 0.8em;margin-top: -0.3em" alt="">
        <div style="font-size: 1.1em;margin-left: 0.5em;margin-top: 0.8em">只需要一个CloudContest竞赛账号</div>
        <div style="font-size: 1em;margin-left: 4.1em">即可享受网络竞赛服务</div>
        <CarVideo style="width: 13em;margin-left: 2.8em"/>
      </div>
    </div>

  </div>
</template>

<script>
import CarVideo from "@/page/component/assets/CarVideo";
import router from "@/router/router";

export default {
  name: "LoginBoard",
  components: {CarVideo},
  mounted() {
    if(router.currentRoute.value.fullPath === '/login/email-code'){
      this.emailCodeMethodBtnHighLight = true
      this.passwordMethodBtnHighLight = false
    }else if(router.currentRoute.value.fullPath === '/login/password'){
      this.passwordMethodBtnHighLight = true
      this.emailCodeMethodBtnHighLight = false
    }
  },
  data() {
    return {
      emailCodeMethodBtnHighLight: false,
      passwordMethodBtnHighLight: false
    }
  },
  methods: {
    toEmailCodeLogin() {
      router.push('/login/email-code')
      this.emailCodeMethodBtnHighLight = true
      this.passwordMethodBtnHighLight = false
    },
    toPasswordLogin() {
      router.push('/login/password')
      this.passwordMethodBtnHighLight = true
      this.emailCodeMethodBtnHighLight = false
    },
  },
  computed: {
    isEmailCodeMethodBtnHighLight(){
      return this.emailCodeMethodBtnHighLight? 'highLightMethodBtn':'lowLightMethodBtn'
    },
    isPasswordMethodBtnHighLight(){
      return this.passwordMethodBtnHighLight ? 'highLightMethodBtn':'lowLightMethodBtn'
    }
  }
}
</script>

<style scoped>
  .board{
    margin-top: 0.6em;
  }
  .form{
    box-shadow: 0 5px 10px 0 rgb(0 0 0 / 10%);
    border-radius: 6px;
    background-color: white;
    width: 50%;
    height: 26em;
    margin-left: 23em;
    margin-top: 4.5em;
    overflow: hidden;
    transition: .5s linear;
  }
  .method{
    font-size: 1.3em;
  }
  div{
    white-space:nowrap
  }
  .highLightMethodBtn{
    color: black;
    font-weight: bold
  }
  .lowLightMethodBtn{
    color: gray;
  }
</style>