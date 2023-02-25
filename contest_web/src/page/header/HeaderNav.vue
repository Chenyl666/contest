<template>
  <t-header>
    <div style="background-color: white;width: 100%;height: 4em;margin-bottom: -0.5em;border-bottom: 2px solid #f6f5f7">
      <img src="../../assets/logo.png" style="width:13.44em;height: 4em;margin-left: 10em;" alt="LOGO">
      <div style="margin-left: 28em;">
        <ul style="display: flex;list-style-type:none;margin-top: -3em">
          <li class="dir-item">
            <t-badge @click="changePage('mainPage')" @mouseenter="menu.tag.mainPage.select = true" @mouseleave="menu.tag.mainPage.select = false" :count="menu.tag.mainPage.hot" class="txt">
              <span :class="{selected: isMainPage, selecting: menu.tag.mainPage.select}">首页</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge @click="changePage('contestPage')" @mouseenter="menu.tag.contestPage.select = true" @mouseleave="menu.tag.contestPage.select = false" :count="menu.tag.contestPage.hot" class="txt">
              <span :class="{selected: isContestPage, selecting: menu.tag.contestPage.select}">竞赛</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge @click="changePage('questionRepo')" @mouseenter="menu.tag.questionRepo.select = true" @mouseleave="menu.tag.questionRepo.select = false" :count="menu.tag.questionRepo.hot" class="txt">
              <span :class="{selected: isQuestionPage, selecting: menu.tag.questionRepo.select}">题库</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge @click="changePage('coursePage')" @mouseenter="menu.tag.coursePage.select = true" @mouseleave="menu.tag.coursePage.select = false" :count="menu.tag.coursePage.hot" class="txt">
              <span :class="{selected: isCoursePage, selecting: menu.tag.coursePage.select}">课程</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge @click="changePage('bookPage')" @mouseenter="menu.tag.bookPage.select = true" @mouseleave="menu.tag.bookPage.select = false" :count="menu.tag.bookPage.hot" class="txt">
              <span :class="{selected: isBookPage, selecting: menu.tag.bookPage.select}">图书</span>
            </t-badge>
          </li>
        </ul>
      </div>

      <div style="margin-left: 73.5em;margin-top: -2.7em;display: flex">
        <div v-if="token===null || token === `none`">
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
        <div v-if="token!==null && token!== `none`">
          <t-popup content="举办比赛"
                   trigger="hover"
                   placement="bottom"
                   v-if="user.userType === 'ORGANIZER'">
            <FlagIcon
                @click="toCreatedContest"
                @mouseenter="btn.organized = '#2c9fe5'"
                @mouseleave="btn.organized = '#B5C0CA'"
                :style="{marginLeft: '0.7em',color: btn.organized,cursor: 'pointer'}"
                size="1.95em"/>
          </t-popup>

          <t-popup content="参加比赛"
                   trigger="hover"
                   placement="bottom"
                   v-if="user.userType === 'PARTICIPANT'">
            <AddRectangleIcon
                @mouseenter="btn.notification = '#2c9fe5'"
                @mouseleave="btn.notification = '#B5C0CA'"
                :style="{marginLeft: '0.7em',color: btn.notification,cursor: 'pointer'}"
                size="1.95em"/>
          </t-popup>

          <t-popup content="消息通知"
                   trigger="hover"
                   placement="bottom">
            <NotificationFilledIcon
                @mouseenter="btn.takePartIn = '#2c9fe5'"
                @mouseleave="btn.takePartIn = '#B5C0CA'"
                :style="{marginLeft: '0.4em',color: btn.takePartIn,cursor: 'pointer'}"
                size="1.95em"/>
          </t-popup>

          <t-dropdown :options="user.menu" @click="userMenuHandler">
            <template #1>
              <ArrowRightIcon/>
            </template>
            <template #2>
              <LockOnIcon/>
            </template>
            <template #3>
              <UserIcon />
            </template>
            <template #4>
              <BulletpointIcon/>
            </template>
            <template #5>
              <StarFilledIcon />
            </template>
            <div style="margin-left: 7.2em;margin-top: -2.1em;">
              <t-space style="cursor: pointer;border: solid white 3px;border-radius: 50%;" :size="100">
                <t-avatar shape="circle" :image="user.userPic" :hide-on-load-failed="false" />
              </t-space>
              <div style="margin-left: 2.5em;margin-top: -1.8em">
                <span style="cursor: pointer">{{user.userName}}</span>
              </div>
            </div>
          </t-dropdown>
        </div>
      </div>
    </div>
  </t-header>
  <ConfirmDialog
      @on-close="onCloseLogout"
      @on-confirm="onConfirmLogout"
      :is-visible="this.dialog.ensureLogout"
      title="提示"
      content="确定要退出登录吗？"/>

</template>

