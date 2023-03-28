<template>
  <t-space direction="vertical">
    <!-- 当数据为空需要占位时，会显示 cellEmptyContent -->
    <t-table
        row-key="index"
        :data="data"
        :columns="columns"
        :stripe="stripe"
        :bordered="bordered"
        :hover="hover"
        :table-layout="tableLayout ? 'auto' : 'fixed'"
        :size="size"
        :pagination="pagination"
        :show-header="showHeader"
        cell-empty-content="-"
        resizable
        @row-click="handleRowClick"
    >
      <template #operation="{row}">
        <t-badge :count="row.tips">
          <t-link @click="openChatDialog(row)" style="margin-left: 0.5em" theme="default">查看消息</t-link>
        </t-badge>
      </template>
    </t-table>
  </t-space>
  <ChatDialog :visitable="ws.chatDialog.visitable"
              ref="chat"
              @on-close="onChatDialogClose"
              :self-id="ws.selfId"
              :key="ws.chatKey"
              :user-id="ws.userId"
              :self-name="ws.selfName"
              :user-name="ws.userName"
              :header="ws.userName"
              @send-message="sendMessage"
              :chat-records="ws.chatRecords"
              :websocket="websocket"/>
</template>

<script setup lang="jsx">
import {onMounted, reactive, ref} from 'vue';
import {getChatRecordsListByUserId, getOnlineStatusDto} from "@/api/online";
import ChatDialog from '@/page/contest/component/ChatDialog'
import router from "@/router/router";
import {MessagePlugin} from "tdesign-vue-next";
import {store} from "@/store";
import {getUserDetail} from "@/api/user";
const data = reactive([]);
const total = 28;

let websocket = reactive(null)

const ws = reactive({
  chatRecords: [],
  userName: null,
  selfName: null,
  userId: null,
  selfId: null,
  chatKey: 1,
  chatDialog: {
    visitable: false
  }
})

const onChatDialogClose = () => {
  ws.chatDialog.visitable = false
}

const sendMessage = (chatDto) => {
  websocket.send(JSON.stringify({
    data: chatDto,
    request: '/chat'
  }))
  ws.chatRecords.push(chatDto)
}

const openChatDialog = (row) => {
  ws.userId = row.userId
  ws.userName = row.userName
  ws.selfId = store.getters.getUserId
  ws.selfName = store.getters.getUserName
  ws.chatKey = (ws.chatKey + 1)%50
  ws.chatRecords.length = 0
  getChatRecordsListByUserId(ws.userId).then(resp => {
    let list = resp.data.data
    ws.chatRecords.length = 0
    for (let i = 0; i < list.length; i++) {
      ws.chatRecords.push(list[i])
    }
  })
  ws.chatDialog.visitable = true
  for (let i = 0; i < data.length; i++) {
    if(data[i].userId === ws.userId){
      data[i].tips = ''
      break
    }
  }
}

const stripe = ref(true);
const bordered = ref(true);
const hover = ref(false);
const tableLayout = ref(false);
const size = ref('medium');
const showHeader = ref(true);


const statusMap = {
  ING: <t-link theme="danger">比赛中</t-link>,
  NO_START: <t-link theme="primary">未开始</t-link>,
  END: <t-link theme="default">已结束</t-link>
}

const columns = ref([
  { colKey: 'userId', title: '参赛者id', width: '100', align: 'center'},
  { colKey: 'userName', title: '参赛者昵称', width: '100', align: 'center'},
  { colKey: 'contestId', title: '竞赛场次id', width: '100', align: 'center'},
  {
    colKey: 'status',
    title: '参赛状态',
    width: '100',
    align: 'center',
    cell: (h,{row}) => {
      return statusMap[row.status]
    }
  },
  { colKey: 'operation', title: '操作',width: '100', align: 'center'}
]);

const handleRowClick = (e) => {
  console.log(e);
};

const pagination = {
  defaultCurrent: 1,
  defaultPageSize: 5,
  total,
};

onMounted(async () => {
  await getOnlineStatusDto(router.currentRoute.value.params.contestId).then(async resp => {
    for (let i = 0; i < resp.data.data.length; i++) {
      resp.data.data[i].tips = ''
      data.push(resp.data.data[i])
    }
    // data = resp.data.data
    await getUserDetail().then(resp => {
      ws.selfId = resp.data.data.userId
    })
    websocket = new WebSocket('ws://127.0.0.1:8596/ws/'.concat(ws.selfId))
    websocket.onopen = () => {
      console.log('成功连接到WebSocket服务器')
      getChatRecordsListByUserId(ws.userId).then(resp => {
        let list = resp.data.data
        ws.chatRecords.length = 0
        for (let i = 0; i < list.length; i++) {
          ws.chatRecords.push(list[i])
        }
      })
    }
    websocket.onmessage = (m) => {
      let sourceUserId = JSON.parse(m.data).data.sourceUserId
      if (ws.userId !== sourceUserId || !ws.chatDialog.visitable) {
        MessagePlugin.info('您有新消息，请查看！')
        for (let i = 0; i < data.length; i++) {
          if(data[i].userId === sourceUserId){
            data[i].tips = '!'
            break
          }
        }
      }
      ws.chatRecords.push(JSON.parse(m.data).data)
    }
    websocket.onerror = () => {
      console.log('WebSocket连接异常')
    }
    websocket.onclose = () => {
      console.log('与WebSocket服务器断开连接')
    }
  })

})
</script>
