<template>
  <div style="width: 70em;margin-top: 0.5em;margin-left: 13em;border-bottom: rgba(0,0,0,0.10) 1px solid">
    <img style="width: 70em;" :src="contestDetailMessage.contestPicture" alt=""/>
    <div @click="selectedContestMessage" :class="{titleTagBorder: true,titleSelected: contestMessage.titleSelected}">
      <span class="titleTag">竞赛信息</span>
    </div>
    <div @click="selectedNotifyMessage" :class="{titleTagBorder: true,titleSelected: notifyMessage.titleSelected}">
      <span class="titleTag">通知公告</span>
    </div>
    <div v-if="!isCreator" @click="selectedContestResult" :class="{titleTagBorder: true,titleSelected: contestResult.titleSelected}">
      <span class="titleTag">查询成绩</span>
    </div>
    <div v-if="isCreator" @click="selectedEditMessage" :class="{titleTagBorder: true,titleSelected: editContest.titleSelected}">
      <span class="titleTag">编辑竞赛</span>
    </div>
    <div v-if="isCreator" @click="selectedContestManagement" :class="{titleTagBorder: true,titleSelected: contestManagement.titleSelected}">
      <span class="titleTag">竞赛管理</span>
    </div>
  </div>
  <div v-if="contestMessage.titleSelected" style="width: 70em;margin-top: 0.5em;margin-left: 13em;margin-bottom: 15em">
    <div style="width: 70%;display: inline-block">
      <h1>{{contestDetailMessage.contestSubject}}</h1><br>
      <h3><span style="border-left: #2c9fe5 5px solid;padding-left: 0.8em">竞赛信息</span></h3><br>
      <div style="margin-top: -2em" v-html="contestDetailMessage.contestDescription"></div>
    </div>
    <div style="width: 25%;display: inline-block;float: right;margin-bottom: 15em">
      <div>
        <p class="boldFront">发布者</p>
        <t-space style="cursor: pointer" :size="100">
          <t-avatar shape="circle" :image="contestCreator.userPic" :hide-on-load-failed="false" />
        </t-space>
        <div style="display: inline-block;margin-left: 0.7em">{{this.contestCreator.userName}}</div>
      </div>
      <div>
        <p class="boldFront">竞赛类型</p>
        <p class="rightItem">{{this.contestDetailMessage.typeName}}</p>
      </div>
      <div>
        <p class="boldFront">报名费用</p>
        <p class="rightItem">{{this.contestDetailMessage.contestPrice}}￥</p>
      </div>
      <div>
        <p class="boldFront">报名时间</p>
        <p class="rightItem">开始：{{this.getEnrollStartTime}}</p>
        <p class="rightItem">截止：{{this.getEnrollEndTime}}</p>
      </div>
      <div>
        <p class="boldFront">比赛时间</p>
        <p class="rightItem">开始：{{this.getContestStartTime}}</p>
        <p class="rightItem">截止：{{this.getContestEndTime}}</p>
      </div>
      <div>
        <p class="boldFront">主办方</p>
        <p class="rightItem">账号：{{this.contestDetailMessage.createdBy}}</p>
      </div>
      <t-form-item v-if="getUserType === 'PARTICIPANT'" style="margin-top: 2em">
        <t-button :disabled="hasEnroll" @click="enrollContest" size="large" theme="primary" style="width: 85%">{{hasEnroll?'已报名':'报名参赛'}}</t-button>
      </t-form-item>
    </div>
  </div>
  <div v-if="notifyMessage.titleSelected" style="width: 70em;margin-top: 0.5em;margin-left: 13em;margin-bottom: 15em">
    <ContestNotify/>
  </div>
  <div v-if="contestResult.titleSelected" style="width: 70em;margin-top: 0.5em;margin-left: 13em;margin-bottom: 15em">
    <ContestResultOfUser/>
  </div>
  <div v-if="editContest.titleSelected" style="width: 70em;margin-top: 0.5em;margin-left: 13em;margin-bottom: 15em">
    <t-form ref="form" :data="formData" :colon="true">
      <t-form-item :label="`竞赛封面`">
        <UploadImage @on-success="onSuccess"/>
        <t-input style="display: none"/>
      </t-form-item>
      <t-form-item label="当前封面">
        <img :src="formData.contestPicture" alt="" style="width: 72.7em">
      </t-form-item>

      <t-form-item label="竞赛名称" name="contestSubject">
        <t-input v-model="formData.contestSubject" placeholder="请输入竞赛名称"></t-input>
      </t-form-item>
      <t-form-item label="主办方" name="organizeUnit">
        <t-input v-model="formData.createdBy" placeholder="请输入竞赛举办方，如有协办方请在竞赛详细中说明"></t-input>
      </t-form-item>
      <t-form-item label="竞赛类别" name="contestTypeId">
        <t-select v-model="formData.contestType" placeholder="请输入竞赛类别">
          <t-option v-for="contestType in meta.contestTypeList"
                    :key="contestType.typeId"
                    :label="contestType.typeName"
                    :value="contestType.typeId" />
        </t-select>
      </t-form-item>

      <t-form-item label="竞赛级别" name="contestLevel">
        <t-radio-group v-model="formData.contestLevel">
          <t-radio :value="CONTEST.LEVEL.COLLEGE">校级</t-radio>
          <t-radio :value="CONTEST.LEVEL.CITY">市级</t-radio>
          <t-radio :value="CONTEST.LEVEL.PROVINCE">省级</t-radio>
          <t-radio :value="CONTEST.LEVEL.NATIONAL">国家</t-radio>
          <t-radio :value="CONTEST.LEVEL.INTERNATIONAL">全球</t-radio>
          <t-radio :value="CONTEST.LEVEL.OTHER">其它</t-radio>
        </t-radio-group>
      </t-form-item>
      <t-form-item name="enableGroup" label="是否可以组队">
        <t-radio-group v-model="formData.groupingContest">
          <t-radio :value="true">是</t-radio>
          <t-radio :value="false">否</t-radio>
        </t-radio-group>
      </t-form-item>

      <t-form-item v-if="formData.groupingContest" name="groupLimit" label="队伍人数">
        <t-range-input v-model="formData.groupingLimit" placeholder="请输入队伍人数区间"/>
      </t-form-item>

      <t-form-item name="contestPrice" label="竞赛费用">
        <t-input v-model="formData.contestPrice" type="number" placeholder="提现时将扣除5%的使用费用"/>
      </t-form-item>
      <t-form-item name="description" label="竞赛详细">
        <MyEditor
            :content="contestDetailMessage.contestDescription"
            @content-change="(value) => formData.contestDescription = value" />
      </t-form-item>
      <t-form-item style="margin-bottom: 5em;margin-top: 10em">
        <t-button size="large" block theme="primary" type="submit" @click="onSubmit">保存</t-button>
      </t-form-item>
    </t-form>
  </div>
  <div v-if="contestManagement.titleSelected" style="width: 70em;margin-top: 0.5em;margin-left: 13em;margin-bottom: 15em">
    <ContestManagement/>
  </div>
