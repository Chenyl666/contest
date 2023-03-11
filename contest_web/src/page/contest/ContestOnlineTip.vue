<template>
  <h1 style="margin-top: 2.5em;margin-left: 22em">赛前准备</h1>
  <t-steps :current="step" style="width: 60em;margin-left: 25em;margin-top: 2em">
    <t-step-item title="比赛说明"/>
    <t-step-item title="摄像头检查"/>
    <t-step-item title="等待开始"/>
  </t-steps>
  <div v-if="step === 0" style="width: 39em;font-size: 22px;margin-top: 1.8em;margin-left: 15.5em">
    本场比赛服务由CloudContest网络竞赛平台提供，请参赛者在比赛之前进行摄像头、麦克风测试，测试不成功将无法参加比赛。
    比赛摄像头和麦克风处于打开状态，并且参赛者在比赛全程中禁止退出全屏页面、禁止中途离开、禁止打开和比赛无关的软件，如有违背者将列入平台黑名单并在2年内无法在本平台参加任何的网络竞赛。
    <br><br><br>
    <div style="text-align: center;font-weight: bold;font-size: 20px">竞赛名称：{{contestSubject}}</div>
    <div style="text-align: center;font-weight: bold;font-size: 20px">开始时间：{{getTime(contestStartTime)}}</div>
    <div style="text-align: center;font-weight: bold;font-size: 20px">结束时间：{{getTime(contestEndTime)}}</div>
    <br>
    <t-button @click="toSecondStep" size="large" style="margin-left: 12em;width: 30em;margin-top: 3em">
      下一步
    </t-button>
  </div>
  <div v-if="step === 1">
    <div style="text-align: center;margin-top: 2em;font-size: 22px">
      请打开摄像头进行拍照调试，并且保证比赛过程全程开着摄像头
    </div>
    <CameraVideo :key="cameraKey" @show-next="showSecondNextBtn = true" @refresh-camera="refreshCamera" style="margin-left: 40.5em;margin-top: -2em"/>
    <t-button @click="toThirdStep" v-if="showSecondNextBtn" size="large" style="width: 14.3em;margin-left: 40.5em;margin-top: 1em" theme="success">下一步</t-button>
  </div>
  <div style="margin-top: 4em" v-if="step === 2">
    <h2 style="text-align: center">请做好准备，比赛即将开始</h2>
    <p style="text-align: center">当前时间：{{getCurrentTime}}</p>
    <p style="text-align: center">开始时间：{{contestStartTime}}</p>
    <t-button @click="startContest" :disabled="!enableToStart" size="large" style="width: 17em;margin-left: 39.5em;margin-top: 1em" theme="success">开始</t-button>
  </div>
  <ScreenLoading :loading="loading"/>
</template>

<script>
import {getContestDetailById} from "@/api/contest";
import {getTimeStrOfChina} from "@/util/date.util";
import CameraVideo from '@/page/contest/component/CameraVideo'
import router from "@/router/router";
import screenfull from "screenfull";
import {initContest} from "@/api/online";
import {result} from "@/common/request.result";
import ScreenLoading from "@/page/component/loading/ScreenLoading";
import {MessagePlugin} from "tdesign-vue-next";

const onFullscreenchange = () => {
  if(!screenfull.isFullscreen){
    console.log('退出全屏')
  }
  document.addEventListener('fullscreenchange',() => onFullscreenchange())
}

export default {
  name: "ContestOnlineTip",
  components: {ScreenLoading, CameraVideo},
  emits: ['toRegister', 'toLogin', 'toModify', 'toIndex', 'decrNotifyCount'],
  data() {
    return {
      contestStartTime: null,
      contestEndTime: null,
      contestSubject: null,
      step: 2,
      cameraKey: 1,
      showSecondNextBtn: false,
      times: new Date(),
      interval: null,
      enableToStart: false,
      loading: false
    }
  },
  methods:{
    getTime(time){
      return getTimeStrOfChina(time)
    },
    toSecondStep(){
      this.step = 1
    },
    refreshCamera() {
      this.cameraKey++
      this.showSecondNextBtn = false
    },
    toThirdStep() {
      this.step = 2
    },
    getTimes(){
      this.interval = setInterval(this.setTimesInterval, 1000);
    },
    setTimesInterval:function() {
      this.times = new Date()
    },
    startContest: function () {
      initContest(this.$route.params.contestId).then(resp => {
        this.loading = true
        if(resp.data.resultCode === result.code.SUCCESS){
          this.loading = false
          screenfull.toggle()
          document.addEventListener('fullscreenchange',() => onFullscreenchange())
          router.push('/contest/online/page/'.concat(this.$route.params.contestId))
        }else{
          this.startFailCallBack('系统繁忙！')
        }
      }).catch(() => {
        this.startFailCallBack("请检查网络！")
      })
    },
    startFailCallBack: function (info) {
      this.loading = false
      MessagePlugin.error(info)
    }
  },
  mounted() {
    this.getTimes()
    getContestDetailById(this.$route.params.contestId).then(resp => {
      this.contestStartTime = getTimeStrOfChina(resp.data.data.contestStartTime)
      this.contestEndTime = getTimeStrOfChina(resp.data.data.contestEndTime)
      this.contestSubject = resp.data.data.contestSubject
      let startTimeStamp = new Date(resp.data.data.contestStartTime).getTime()
      let endTimeStamp = new Date(resp.data.data.contestEndTime).getTime()
      let currentTimeStamp = new Date()
      if(currentTimeStamp>=startTimeStamp && currentTimeStamp<=endTimeStamp){
        this.enableToStart = true
      }
    })
  },
  computed: {
    getCurrentTime: function () {
      return getTimeStrOfChina(this.times)
    }
  },
  watch: {
    "getCurrentTime": {
      handler: function (newVal) {
        if(newVal === this.contestStartTime){
          this.enableToStart = true
        }
      }
    }
  },
  beforeRouteLeave(){
    clearInterval(this.interval)
  }
}
</script>

<style scoped>

</style>