<template>

  <div style="width: 70em;margin-top: 0.5em;margin-left: 13em;border-bottom: rgba(0,0,0,0.10) 1px solid">
    <img style="width: 70em;" src="https://publicqn.saikr.com/2023/02/13/contest63e9d7e8d0a482.766950241676269952953.png?imageView2/2/w/1080" alt="" @click="show"/>
    <div :class="{titleTagBorder: true,titleSelected: contestMessage.titleSelected}">
      <span class="titleTag">竞赛信息</span>
    </div>
    <div :class="{titleTagBorder: true,titleSelected: notifyMessage.titleSelected}">
      <span class="titleTag">通知公告</span>
    </div>
    <div :class="{titleTagBorder: true,titleSelected: editContest.titleSelected}">
      <span class="titleTag">编辑竞赛</span>
    </div>
  </div>
  <div style="width: 70em;margin-top: 0.5em;margin-left: 13em;">
    <div style="width: 75%;display: inline-block">
      <h1>2023年第十三届MathorCup高校数学建模挑战赛</h1><br>
      <h2><span style="border-left: #2c9fe5 5px solid;padding-left: 0.8em">竞赛信息</span></h2><br>
      <div>
        <span>内容内容</span>
      </div>
    </div>
    <div style="width: 25%;display: inline-block;float: right">bbb</div>
  </div>

</template>

<script>

const load = (contestId,_this) => {
  _this.contestId = contestId
}

export default {
  name: "ContestDetailPage",
  data() {
    return {
      contestMessage: {
        titleSelected: true
      },
      notifyMessage: {
        titleSelected: false
      },
      editContest: {
        titleSelected: false
      },
      contestId: '',
      contestDetail: {

      }
    }
  },
  methods: {
    show: function () {
      alert('contestId: ' + this.contestId)
    }
  },
  watch: {
    'this.$route.params.contestId': {
      handler: function () {
        load(this.$route.params.contestId,this)
      }
    }
  },
  mounted() {
    load(this.$route.params.contestId,this)
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
</style>