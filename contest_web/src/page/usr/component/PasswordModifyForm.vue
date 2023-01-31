<template>
  <t-link style="margin-top: 4em;margin-left: 2.5em">
    <span style="font-size: 1.5em;font-weight: bold">忘记密码</span>
  </t-link>
  <t-link @click="$emit('to-login')" style="margin-left: 10.6em;">{{ data.toLoginBtn }}</t-link>
  <div style="width: 320px;margin-left: 2em;margin-top: 2em">
    <t-form ref="form" :data="formData" :colon="true" :label-width="0" @submit="onSubmit">
      <t-form-item name="account">
        <t-input
            :status="data.emailInputAlert.status"
            :tips="data.emailInputAlert.tips"
            size="large"
            v-model="formData.userEmail"
            placeholder="请输入邮箱"
            @change="clearEmailAlert">
          <template #prefix-icon>
            <mail-icon/>
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="account">
        <t-input
            :status="data.emailCodeInputAlert.status"
            :tips="data.emailCodeInputAlert.tips"
            size="large"
            style="margin-top: 0.5em"
            v-model="formData.emailCode"
            placeholder="请输入验证码"
            @change="clearEmailCodeAlert">
          <template #prefix-icon>
            <check-circle-icon/>
          </template>
        </t-input>
        <t-button
            :disabled="data.sendEmailCodeBtn.disabled"
            @click="sendEmailCode"
            style="width: 8em;margin-top: 0.5em;height: 2.8em;">
          {{data.sendEmailCodeBtn.show}}</t-button>
      </t-form-item>

      <t-form-item name="password">
        <t-input
            :status="data.userPwdInputAlert.status"
            :tips="data.userPwdInputAlert.tips"
            size="large"
            style="margin-top: 0.5em"
            v-model="formData.userPwd"
            type="password"
            placeholder="请输入新密码"
            @change="checkingPassword">
          <template #prefix-icon>
            <lock-on-icon/>
          </template>
        </t-input>
      </t-form-item>

      <t-form-item>
        <t-button style="" size="large" theme="primary" type="submit" block>确定修改</t-button>
      </t-form-item>
    </t-form>
  </div>
</template>
<script setup>
import {reactive} from 'vue';
import {MessagePlugin} from 'tdesign-vue-next';
import {LockOnIcon, MailIcon, CheckCircleIcon} from 'tdesign-icons-vue-next';
import {StringUtil} from "@/util/string.util";
import {saveModifyDetail, sendModifyEmailCode} from "@/api/modify";
import {passwordChecking} from "@/page/usr/component/usr.checking";
import {defineEmits} from "vue";

const emits = defineEmits(['to-success'])

const formData = reactive({
  userEmail: '',
  emailCode: '',
  userPwd: ''
});

const data = reactive({
  toLoginBtn: '<返回登录',
  sendEmailCodeBtn: {
    show: '发送验证码',
    disabled: false
  },
  emailInputAlert: {
    tips: '',
    status: undefined
  },
  emailCodeInputAlert: {
    tips: '',
    status: undefined
  },
  userPwdInputAlert: {
    tips: '',
    status: undefined
  }
})

const showEmailAlert = (msg) => {
  data.emailInputAlert.tips = msg
  data.emailInputAlert.status = 'error'
}

const clearEmailAlert = () => {
  data.emailInputAlert.tips = ''
  data.emailInputAlert.status = undefined
}

const showEmailCodeAlert = (msg) => {
  data.emailCodeInputAlert.tips = msg
  data.emailCodeInputAlert.status = 'error'
}

const clearEmailCodeAlert = () => {
  data.emailCodeInputAlert.tips = ''
  data.emailCodeInputAlert.status = undefined
}

const checkingPassword = (password) => {
  passwordChecking(password).then(r => {
    data.userPwdInputAlert.tips = r.msg
    data.userPwdInputAlert.status = r.status
  })
}

let clock = null

const sendEmailCode = async () => {
  if (StringUtil.isEmpty(formData.userEmail)) {
    showEmailAlert('邮箱不能为空！')
  }
  let result = null
  await sendModifyEmailCode(formData.userEmail).then(r => {
    result = r
  })
  if(result.isSuccess){
    data.sendEmailCodeBtn.disabled = true
    data.sendEmailCodeBtn.show = '60s'
    clock = setInterval(() => {
      let seconds = parseInt(data.sendEmailCodeBtn.show.split('s')[0])
      seconds = seconds - 1
      if(seconds >= 0){
        data.sendEmailCodeBtn.show = seconds + 's'
      }else{
        clearInterval(clock)
        data.sendEmailCodeBtn.disabled = false
        data.sendEmailCodeBtn.show = '发送验证码'
      }
    },1000)
    await MessagePlugin.success('验证码已发送')
  }else{
    await showEmailAlert(result.msg)
  }
}

const onSubmit = async () => {
  if (StringUtil.isEmpty(formData.userEmail)) {
    showEmailAlert('邮箱不能为空')
  }
  if (StringUtil.isEmpty(formData.emailCode)) {
    showEmailCodeAlert('验证码不能为空')
  }
  checkingPassword(formData.userPwd)
  let checkingResult = (
      data.userPwdInputAlert.status !== 'error'
      && data.emailInputAlert.status !== 'error'
      && data.emailCodeInputAlert.status !== 'error'
  )
  if (!checkingResult) {
    return
  }
  let modifyPwdDto = {
    userEmail: formData.userEmail,
    userPwd: formData.userPwd,
    emailCode: formData.emailCode
  }
  await saveModifyDetail(modifyPwdDto).then(r => {
    if(r.isSuccess){
      emits('to-success','修改成功')
    }else{
      MessagePlugin.error(r.msg)
    }
  })
};
</script>
