<template>
  <div class="board">
    <h3 style="text-align: center">个人信息</h3>
    <t-form>
      <t-form-item label="修改头像" style="margin-top: 2em">
        <UploadUserImage style="margin-top: 0.5em" @on-success="imageUploadSuccess"/>
      </t-form-item>
      <t-form-item style="" label="用户昵称">
        <t-input v-model="user.userName" placeholder="请输入用户昵称"/>
      </t-form-item>
      <t-form-item label="所属单位">
        <t-input v-model="user.unit" placeholder="请输入所属单位"/>
      </t-form-item>
      <t-form-item label="邮箱">
        <t-input v-model="user.userEmail" placeholder="请输入邮箱"/>
      </t-form-item>
      <t-form-item label="手机号码">
        <t-input v-model="user.userPhone" placeholder="请输入手机号码"/>
      </t-form-item>
      <t-form-item label="身份证号码">
        <t-input v-model="user.identify" placeholder="您必须实名才可以参加或举办比赛！"/>
      </t-form-item>
        <t-button @click="save" style="width: 100%;margin-top: 2em">保存</t-button>
    </t-form>
  </div>

</template>

<script>
import UploadUserImage from "@/page/component/upload/UploadUserImage";
import {getUserDetail, saveUserDetail} from "@/api/user";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";
export default {
  name: "UserDetail",
  components: {UploadUserImage},
  data() {
    return {
      user: {
        userId: null,
        userPic: null,
        userName: null,
        unit: null,
        userEmail: null,
        userPhone: null,
        identify: null
      }
    }
  },
  methods: {
    imageUploadSuccess: function (url) {
      let urlBlocks = url.split('/');
      this.user.userPic = urlBlocks[urlBlocks.length-1]
    },
    save: function () {
      saveUserDetail(this.user).then(resp => {
        if(resp.data.resultCode === result.code.SUCCESS){
          MessagePlugin.success('保存成功！')
        }else{
          MessagePlugin.error('系统繁忙！')
        }
      }).catch(() => {
        MessagePlugin.error('系统繁忙！')
      })
    }
  },
  mounted() {
    getUserDetail().then(resp => {
      if(resp.data.resultCode === result.code.SUCCESS){
        let userDetail = resp.data.data
        this.user.userId = userDetail.userId
        this.user.userName = userDetail.userName
        this.user.userEmail = userDetail.userEmail
        this.user.userPhone = userDetail.userPhone
        this.user.identify = userDetail.identify
        this.user.unit = userDetail.unit
      }
    })
  }
}
</script>

<style scoped>
  .board{
    width: 40em;
    margin-left:27em;
    margin-top: 1em;
    box-shadow: 0 5px 10px 0 rgb(0 0 0 / 10%);
    border-radius: 6px;
    background-color: white;
    padding: 1em 3em 4em;
  }
</style>