<template>
  <div class="title">
    <div style="font-size: 30px;font-weight: bold;color: deepskyblue;display: inline-block;margin-top: 0.8em">消息中心</div>
  </div>
  <div class="body">
    <p class="allMsg">消息通知</p>
    <t-list style="margin-bottom: 5em" :async-loading="asyncLoading" :split="true" @load-more="loadMore">
      <t-list-item :key="index" v-for="(notifyMessage,index) in notifyMessageList" class="list-item">
        <t-list-item-meta :image="'http://localhost:8080/user/pic/get/'.concat(notifyMessage.createdBy)"
                          :title="notifyMessage.creatorName"
                          @click="browseMessage(notifyMessage)"
                          :description="notifyMessage.messageTitle" style="cursor:pointer;"/>
        <template #action>
          <p style="margin-top:-0.2em" :style="{'color': notifyMessage.hasRead?'black':'red'}">{{getTime(notifyMessage.createdDate) + ' '}}({{notifyMessage.hasRead?'已读':'未读'}})</p>
          <div style="margin-top: 0.5em">
            <t-button style="margin-left: 5em" variant="text" shape="square">
              <BrowseIcon @click="browseMessage(notifyMessage)"/>
            </t-button>
            <t-button variant="text" shape="square">
              <DeleteIcon @click="deleteMessage(index)"/>
            </t-button>
          </div>
        </template>
      </t-list-item>
    </t-list>
  </div>
  <MessageShower :title="currentMessage.messageTitle"
                 :key="currentMessage.key"
                 :visible="currentMessage.visitable"
                 @on-close="currentMessage.visitable = false"
                 :content="currentMessage.messageContent"/>
  <ConfirmDialog :content="dialog.deleteConfirmDialog.content"
                 :is-visible="dialog.deleteConfirmDialog.visitable"
                 :title="dialog.deleteConfirmDialog.title"
                 @on-confirm="confirmDeleteNotify" @on-close="dialog.deleteConfirmDialog.visitable = false"/>
</template>

<script>
import {BrowseIcon, DeleteIcon} from "tdesign-icons-vue-next";
import {deleteNotifyMessage, loadNotifyMessage, setHasRead} from "@/api/notify";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";
import {getTimeStrOfChina} from "@/util/date.util";
import MessageShower from "@/page/notify/component/MessageShower";
import ConfirmDialog from "@/page/component/dialog/ConfirmDialog";

const LOADING_TYPE = {
  LOADING: 'loading',
  LOAD_MORE: 'load-more',
  LOAD_END: 'loading-custom'
}

export default {
  components: {
    ConfirmDialog,
    MessageShower,
    BrowseIcon, DeleteIcon
  },
  name: "MessageCenter",
  data() {
    return {
      listLoading: 'load-more',
      asyncLoadingRadio: 'load-more',
      dialog: {
        deleteConfirmDialog: {
          content: null,
          visitable: false,
          title: '提示',
          index: null
        },
      },
      notifyMessageList: [
        // {
        //   messageId: 'system',
        //   messageTitle: '竞赛审核通知',
        //   messageContent: '恭喜您，您举办的网络竞赛ACM竞赛系统已通过审核！系统将在报名时间开启报名，并在比赛时间开放比赛，请注意遵守平台规则规章制度。',
        //   creatorName: '系统通知',
        //   createdDate: '发送时间',
        //   hasRead: false
        // }
      ],
      currentMessage: {
        visitable: false,
        messageTitle: null,
        messageContent: null,
        key: 1
      },
      page: 1
    }
  },
  methods: {
    browseMessage: function (notifyMessage) {
      this.currentMessage.messageTitle = notifyMessage.messageTitle
      this.currentMessage.messageContent = notifyMessage.messageContent
      this.currentMessage.key = (this.currentMessage.key+1)%500
      if(!notifyMessage.hasRead){
        this.$emit('decr-notify-count')
      }
      notifyMessage.hasRead = true
      this.currentMessage.visitable = true
      setHasRead(notifyMessage.messageId)
    },
    deleteMessage: function (index) {
      this.dialog.deleteConfirmDialog.content = '确定要删除通知消息 ”'.concat(this.notifyMessageList[index].messageTitle).concat('“ 吗？')
      this.dialog.deleteConfirmDialog.index = index
      this.dialog.deleteConfirmDialog.visitable = true
      // this.notifyMessageList.splice(index,1)
    },
    getTime: function (time) {
      return getTimeStrOfChina(time)
    },
    loadMore: function () {
      this.asyncLoadingRadio = LOADING_TYPE.LOADING
      loadNotifyMessage(this.page++).then(resp => {
        if (resp.data.resultCode === result.code.SUCCESS) {
          let list = resp.data.data
          if (list.length === 0) {
            this.asyncLoadingRadio = LOADING_TYPE.LOAD_END
            return
          }
          let _this = this
          setTimeout(() => {
            for (let i = 0; i < list.length; i++) {
              _this.notifyMessageList.push(list[i])
            }
            _this.asyncLoadingRadio = LOADING_TYPE.LOAD_MORE
          }, 1000)
        } else {
          this.asyncLoadingRadio = LOADING_TYPE.LOAD_MORE
          MessagePlugin.error('系统繁忙！')
        }
      }).catch(() => {
        this.asyncLoadingRadio = LOADING_TYPE.LOAD_MORE
        MessagePlugin.error('系统繁忙！')
      })
    },
    confirmDeleteNotify: function () {
      this.dialog.deleteConfirmDialog.visitable = false
      deleteNotifyMessage(this.notifyMessageList[this.dialog.deleteConfirmDialog.index].messageId).then(resp => {
        if(resp.data.resultCode === result.code.SUCCESS){
          this.notifyMessageList.splice(this.dialog.deleteConfirmDialog.index,1)
        }
      })
      // alert('删除' + this.notifyMessageList[this.dialog.deleteConfirmDialog.index].messageTitle)
    }
  },
  computed: {
    asyncLoading: function () {
      if (this.asyncLoadingRadio === LOADING_TYPE.LOAD_END) {
        return '暂时没有更多消息了';
      }
      return this.asyncLoadingRadio;
    }
  },
  mounted() {
    loadNotifyMessage(this.page++).then(resp => {
      this.notifyMessageList.length = 0
      if (resp.data.resultCode === result.code.SUCCESS) {
        let list = resp.data.data
        if (list.length === 0) {
          this.asyncLoadingRadio = LOADING_TYPE.LOAD_END
        }
        for (let i = 0; i < list.length; i++) {
          this.notifyMessageList.push(list[i])
        }
      }
    })
  }
}
</script>

<style scoped>
.title {
  width: 100%;
  height: 6em;
  margin-top: -0.7em;
  background: linear-gradient(#f9f9f9, #f6f5f7);
  text-align: center
}

.body {
  width: 55%;
  margin-top: 1em;
  margin-left: 22em;
  /*background-color: red;*/
}

.allMsg {
  margin-top: 2em;
  color: #666;
  height: 33px;
  border-bottom: 2px solid #ddd;
  border-bottom-color: rgb(221, 221, 221);
  margin-bottom: 0;
}

.userPic {
  width: 2.8em;
  height: 2.8em;
  border-radius: 50%;
}

.list-item:hover {
  background-color: #cccccc;
}
</style>