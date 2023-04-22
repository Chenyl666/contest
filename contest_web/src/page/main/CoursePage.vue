<template>
  <SwiperFrame style="margin-top: -0.7em" :picture-list="swiperPictureLinks"/>
  <t-list style="margin-top: 2em">
    <t-list-item :key="index" v-for="(contestDetail,index) in contestDetailList" style="width: 55.5em;height: 13.5em;margin-left: 27em;border:5px solid #f9f9f9;">
      <div style="float: left">
        <h2  @click="toDetail(index)" class="the-title" style="cursor: pointer;font-weight: bold;margin-left: 2em;display: block;font-size: 18px">
          {{contestDetail.contestSubject}}
        </h2>
        <div style="display: inline;margin-left: 2.5em;color: #AAAAAA;font-size: 14px">
          <div style="display: inline-block;margin-right: 2em;font-weight: bold">主办方</div>
          <div style="display: inline-block;margin-left: 0.95em">{{contestDetail.organizeUnit}}</div>
        </div>
        <br>
        <div style="display: inline;margin-left: 2.5em;color: #AAAAAA;font-size: 14px">
          <div style="display: inline-block;margin-right: 2em;font-weight: bold">竞赛级别</div>
          <div style="display: inline-block">{{getLevelMap(contestDetail.contestLevel)}}</div>
        </div>
        <br>
        <div style="display: inline;margin-left: 2.5em;color: #AAAAAA;font-size: 14px">
          <div style="display: inline-block;margin-right: 2em;font-weight: bold">报名时间</div>
          <div style="display: inline-block">{{getTime(contestDetail.enrollStartTime)}}~{{getTime(contestDetail.enrollEndTime)}}</div>
        </div>
        <br>
        <div style="display: inline;margin-left: 2.5em;color: #AAAAAA;font-size: 14px">
          <div style="display: inline-block;margin-right: 2em;font-weight: bold">比赛时间</div>
          <div style="display: inline-block">{{getTime(contestDetail.contestStartTime)}}~{{getTime(contestDetail.contestEndTime)}}</div>
        </div>
      </div>
      <div style="float: right">
        <t-button  @click="toDetail(index)" style="margin-top: -3em;width: 12em;height: 3em;font-size: 15px;font-weight: bold">立即报名</t-button>
        <br>
        <t-link style="color: #0075FF;font-weight: bold;float: right" @click="toDetail(index)">竞赛详细></t-link>
      </div>
    </t-list-item>

  </t-list>
</template>

<script>
import SwiperFrame from "@/page/component/swiper/SwiperFrame";
import {getHotContestDetailList} from "@/api/contest";
import {getTimeStr} from "@/util/date.util";

const levelMap = {
  COLLEGE: '校级',
  CITY: '市级',
  PROVINCE: '省级',
  NATIONAL: '国家级',
  INTERNATIONAL: '国际级',
  OTHER: '其它'
}

export default {
  name: "CoursePage",
  components: {SwiperFrame},
  data() {
    return {
      swiperPictureLinks: [
        {
          index: 0,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421820543193321472'
        },
        {
          index: 1,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421821355533537280'
        },
        {
          index: 2,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421821515718201344'
        },
        {
          index: 3,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421821868081680384'
        },
        {
          index: 4,
          link: 'http://127.0.0.1:8080/filesys/download/inline/421822051947384832'
        },
      ],
      contestDetailList: []
    }
  },
  methods: {
    getTime(datetime){
      return getTimeStr(datetime)
    },
    toDetail(index){
      this.$router.push('/contest/detail/'.concat(this.contestDetailList[index].contestId))
    },
    getLevelMap(level){
      return levelMap[level]
    }
  },
  mounted() {
    getHotContestDetailList().then(resp => {
      this.contestDetailList = resp.data.data
    })
  }
}
</script>

<style scoped>
  .the-title:hover{
    color: #0075FF;
  }
  .the-title{
    color: #666666;
  }
</style>