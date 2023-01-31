<template>
  <div style="width: 350px">
    <t-form ref="form" :data="formData" :colon="true" :label-width="0" @submit="onSubmit">
      <t-form-item name="account">
        <t-input size="large" @change="cleanEmailInputAlert" :status="data.emailInput.showError" :tips="data.emailInput.tip" v-model="formData.userEmail" clearable placeholder="请输入邮箱">
          <template #prefix-icon>
            <mail-icon/>
<!--            <desktop-icon />-->
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password">
        <t-input size="large" @change="cleanEmailCodeInputAlert" :status="data.emailCodeInput.showError" :tips="data.emailCodeInput.tip" v-model="formData.userEmailCode" type="" clearable placeholder="请输入邮箱验证码">
          <template #prefix-icon>
            <check-circle-icon/>
          </template>
        </t-input>
        <t-button size="large" style="width: 8em" @click="sendLoginEmailCode" :disabled="data.showGetEmailCodeBtn.disable">
          {{ data.showGetEmailCodeBtn.show }}
        </t-button>
      </t-form-item>

      <div style="display: flex;margin-top: 2em">
        <input v-model="data.isAgree" type="checkbox" style="display: inline;border-color: #B5C0CA"/>
        <div style="font-size: 0.7em;display: inline;margin-top: 0.05em"><span style="color: #B5C0CA">登录即代表同意</span>《CloudContest服务协议》《隐私协议》</div>
      </div>

      <t-form-item style="margin-top: 0em">
        <t-button theme="primary" type="submit" block style="height: 3em;">登录</t-button>
      </t-form-item>
      <t-button @click="$emit('to-register')" variant="text" style="margin-left: 0;padding: 0">快速注册</t-button>
      <t-button @click="$emit('to-modify')" variant="text" style="padding: 0;margin-left: 15.7em">忘记密码？</t-button>
    </t-form>
  </div>
</template>
<script setup>
import { reactive } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import {CheckCircleIcon, MailIcon} from 'tdesign-icons-vue-next';
import {request} from "@/util/request";
import {loginParam} from "@/const/login.param";
import {result} from "@/const/request.result";
import {StringUtil} from "@/util/string.util";
import {saveToken} from "@/common/login/token.store";

const formData = reactive({
  userEmail: '',
  userEmailCode: '',
});

const data = reactive({
    showGetEmailCodeBtn: {
      show: '获取验证码',
      disable: false,
    },
    emailInput: {
      showError: undefined,
      tip: ''
    },
    emailCodeInput: {
      showError: undefined,
      tip: ''
    },
    isAgree: false
})

const showAlertInputEmail = (tip) => {
  data.emailInput.showError = 'error'
  data.emailInput.tip = tip
}

const showAlertEmailCodeError = (tip) => {
  data.emailCodeInput.showError = 'error';
  data.emailCodeInput.tip = tip
}

const onSubmit = () => {
  let alertUserEmailInput = false
  let alertUserEmailCodeInput = false
  if(StringUtil.isEmpty(formData.userEmail)){
    alertUserEmailInput = true
    showAlertInputEmail('请输入邮箱')
  }
  if(StringUtil.isEmpty(formData.userEmailCode)){
    alertUserEmailCodeInput = true
    showAlertEmailCodeError('请输入验证码')
  }
  if(alertUserEmailInput || alertUserEmailCodeInput){
    return
  }
  if(!data.isAgree){
    MessagePlugin.info('请勾选《CloudContest服务协议》和《隐私协议》')
    return
  }
  let userLoginDto = {
    userEmail: formData.userEmail,
    userEmailCode: formData.userEmailCode,
    loginOperation: loginParam.LOGIN_BY_EMAIL_CODE
  }
  request.post('/login/submit',userLoginDto).then(resp => {
    if(resp.status === 200){
      if(resp.data['resultCode'] === result.code.SUCCESS){
        MessagePlugin.success('登录成功！')
        saveToken(resp.data['data'])
      }
      if(resp.data['resultCode'] === result.code.FAIL){
        MessagePlugin.error('验证码输入有误')
      }
    }
  }).catch(() => {
    MessagePlugin.error('系统繁忙！')
  })
};

// eslint-disable-next-line no-unused-vars
let clock = null;

const decreaseSeconds = () => {
  let second = parseInt(data.showGetEmailCodeBtn.show.split("s")[0]) - 1
  if(second >= 0){
    data.showGetEmailCodeBtn.show = second + 's'
  }else{
    data.showGetEmailCodeBtn.disable = false
    data.showGetEmailCodeBtn.show = '获取验证码'
    clearInterval(clock)
  }
}

const sendLoginEmailCode = () => {
  if(StringUtil.isEmpty(formData.userEmail)){
    showAlertInputEmail('请输入邮箱')
    return
  }
  request.get('/login/email/code/' + formData.userEmail).then(resp => {
    if(resp.status === 200){
      if(resp.data['resultCode'] === result.code.SUCCESS){
        MessagePlugin.success('验证码已发送至邮箱!')
        data.showGetEmailCodeBtn.show = '60s'
        data.showGetEmailCodeBtn.disable = true
        clock = setInterval(decreaseSeconds,1000)
      }else{
        showAlertInputEmail('邮箱未注册')
      }
    }else{
      MessagePlugin.error('系统繁忙!')
    }
  }).catch(() => {
    MessagePlugin.error('系统繁忙!')
  })
}

const cleanEmailInputAlert = () => {
  if(data.emailInput.showError !== undefined || data.emailInput.tip !== ''){
    data.emailInput.showError = undefined
    data.emailInput.tip = ''
  }
}

const cleanEmailCodeInputAlert = () => {
  if(data.emailCodeInput.showError !== undefined || data.emailCodeInput.tip !== ''){
    data.emailCodeInput.showError = undefined
    data.emailCodeInput.tip = ''
  }
}

</script>
<style scoped>

</style>
