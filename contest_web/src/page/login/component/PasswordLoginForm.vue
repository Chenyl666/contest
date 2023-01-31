<template>
  <div style="width: 350px">
    <t-form ref="form" :data="formData" :colon="true" :label-width="0" @submit="onSubmit">
      <t-form-item name="account">
        <t-input size="large" @change="cleanAlertUsernameInputTip" :status="data.usernameInput.showError" :tips="data.usernameInput.tip" v-model="formData.userId" clearable placeholder="请输入用户名或邮箱">
          <template #prefix-icon>
            <desktop-icon />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password">
        <t-input size="large" @change="cleanAlertPasswordInputTip" :status="data.passwordInput.showError" :tips="data.passwordInput.tip" v-model="formData.userPwd" type="password" clearable placeholder="请输入密码">
          <template #prefix-icon>
            <lock-on-icon />
          </template>
        </t-input>
      </t-form-item>

      <div style="display: flex;margin-top: 2em">
        <input v-model="data.isAgree" type="checkbox" style="display: inline;border-color: #B5C0CA"/>
        <div style="font-size: 0.7em;display: inline;margin-top: 0.05em"><span style="color: #B5C0CA">登录即代表同意</span>《CloudContest服务协议》《隐私协议》</div>
      </div>


      <t-form-item style="margin-top: 0">
        <t-button theme="primary" type="submit" block style="height: 3em">登录</t-button>
      </t-form-item>
      <t-button @click="$emit('to-register')" variant="text" style="margin-left: 0;padding: 0">快速注册</t-button>
      <t-button @click="$emit('to-modify')" variant="text" style="padding: 0;margin-left: 15.7em">忘记密码？</t-button>
    </t-form>
  </div>
</template>
<script setup>
import { reactive } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import { DesktopIcon, LockOnIcon } from 'tdesign-icons-vue-next';
import {StringUtil} from "@/util/string.util";
import {result} from "@/const/request.result";
import {loginParam} from "@/const/login.param";
import {request} from "@/util/request";
import {saveToken} from "@/common/login/token.store";

const formData = reactive({
  userId: '',
  userPwd: '',
});

const data = reactive({
  usernameInput: {
    showError: undefined,
    tip: ''
  },
  passwordInput: {
    showError: undefined,
    tip: ''
  },
  isAgree: false
})

const showAlertUsernameInputTip = (message) => {
  data.usernameInput.showError = 'error'
  data.usernameInput.tip = message
}

const showAlertPasswordInputTip = (message) => {
  data.passwordInput.showError = 'error'
  data.passwordInput.tip = message
}

const cleanAlertUsernameInputTip = () => {
  data.usernameInput.showError = undefined
  data.usernameInput.tip = ''
}

const cleanAlertPasswordInputTip = () => {
  data.passwordInput.showError = undefined
  data.passwordInput.tip = ''
}

const onSubmit = () => {
  let alertUsernameInput = false
  let alertPasswordInput = false
  if(StringUtil.isEmpty(formData.userId)){
    alertUsernameInput = true
    showAlertUsernameInputTip('请输入用户名或邮箱')
  }
  if(StringUtil.isEmpty(formData.userPwd)){
    alertPasswordInput = true
    showAlertPasswordInputTip('请输入密码')
  }
  if(alertUsernameInput || alertPasswordInput){
    return
  }
  if(!data.isAgree){
    MessagePlugin.info('请勾选《CloudContest服务协议》和《隐私协议》')
    return
  }
  let loginOperation = (formData.userId.indexOf('@') === -1)?
      loginParam.LOGIN_BY_USER_PASSWORD : loginParam.LOGIN_BY_EMAIL_PASSWORD
  let userLoginDto = {
    userId: formData.userId,
    userPwd: formData.userPwd,
    loginOperation
  }
  request.post('/login/submit',userLoginDto).then(resp => {
    if(resp.data['resultCode'] === result.code.SUCCESS){
      MessagePlugin.success('登录成功！')
      saveToken(resp.data['data'])
    }
    if(resp.data['resultCode'] === result.code.FAIL){
      MessagePlugin.error('密码错误！')
    }
  }).catch(() => {
    MessagePlugin.error('系统繁忙！')
  })
};
</script>