</template>

<script>

import {
  getContestCreatorByContestId, getContestDetailById,
  getContestDetailMessageById,
  getContestTypeList, getUserEnrollSituation, updateContestDetail, updateContestStatus
} from "@/api/contest";
import {result} from "@/common/request.result";
import {toRef} from "vue";
import {mapGetters} from "vuex";
import {gettersName} from "@/store/getters/getters.name";
import {getTimeStrOfChina} from "@/util/date.util";
import {DEV_CONFIG} from "@/config/dev.config";
import UploadImage from "@/page/component/upload/UploadImage"
import MyEditor from '@/page/component/editor/MyEditor'
import {CONTEST} from "@/common/contest";
import {MessagePlugin} from "tdesign-vue-next";
import router from "@/router/router";
import {getUserDetail} from "@/api/user";
import {createEnrollPaymentOrder} from "@/api/payment";
import {store} from "@/store";
import ContestManagement from "@/page/contest/component/ContestManagement";
import ContestNotify from "@/page/contest/component/ContestNotify";
import ContestResultOfUser from "@/page/contest/component/ContestResultOfUser";
import {globalConfig} from "@/global.config";

const load = (contestId,_this) => {
  _this.contestId = contestId
}

export default {
  name: "ContestDetail",
  components: {ContestResultOfUser, ContestNotify, ContestManagement, UploadImage,MyEditor},
  data() {
    return {
      contestMessage: {
        titleSelected: true
      },
      notifyMessage: {
        titleSelected: false
      },
      contestResult: {
        titleSelected: false
      },
      editContest: {
        titleSelected: false
      },
      contestManagement: {
        titleSelected: false
      },
      contestId: '',
      contestDetailMessage: {
      },
      contestCreator: {
      },
      formData: {
        contestPicture: null,
        contestSubject: null,
        createdBy: null,
        contestType: null,
        contestLevel: null,
        groupingContest: null,
        groupingLimit: [],
        contestPrice: null,
        contestDescription: null
      },
      CONTEST: CONTEST,
      meta: {
        contestTypeList: [{typeId: null,typeName: null}]
      },
      hasEnroll: false
    }
  },
  methods: {
    selectedContestMessage: async function () {
      this.clearTitleSelected()
      this.contestMessage.titleSelected = true
      await getContestDetailMessageById(this.$route.params.contestId).then(resp => {
        if (resp.data.resultCode === result.code.SUCCESS) {
          this.contestDetailMessage = toRef(resp.data, 'data')
        }
      })
      await getContestCreatorByContestId(this.contestDetailMessage.contestId).then(resp => {
        if (resp.data.resultCode === result.code.SUCCESS) {
          this.contestCreator = toRef(resp.data, 'data')
          this.contestCreator.userPic = DEV_CONFIG.BASE_URL.concat('/user/pic/get/').concat(this.contestCreator.userId)
        }
      })
    },
    selectedNotifyMessage: function () {
      this.clearTitleSelected()
      this.notifyMessage.titleSelected = true
    },
    clearTitleSelected: function () {
      this.contestManagement.titleSelected = false
      this.contestMessage.titleSelected = false
      this.notifyMessage.titleSelected = false
      this.editContest.titleSelected = false
      this.contestResult.titleSelected = false
    },
    selectedContestManagement: function () {
      this.clearTitleSelected()
      this.contestManagement.titleSelected = true
    },
    selectedContestResult: function () {
      this.clearTitleSelected()
      this.contestResult.titleSelected = true
    },
    selectedEditMessage: function () {
      this.clearTitleSelected()
      this.editContest.titleSelected = true
      getContestDetailById(this.$route.params.contestId).then(resp => {
        console.log(resp.data)
        if(resp.data['resultCode'] === result.code.SUCCESS){
          this.formData.createdBy = resp.data.data.createdBy
          this.formData.contestLevel = resp.data.data.contestLevel
          this.formData.contestPrice = resp.data.data.contestPrice
          this.formData.contestSubject = resp.data.data.contestSubject
          this.formData.contestPicture = resp.data.data.contestPicture
          this.formData.contestType = resp.data.data.contestTypeId
          this.formData.contestDescription = resp.data.data.contestDescription
          this.formData.groupingContest = resp.data.data.groupingContest
          this.formData.groupingLimit[0] = resp.data.data.groupingMinNum
          this.formData.groupingLimit[1] = resp.data.data.groupingMaxNum
        }
      })
    },
    onSuccess: function (url) {
      this.formData.contestPicture = url
    },
    onSubmit: function () {
      // alert(this.formData.contestPicture)
      let data = {
        contestId: this.$route.params.contestId,
        contestPicture: this.formData.contestPicture,
        contestSubject: this.formData.contestSubject,
        createdBy: this.formData.createdBy,
        contestTypeId: this.formData.contestType,
        contestLevel: this.formData.contestLevel,
        groupingContest: this.formData.groupingContest,
        groupingMinNum: this.formData.groupingLimit[0],
        groupingMaxNum: this.formData.groupingLimit[1],
        contestPrice: this.formData.contestPrice,
        contestDescription: this.formData.contestDescription
      }
      updateContestDetail(data).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
          // router.push('/contest/detail/'.concat(contestId))
          this.selectedContestMessage()
        }else{
          MessagePlugin.error("系统繁忙！")
        }
      }).catch(() => {
        MessagePlugin.error("系统繁忙！")
      })
    },
    enrollContest: async function () {
      if(confirm("确定要报名吗？")){
        let pass = false
        await getUserDetail().then(resp => {
          if(resp.data.resultCode === result.code.SUCCESS){
            if(resp.data.data.identify === null || resp.data.data.identify === ''){
              MessagePlugin.info('请先进行实名认证！')
              pass = false
              setTimeout(() => {
                router.push('/usr/detail')
              },3000)
            }else{
              pass = true
            }
          }
        })
        if(pass){
          await createEnrollPaymentOrder(this.$route.params.contestId).then(resp => {
            if(resp.data.resultCode === result.code.SUCCESS){
              window.open(resp.data.data)
            }
            if(resp.data.resultCode === result.code.CONTINUE){
              window.open(globalConfig.requestConfig.baseURL.concat("/contest/pay/page/success"))
            }
            if(resp.data.resultCode === result.code.FAIL){
              MessagePlugin.error('不在报名时间范围内！')
            }
          }).catch(() => {
            MessagePlugin.error('系统繁忙!')
          })
        }
      }
    }
  },
  computed: {
    ...mapGetters([gettersName.GET_USER_PIC,gettersName.GET_USER_NAME,gettersName.GET_USER_TYPE,gettersName.GET_USER_ID]),
    getEnrollStartTime: function(){
      return getTimeStrOfChina(this.contestDetailMessage.enrollStartTime)
    },
    getEnrollEndTime: function () {
      return getTimeStrOfChina(this.contestDetailMessage.enrollEndTime)
    },
    getContestStartTime: function () {
      return getTimeStrOfChina(this.contestDetailMessage.contestStartTime)
    },
    getContestEndTime: function () {
      return getTimeStrOfChina(this.contestDetailMessage.contestEndTime)
    },
    isCreator: function () {
      return store.getters.getUserId === this.contestDetailMessage.createdBy
    }
  },
  watch: {
    'this.$route.params.contestId': {
      handler: function () {
        load(this.$route.params.contestId,this)
      }
    }
  },
  async mounted() {
    await updateContestStatus(this.$route.params.contestId)
    await getContestTypeList().then(resp => {
      this.meta.contestTypeList = toRef(resp.data,'data')
    })
    await getContestDetailMessageById(this.$route.params.contestId).then(resp => {
      if (resp.data.resultCode === result.code.SUCCESS) {
        this.contestDetailMessage = toRef(resp.data, 'data')
        console.log(this.contestDetailMessage)
        console.log(store.getters.getUserId)
        if(resp.data.data === null){
          router.push('/404')
        }
      }
    })
    await getContestCreatorByContestId(this.contestDetailMessage.contestId).then(resp => {
      if (resp.data.resultCode === result.code.SUCCESS) {
        this.contestCreator = toRef(resp.data, 'data')
        this.contestCreator.userPic = DEV_CONFIG.BASE_URL.concat('/user/pic/get/').concat(this.contestCreator.userId)
      }
    })
    await getUserEnrollSituation(this.$route.params.contestId).then(resp => {
      if(resp.data.resultCode === result.code.SUCCESS){
        this.hasEnroll = resp.data.data.pay
      }
    })
  }
}

</script>

<style scoped>
.titleTag{
  font-size: 18px;
  font-weight: bold
}
.titleTagBorder{
  display: inline-block;
  padding: 2em 2em;
  cursor: pointer;
}
.titleTagBorder:hover{
  color: #2c9fe5;
  background-color: #f6f5f7;
}
.titleSelected{
  border-bottom: #2c9fe5 3px solid;
}
.boldFront{
  margin-top: 2em;
  color: #666;
  line-height: 16px;
  font-weight: 600;
  padding-left: 6px;
  border-left: 4px solid #2c9fe5;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.rightItem{
  margin-left: 0.5em
}
</style>