<template>
  <t-space direction="vertical">
    <t-form style="">
      <t-form-item :label="`通知标题`">
        <t-input v-model="search" style="width: 20em"/>
        <t-button @click="searchContestNotify" style="margin-left: 2em" theme="success">筛选</t-button>
        <t-button style="margin-left: 2em" @click="dialog.addContestNotifyDialog.visitable = true">发布</t-button>
      </t-form-item>
    </t-form>

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
        resizable>
      <template #operation="{row}">
        <t-link @click="showNotify(row)" theme="primary" style="margin-right: 1em">查看</t-link>
        <t-link @click="deleteById(row)" theme="danger">删除</t-link>
      </template>
    </t-table>
  </t-space>
  <AddContestNotifyDialog @on-confirm="getList(router.currentRoute.value.params.contestId)" @on-close="dialog.addContestNotifyDialog.visitable = false" :visitable="dialog.addContestNotifyDialog.visitable"/>
  <NotifyDialog @on-close="dialog.notifyDialog.visitable = false"
                :content="dialog.notifyDialog.content"
                :title="dialog.notifyDialog.title"
                :visitable="dialog.notifyDialog.visitable"/>
</template>

<script setup lang="jsx">
import {onMounted, reactive, ref} from 'vue';
import {getTimeStr} from "@/util/date.util";
import AddContestNotifyDialog from "@/page/contest/component/detail_page/dialog/AddContestNotifyDialog";
import {deleteContestNotifyById, publishList} from "@/api/notify";
import router from "@/router/router";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";
import NotifyDialog from "@/page/contest/component/detail_page/dialog/NotifyDialog";
const dialog = reactive({
  addContestNotifyDialog: {
    visitable: false
  },
  notifyDialog: {
    visitable: false,
    title: null,
    content: null
  }
})
const data = reactive([]);
const allData = []
const total = 28;
const stripe = ref(true);
const bordered = ref(true);
const hover = ref(false);
const tableLayout = ref(false);
const size = ref('medium');
const showHeader = ref(true);
const search = ref('')

const columns = ref([
  { colKey: 'title', title: '通知标题', width: '100',align: 'center' },
  { colKey: 'contestSubject', title: '竞赛名称', width: '100',align: 'center' },
  { colKey: 'createdBy', title: '发布人', width: '100',align: 'center' },
  {
    colKey: 'createdDate',
    title: '发布时间',
    width: '100',
    align: 'center',
    cell: (h,{row}) => {
      return getTimeStr(row.createdDate)
    }
  },
  { colKey: 'operation', title: '操作', width: '100',align: 'center' },
]);

const pagination = reactive({
  defaultCurrent: 1,
  defaultPageSize: 5,
  total,
});

const showNotify = (contestNotify) => {
  dialog.notifyDialog.title = contestNotify.title
  dialog.notifyDialog.content = contestNotify.content
  dialog.notifyDialog.visitable = true
}

const deleteById = (contestNotify) => {
  if(confirm('确定删除通知：'.concat(contestNotify.title).concat("?")))
  deleteContestNotifyById(contestNotify.contestNotifyId).then(resp => {
    if(resp.data.resultCode === result.code.SUCCESS){
      MessagePlugin.success('删除成功！')
      getList(router.currentRoute.value.params.contestId)
    }else{
      MessagePlugin.error('删除失败！')
    }
  }).catch(() => {
    MessagePlugin.error('删除失败！')
  })
}

const getList = (contestId) => {
  publishList(contestId).then(resp => {
    let list = resp.data.data
    data.length = 0
    allData.length = 0
    for(let i=0;i<list.length;i++){
      data.push(list[i])
      allData.push(list[i])
    }
    pagination.total = data.length
    pagination.defaultPageSize = 8
  })
}

const searchContestNotify = () => {
  data.length = 0
  for (let i = 0; i < allData.length; i++) {
    if(allData[i].title.indexOf(search.value)!==-1){
      data.push(allData[i])
    }
  }
  pagination.total = data.length
}

onMounted(() => {
  getList(router.currentRoute.value.params.contestId)
})
</script>
