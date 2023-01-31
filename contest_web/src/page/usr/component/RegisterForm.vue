<template>
  <t-link style="margin-top: 3.4em;margin-left: 2.5em">
    <span @click="updateUserType" style="font-size: 1.5em;font-weight: bold">成为{{
        registerUserType[formData.userType].introduction }}</span>
  </t-link>
  <t-link @click="toLogin" style="margin-left: 10.6em">{{data.toLogin}}</t-link>
  <div style="width: 320px;margin-left: 2em;margin-top: 2em">
    <t-form ref="form" :data="formData" :colon="true" :label-width="0" @submit="onSubmit">
      <t-form-item name="account">
        <t-input v-model="formData.userEmail"
                 :placeholder="`请输入`+ registerUserType[formData.userType].introduction +`邮箱`"
                 :status="data.emailInputAlert.status"
                 :tips="data.emailInputAlert.msg"
                 @change="checkingEmail">
          <template #prefix-icon>
            <mail-icon />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="account">
        <t-input
            :status="data.emailCodeInputAlert.status"
            :tips="data.emailCodeInputAlert.msg"
            v-model="formData.emailCode"
            placeholder="请输入邮箱验证码"
            @change="checkingEmailCode">
          <template #prefix-icon>
            <check-circle-icon/>
          </template>
        </t-input>
        <t-button style="width: 8em;" :disabled="data.sendEmailCodeBtn.disabled" @click="sendEmailCode">{{data.sendEmailCodeBtn.show}}</t-button>
      </t-form-item>

      <t-form-item name="account">
        <t-input v-model="formData.userId"
                 :placeholder="`请输入`+ registerUserType[formData.userType].introduction +`用户名`"
                 :status="data.userIdInputAlert.status"
                 :tips="data.userIdInputAlert.msg"
                 @change="checkingUserId">
          <template #prefix-icon>
             <desktop-icon />
          </template>
        </t-input>
      </t-form-item>

      <t-form-item name="password">
        <t-input
            :status="data.userPwdInputAlert.status"
            :tips="data.userPwdInputAlert.msg"
            v-model="formData.userPwd"
            type="password"
            placeholder="请输入密码"
            @change="checkingPassword">
          <template #prefix-icon>
            <lock-on-icon />
          </template>
        </t-input>
      </t-form-item>

      <div style="display: flex;margin-top: 2em">
        <input v-model="data.isAgree" type="checkbox" style="display: inline;border-color: #B5C0CA"/>
        <div style="font-size: 0.7em;display: inline;margin-top: 0.05em"><span style="color: #B5C0CA">本人已同意</span>《CloudContest服务协议》《隐私协议》</div>
      </div>

      <t-form-item>
        <t-button theme="primary" type="submit" block>注册</t-button>
      </t-form-item>
    </t-form>
  </div>
</template>
<script setup>
import { reactive } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import {CheckCircleIcon, DesktopIcon, LockOnIcon, MailIcon} from 'tdesign-icons-vue-next';
import {defineEmits} from "vue";
import {
  emailChecking,
  emailCodeChecking,
  passwordChecking,
  userIdChecking
} from "@/page/usr/component/usr.checking";
import {saveUser, sendEmailCodeToUserEmail} from "@/api/register";

const userType = {
  participant: {
    enum: 'PARTICIPANT',
    introduction: '参赛者'
  },
  organizer: {
    enum: 'ORGANIZER',
    introduction: '举办方'
  }
}

const registerUserType = [userType.participant,userType.organizer]

const defaultData = {
  userEmail: '',
  emailCode: '',
  userId: '',
  userPwd: '',
  userType: 0
}

let formData = reactive(defaultData);

let emits = defineEmits(['to-login','to-success']);

let clock = null

const toLogin = () => {
  formData = reactive(defaultData)
  emits('to-login')
}

const data = reactive({
  toLogin: '<返回登录',
  isAgree: false,
  sendEmailCodeBtn : {
    show: '发送验证码',
    disabled: false
  },
  emailInputAlert: {
    status: undefined,
    msg: ''
  },
  emailCodeInputAlert: {
    status: undefined,
    msg: ''
  },
  userIdInputAlert: {
    status: undefined,
    msg: ''
  },
  userPwdInputAlert: {
    status: undefined,
    msg: ''
  }
})

const updateUserType = () => {
  formData.userType++
  if(formData.userType === registerUserType.length){
    formData.userType = 0
  }
}

const sendEmailCode = async () => {
  await checkingEmail()
  if (data.emailInputAlert.status !== 'error') {
    let sendResult = false
    await sendEmailCodeToUserEmail(formData.userEmail).then(r => {
      sendResult = r
    })
    if(sendResult){
      await MessagePlugin.success('验证码发送成功！')
    }else{
      await MessagePlugin.error('系统繁忙！')
    }
    data.sendEmailCodeBtn.show = '60s'
    data.sendEmailCodeBtn.disabled = true
    clock = setInterval(() => {
      let second = parseInt(data.sendEmailCodeBtn.show.split('s')[0]) - 1
      if (second >= 0) {
        data.sendEmailCodeBtn.show = second + 's'
      } else {
        data.sendEmailCodeBtn.show = '发送验证码'
        data.sendEmailCodeBtn.disabled = false
        clearInterval(clock)
      }
    }, 1000)
  }
}

const checkingEmail = async () => {
  await emailChecking(formData.userEmail).then(r => {
    data.emailInputAlert.status = r.status
    data.emailInputAlert.msg = r.msg
  })
}

const checkingUserId = async () => {
  await userIdChecking(formData.userId).then(r => {
    data.userIdInputAlert.status = r.status
    data.userIdInputAlert.msg = r.msg
  })
}

const checkingPassword = async () => {
  await passwordChecking(formData.userPwd).then(r => {
    data.userPwdInputAlert.status = r.status
    data.userPwdInputAlert.msg = r.msg
  })
}

const checkingEmailCode = async () => {
  await emailCodeChecking(formData.emailCode).then(r => {
    data.emailCodeInputAlert.status = r.status
    data.emailCodeInputAlert.msg = r.msg
  })
}

const onSubmit = async () => {
  await checkingEmail()
  await checkingEmailCode()
  await checkingUserId()
  await checkingPassword()
  let checkingResult = (
      data.emailInputAlert.status !== 'error'
      && data.emailCodeInputAlert.status !== 'error'
      && data.userIdInputAlert.status !== 'error'
      && data.userPwdInputAlert.status !== 'error')
  if(checkingResult){
    if(!data.isAgree){
      await MessagePlugin.info('请勾选《CloudContest服务协议》和《隐私协议》')
      return
    }
    let userRegisterDto = {
      userId: formData.userId,
      userPwd: formData.userPwd,
      userType: registerUserType[formData.userType].enum,
      userEmail: formData.userEmail,
      emailCode: formData.emailCode
    }
    await saveUser(userRegisterDto).then(r => {
      if(r.result){
        emits('to-success',"注册成功")
      }else{
        MessagePlugin.error(r.msg)
      }
    })
  }
};
</script>
