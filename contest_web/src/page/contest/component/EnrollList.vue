<template>
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
        <t-link style="margin-left: 2em"
                @click="enterContestDetail(row)"
                theme="primary">
          进入比赛
        </t-link>
      </template>
    </t-table>
  </div>
</template>
<script setup lang="jsx">
import {onMounted, reactive, ref} from 'vue';
import {CheckCircleFilledIcon, CloseCircleFilledIcon, ErrorCircleFilledIcon, StarFilledIcon} from 'tdesign-icons-vue-next';
import {getTimeStr} from "@/util/date.util";
import router from "@/router/router";
import {getOrganizerEnrollList} from "@/api/contest";
import {result} from "@/common/request.result";
// import {store} from "@/store";
// import {mutationName} from "@/store/mutations/const.name";

let data = reactive([
  // {
  //   contestId: '424399885819711488',
  //   contestSubject: '北理珠蓝桥杯校内选拔赛',
  //   contestStatus: 'CHECKING',
  //   contestStartTime: new Date(),
  //   contestEndTime: new Date()
  // }
]);
const statusNameListMap = {
  CHECKING: { label: '正在审核', theme: 'primary', icon: <ErrorCircleFilledIcon /> },
  PASSABLE: { label: '审核通过', theme: 'success', icon: <CheckCircleFilledIcon /> },
  ENROLLING: { label: '正在报名', theme: 'primary', icon: <ErrorCircleFilledIcon /> },
  WAIT_CONTEST: { label: '等待开始', theme: 'primary', icon: <StarFilledIcon /> },
  CONTESTING: { label: '正在比赛', theme: 'warning', icon: <ErrorCircleFilledIcon /> },
  CONTEST_END: { label: '比赛结束', theme: 'default', icon: <CloseCircleFilledIcon /> },
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
  { colKey: 'operation', title: '操作', width: 120, foot: '-',align: 'center' },
]);


const showContestDetail = (row) => {
  router.push('/contest/detail/'.concat(row.contestId))
}

const enterContestDetail = (row) => {
  // store.commit(mutationName.SET_FULL_SCREEN,true)
  router.push('/contest/online/tip/'.concat(row.contestId))
}

onMounted(() => {
  getOrganizerEnrollList().then(resp => {
    if(resp.data.resultCode === result.code.SUCCESS){
      let contestDetailList = resp.data['data'];
      for(let i=0;i<contestDetailList.length;i++){
        data.push(contestDetailList[i])
      }
    }
  })
})
</script>
