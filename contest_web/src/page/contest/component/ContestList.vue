<template>
  <ConfirmDialog
      @on-close="dialog.deleteItemDialog.visitable = false"
      @on-confirm="onConfirmDeleteItem"
      :is-visible="dialog.deleteItemDialog.visitable"
      :title="dialog.deleteItemDialog.title"
      :content="dialog.deleteItemDialog.content"/>
  <div>
    <t-table
        row-key="contestId"
        :data="data"
        :columns="columns"
        :max-height="1000"
        bordered
        align="center">
      <template #operation="{row}">
        <t-link @click="showContestDetail(row)" theme="default">
          查看信息
        </t-link>
        <t-link @click="importQuestion(row)" style="margin-left: 2em" theme="primary">
          设置题目
        </t-link>
        <t-link @click="deleteContestDetail(row)" style="margin-left: 2em" theme="danger">
          撤销竞赛
        </t-link>
      </template>
    </t-table>
  </div>
</template>
<script setup lang="jsx">
import {onMounted, reactive, ref} from 'vue';
import {CheckCircleFilledIcon, CloseCircleFilledIcon, ErrorCircleFilledIcon} from 'tdesign-icons-vue-next';
import {getContestDetail} from "@/api/contest";
import {getTimeStr} from "@/util/date.util";
import ConfirmDialog from '@/page/component/dialog/ConfirmDialog'

let dialog = reactive({
    deleteItemDialog: {
      visitable: false,
      title: '提示',
      content: '',
      selectContestId: ''
    },
    showContestDetailDialog: {
      visible: false,
      cid: ''
    }
})

let data = reactive([]);
const statusNameListMap = {
  CHECKING: { label: '审核中', theme: 'primary', icon: <ErrorCircleFilledIcon /> },
  PASSABLE: { label: '审核通过', theme: 'success', icon: <CheckCircleFilledIcon /> },
  ENROLLING: { label: '报名中', theme: 'primary', icon: <ErrorCircleFilledIcon /> },
  CONTESTING: { label: '比赛中', theme: 'warning', icon: <ErrorCircleFilledIcon /> },
  END: { label: '已结束', theme: 'default', icon: <CloseCircleFilledIcon /> },
  FAIL: { label: '审核失败', theme: 'danger', icon: <CloseCircleFilledIcon /> },
};

const columns = ref([
  { colKey: 'contestId', title: '竞赛id', width: 104, foot: '-', align: 'center' },
  { colKey: 'contestSubject', title: '竞赛名称', width: 100, foot: '-', align: 'center' },
  {
    colKey: 'contestStatus',
    title: '竞赛状态',
    width: 60,
    foot: '-',
    align: 'center',
    cell: (h, { row }) => {
      return (
          // <t-tag shape="round" theme={statusNameListMap[row.status].theme} variant="light-outline">
          <t-tag shape="round" theme={statusNameListMap[row.contestStatus].theme} variant="light-outline">
            {statusNameListMap[row.contestStatus].icon}
            {statusNameListMap[row.contestStatus].label}
          </t-tag>
      );
    },
  },
  {
    colKey: 'enrollTime',
    title: '报名时间',
    width: 130,
    foot: '-',
    align: 'center',
    cell: (h, {row}) => {
      let enrollStartTime = getTimeStr(row.enrollStartTime);
      let enrollEndTime = getTimeStr(row.enrollEndTime)
      return enrollStartTime + '~' + enrollEndTime
    }
  },
  { colKey: 'contestTime',
    title: '比赛时间',
    width: 130,
    foot: '-',
    ellipsis: true,
    align: 'center',
    cell: (h, {row}) => {
      let contestStartTime = getTimeStr(row.contestStartTime);
      let contestEndTime = getTimeStr(row.contestEndTime)
      return contestStartTime + '~' + contestEndTime
    }
  },
  { colKey: 'contestPrice', title: '报名费用', width: 45, foot: '-',align: 'center', },
  { colKey: 'operation', title: '操作', width: 120, foot: '-',align: 'center', },
]);

const deleteContestDetail = (row) => {
  dialog.deleteItemDialog.selectContestId = row.contestId
  dialog.deleteItemDialog.content = '确定要撤销竞赛“' + row.contestSubject + '”吗？'
  dialog.deleteItemDialog.visitable = true
}

const onConfirmDeleteItem = () => {
  dialog.deleteItemDialog.visitable = false
  dialog.deleteItemDialog.content = null
  dialog.deleteItemDialog.selectContestId = null
}

const importQuestion = (row) => {
  alert('导入' + row.contestSubject + '的题目页面')
}

const showContestDetail = (row) => {
  dialog.showContestDetailDialog.cid = row.contestId
  dialog.showContestDetailDialog.visible = true
  // alert('展示' + row.contestSubject + '的信息')
}


onMounted(() => {
  getContestDetail().then(resp => {
    let contestDetailList = resp.data['data'];
    for(let i=0;i<contestDetailList.length;i++){
      data.push(contestDetailList[i])
    }
  })
})
</script>
