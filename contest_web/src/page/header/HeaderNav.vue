<template>
  <t-header>
    <div style="background-color: white;width: 100%;height: 4em;margin-bottom: -0.5em;border-bottom: 2px solid #f6f5f7">
      <img src="../../assets/logo.png" style="width:13.44em;height: 4em;margin-left: 10em;" alt="LOGO">
      <div style="margin-left: 28em;position: fixed">
        <ul style="display: flex;list-style-type:none;margin-top: -3em">
          <li class="dir-item">
            <t-badge :count="menu.tag.mainPage.hot" class="txt">
              <span :style="{fontWeight: isMainPage,textDecoration: isMainPage==='bold'?'underline':null}">首页</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge :count="menu.tag.contestPage.hot" class="txt">
              <span :style="{fontWeight: isContestPage,textDecoration: isContestPage==='bold'?'underline':null}">竞赛</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge :count="menu.tag.questionRepo.hot" class="txt">
              <span :style="{fontWeight: isQuestionPage,textDecoration: isQuestionPage==='bold'?'underline':null}">题库</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge :count="menu.tag.coursePage.hot" class="txt">
              <span :style="{fontWeight: isCoursePage,textDecoration: isCoursePage==='bold'?'underline':null}">课程</span>
            </t-badge>
          </li>
          <li class="dir-item">
            <t-badge :count="menu.tag.bookPage.hot" class="txt">
              <span :style="{fontWeight: isBookPage,textDecoration: isBookPage==='bold'?'underline':null}">图书</span>
            </t-badge>
          </li>
        </ul>
      </div>

      <div style="margin-left: 73.5em;margin-top: -3.3em;display: flex">
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
              <ShopIcon />
            </template>
            <template #5>
              <StarFilledIcon />
            </template>
            <t-space style="cursor: pointer" :size="100">
              <t-avatar shape="circle" :image="user.userPic" :hide-on-load-failed="false" />
            </t-space>
          </t-dropdown>
          <NotificationFilledIcon
              @mouseenter="btn.notification = '#2c9fe5'"
              @mouseleave="btn.notification = '#B5C0CA'"
              :style="{marginLeft: '0.7em',color: btn.notification,cursor: 'pointer'}"
              size="1.8em"/>
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
import {ArrowRightIcon,LockOnIcon,UserIcon,ShopIcon,StarFilledIcon,NotificationFilledIcon} from "tdesign-icons-vue-next";
import ConfirmDialog from "@/page/component/dialog/ConfirmDialog";
import {removeToken} from "@/common/login/token.store";
import {style} from "@/const/style";
import {store} from "@/store";

export default {
  name: "HeaderDirection",
  components: {ConfirmDialog, ArrowRightIcon,LockOnIcon,UserIcon,ShopIcon,StarFilledIcon,NotificationFilledIcon},
  data() {
    return {
      userName: '',
      registerBtn: {
        ghost: true
      },
      btn: {
        notification: '#B5C0CA'
      },
      menu: {
        tag: {
          mainPage: {
            hot: ''
          },
          contestPage: {
            hot: 'hot'
          },
          questionRepo: {
            hot: ''
          },
          coursePage: {
            hot: ''
          },
          bookPage: {
            hot: ''
          }
        }
      },
      user: {
        userPic: 'http://127.0.0.1:8080/filesys/download/inline/421432667465060352',
        menu: [
            { content: <div style="text-align: center">臭崽崽</div>,   value: 1, divider: true,  prefixIcon: '' },
            { content: '我的竞赛', value: 2, divider: false, prefixIcon: <StarFilledIcon /> },
            { content: '我的订单', value: 3, divider: false, prefixIcon: <ShopIcon/> },
            { content: '个人信息', value: 4, divider: false, prefixIcon: <UserIcon /> },
            { content: '修改密码', value: 5, divider: false, prefixIcon: <LockOnIcon /> },
            { content: '退出登录', value: 6, divider: false, prefixIcon: <ArrowRightIcon /> }
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
      if(item.value === 6){
        this.confirmLogout()
      }
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
    }
  },
  computed: {
    ...mapState({
      token: 'token',
      page: 'page'
    }),
    isMainPage: function () {
      return store.state.page === style.HEADER_MENU.MAIN_PAGE?'bold':'initial'
    },
    isContestPage: function () {
      return store.state.page === style.HEADER_MENU.CONTEST_PAGE?'bold':'initial'
    },
    isQuestionPage: function () {
      return store.state.page === style.HEADER_MENU.QUESTION_REPO?'bold':'initial'
    },
    isCoursePage: function () {
      return store.state.page === style.HEADER_MENU.COURSE_PAGE?'bold':'initial'
    },
    isBookPage: function () {
      return store.state.page === style.HEADER_MENU.BOOK_PAGE?'bold':'initial'
    }
  },
  watch: {

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

</style>