<script>
import {mapState} from "vuex";
import {
  ArrowRightIcon,
  LockOnIcon,
  UserIcon,
  StarFilledIcon,
  NotificationFilledIcon,
  AddRectangleIcon,
  FlagIcon,
  BulletpointIcon
} from "tdesign-icons-vue-next";
import ConfirmDialog from "@/page/component/dialog/ConfirmDialog";
import {removeToken} from "@/common/token.store";
import {style} from "@/common/style";
import {store} from "@/store";
import {DEV_CONFIG} from "@/config/dev.config";
import router from "@/router/router";

export default {
  name: "HeaderDirection",
  components: {
    FlagIcon,
    AddRectangleIcon,
    ConfirmDialog, ArrowRightIcon,LockOnIcon,UserIcon,BulletpointIcon,StarFilledIcon,NotificationFilledIcon},
  data() {
    return {
      registerBtn: {
        ghost: true
      },
      btn: {
        notification: '#B5C0CA',
        takePartIn: '#B5C0CA',
        organized: '#B5C0CA'
      },
      menu: {
        tag: {
          mainPage: {
            hot: '',
            select: false
          },
          contestPage: {
            hot: 'hot',
            select: false
          },
          questionRepo: {
            hot: '',
            select: false
          },
          coursePage: {
            hot: '',
            select: false
          },
          bookPage: {
            hot: '',
            select: false
          }
        }
      },
      user: {
        operation: '参赛',
        userName: '',
        userPic: '',
        userType: '',
        menu: [
            { content: '我的竞赛', value: 1, divider: false, prefixIcon: <StarFilledIcon /> },
            { content: '我的题库', value: 2, divider: false, prefixIcon: <BulletpointIcon /> },
            { content: '个人信息', value: 3, divider: false, prefixIcon: <UserIcon /> },
            { content: '修改密码', value: 4, divider: false, prefixIcon: <LockOnIcon /> },
            { content: '退出登录', value: 5, divider: false, prefixIcon: <ArrowRightIcon /> }
        ]
      },
      dialog: {
        ensureLogout: false
      }
    }
  },
  methods: {
    userMenuHandler: function(item) {
      // MessagePlugin.info("选中" + item.value + ": " + item.content)
      if(item.value === 1){
        this.toContestList()
      }
      if(item.value === 2){
        this.toQuestionRepo()
      }
      if(item.value === 5){
        this.confirmLogout()
      }
    },
    toContestList: function () {
      this.$emit('to-contest-list')
    },
    toQuestionRepo: function () {
      this.$emit('to-question-repo')
    },
    confirmLogout: function () {
      this.dialog.ensureLogout = true
    },
    onConfirmLogout: function () {
      this.dialog.ensureLogout = false
      if(removeToken()){
        this.$emit('to-login')
      }
    },
    onCloseLogout: function () {
      this.dialog.ensureLogout = false
    },
    changePage: (page) => {
        switch (page) {
          case style.HEADER_MENU.MAIN_PAGE: {
            router.push('/main')
            break
          }
          case style.HEADER_MENU.CONTEST_PAGE: {
            router.push('/contest')
            break
          }
          case style.HEADER_MENU.QUESTION_REPO: {
            router.push('/ques')
            break
          }
          case style.HEADER_MENU.COURSE_PAGE: {
            router.push('/course')
            break
          }
          case style.HEADER_MENU.BOOK_PAGE: {
            router.push('/book')
            break
          }
      }
    },
    toCreatedContest: () => {
      router.push('/contest/create')
    }
  },
  computed: {
    ...mapState({
      token: 'token',
      page: 'page'
    }),
    isMainPage: function () {
      return store.state.page === style.HEADER_MENU.MAIN_PAGE
    },
    isContestPage: function () {
      return store.state.page === style.HEADER_MENU.CONTEST_PAGE
    },
    isQuestionPage: function () {
      return store.state.page === style.HEADER_MENU.QUESTION_REPO
    },
    isCoursePage: function () {
      return store.state.page === style.HEADER_MENU.COURSE_PAGE
    },
    isBookPage: function () {
      return store.state.page === style.HEADER_MENU.BOOK_PAGE
    }
  },
  watch: {
    "$store.state.userDto":{
      handler:function(newVal){
        if(newVal !== undefined){
          this.user.userName = store.state.userDto.userName
          this.user.userPic = DEV_CONFIG.BASE_URL.concat('/user/pic/get/').concat(store.state.userDto.userId)
          this.user.userType = store.state.userDto.userType
        }
      }
    }
  },
  mounted() {
    if(store.state.userDto !== undefined){
      this.user.userName = store.state.userDto.userName
      this.user.userPic = DEV_CONFIG.BASE_URL.concat('/user/pic/get/').concat(store.state.userDto.userId)
      this.user.userType = store.state.userDto.userType
    }
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
  .selected{
    color: deepskyblue;
    border-bottom: 3px solid deepskyblue;
    font-weight: bold;
  }

  .selecting{
    color: deepskyblue;
  }

</style>