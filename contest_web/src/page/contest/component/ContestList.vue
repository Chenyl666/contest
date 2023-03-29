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
          {{nullString(row.questionRepoId)?'导入题库':'查看题库'}}
        </t-link>
        <t-link @click="deleteContestDetail(row)" style="margin-left: 2em" theme="danger">
          撤销竞赛
        </t-link>
      </template>
    </t-table>
  </div>
  <QuestionSelector @on-close="dialog.questionSelectorDialog.visitable = false"
                    @on-confirm="onQuestionSelectConfirm"
                    @import-repo="onQuestionSelectConfirm"
                    :visible="dialog.questionSelectorDialog.visitable"/>
</template>
<script setup lang="jsx">
import {onMounted, reactive, ref} from 'vue';
import {CheckCircleFilledIcon, CloseCircleFilledIcon, ErrorCircleFilledIcon,StarFilledIcon} from 'tdesign-icons-vue-next';
import {getContestDetail, getContestTypeByContestId, importContestQuestion} from "@/api/contest";
import {getTimeStr} from "@/util/date.util";
import ConfirmDialog from '@/page/component/dialog/ConfirmDialog'
import router from "@/router/router";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";
import QuestionSelector from "@/page/contest/component/QuestionSelector";

let dialog = reactive({
    deleteItemDialog: {
      visitable: false,
      title: '提示',
      content: '',
      selectContestId: ''
    },
    questionSelectorDialog: {
      visitable: false
    }
})

let state = reactive({
  contestSelected: null
})

let data = reactive([]);
const statusNameListMap = {
  CHECKING: { label: '审核中', theme: 'primary', icon: <ErrorCircleFilledIcon /> },
  PASSABLE: { label: '审核通过', theme: 'success', icon: <CheckCircleFilledIcon /> },
  ENROLLING: { label: '报名中', theme: 'primary', icon: <ErrorCircleFilledIcon /> },
  WAIT_CONTEST: { label: '待开始', theme: 'primary', icon: <StarFilledIcon /> },
  CONTESTING: { label: '比赛中', theme: 'warning', icon: <ErrorCircleFilledIcon /> },
  CONTEST_END: { label: '已结束', theme: 'default', icon: <CloseCircleFilledIcon /> },
  FAIL: { label: '审核失败', theme: 'danger', icon: <CloseCircleFilledIcon /> },
  RESULT: { label: '已结束', theme: 'default', icon: <CloseCircleFilledIcon /> },
  PUBLISH: { label: '已结束', theme: 'default', icon: <CloseCircleFilledIcon /> }
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

const importQuestion = async (row) => {
  if(!nullString(row.questionRepoId)){
    await router.push('/question/repo/detail/'.concat(row.questionRepoId))
    return
  }
  let contestType = null
  await getContestTypeByContestId(row.contestId).then(resp => {
    if(resp.data.resultCode === result.code.SUCCESS){
      contestType = resp.data.data
    }
  })
  if(contestType.typeId === 3){
    await MessagePlugin.info('该竞赛是项目型竞赛，无需导入题目！')
  }else{
    state.contestSelected = row.contestId
    dialog.questionSelectorDialog.visitable = true
  }
}

const showContestDetail = (row) => {
  router.push('/contest/detail/'.concat(row.contestId))
}

const onQuestionSelectConfirm = (questionRepoId) => {
  importContestQuestion(state.contestSelected,questionRepoId).then(resp => {
    if(resp.data.resultCode === result.code.SUCCESS){
      MessagePlugin.success('题库导入成功！')
    }else if(resp.data.resultCode === result.code.FAIL){
      MessagePlugin.error(resp.data.message)
    }
  }).catch(() => {
    MessagePlugin.error('系统繁忙！')
  })
  dialog.questionSelectorDialog.visitable = false
  state.contestSelected = null
}

const nullString = (str) => {
  return str === null || str === '' || str === 'null'
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
