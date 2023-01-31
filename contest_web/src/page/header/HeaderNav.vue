<template>
  <t-header>
    <div style="background-color: white;width: 100%;height: 4em;margin-bottom: -0.5em;border-bottom: 2px solid #f6f5f7">
      <img src="../../assets/logo.png" style="width:13.44em;height: 4em;margin-left: 10em;" alt="LOGO">
      <div style="margin-left: 28em;position: fixed">
        <ul style="display: flex;list-style-type:none;margin-top: -3em">
          <li class="dir-item">
            <t-badge count="" class="txt">
              首页
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge count="hot" class="txt">
              竞赛
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge count="" class="txt">
              题库
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge count="" class="txt">
              课程
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge count="" class="txt">
              图书
            </t-badge>
          </li>
        </ul>
      </div>
      <div style="margin-left: 73.5em;margin-top: -3.3em;display: flex">
        <t-button
            @click="this.$emit('to-login')"
            class="btn"
            theme="default"
            variant="text">
          登录</t-button>
        <t-button
            @click="this.$emit('to-register')"
            class="btn"
            theme="primary"
            variant="base"
            :ghost="registerBtn.ghost"
            @mouseenter="registerBtn.ghost = false"
            @mouseleave="registerBtn.ghost = true">
          注册
        </t-button>
      </div>
    </div>
  </t-header>
</template>

<script>
export default {
  name: "HeaderDirection",
  data() {
    return {
      registerBtn: {
        ghost: true
      }
    }
  },
  methods: {
  }
}
</script>

<style scoped>
  .dir-item{
    margin-left: 3.5em;
    cursor: pointer;
  }
  .txt{
    font-size: 1.05em;
  }
  .btn{
    margin-left: 1em;
  }
  div{
    white-space:nowrap
  }
  t-button{
    white-space:nowrap
  }

</style>