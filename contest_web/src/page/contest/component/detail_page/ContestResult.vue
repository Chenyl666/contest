<template>
  <h1 style="margin-left: 15em">成绩列表</h1>
  <t-link @click="exportResultAsExcel" style="margin-left: 29.5em" theme="primary">导出成绩</t-link>
  <t-link :disabled="props.contestDetail.contestStatus === 'PUBLISH'" style="margin-left: 1em" @click="publish">发布成绩</t-link>
  <t-space direction="vertical">
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
    </t-table>
  </t-space>
</template>

<script setup lang="jsx">
import {onMounted, reactive, ref, toRef} from 'vue';
// import { ErrorCircleFilledIcon, CheckCircleFilledIcon, CloseCircleFilledIcon } from 'tdesign-icons-vue-next';
import {defineProps} from "vue";
import {defineEmits} from "vue";
import {exportContestResultAsExcel} from "@/api/contest";
import router from "@/router/router";
import {MessagePlugin} from "tdesign-vue-next";

const props = defineProps({
  contestResultList: {
    type: Array,
    required: true
  },
  contestDetail: {
    type: Object,
    required: true,
  }
})

// const total = ref(props.contestResultList.length)

const emits = defineEmits(['publish'])

const data = toRef(props,'contestResultList')

const stripe = ref(true);
const bordered = ref(true);
const hover = ref(false);
const tableLayout = ref(false);
const size = ref('medium');
const showHeader = ref(true);

const columns = ref([
  { colKey: 'rank', title: '排名', width: '100', align: 'center' },
  { colKey: 'userId', title: '用户id', width: '100', align: 'center' },
  { colKey: 'userName', title: '用户昵称', width: '100', align: 'center' },
  { colKey: 'contestSubject', title: '竞赛名称', width: '100', align: 'center' },
  { colKey: 'sumScore', title: '总得分', width: '100', align: 'center' },
]);

const handleRowClick = (e) => {
  console.log(e);
};

const pagination = reactive({
  defaultCurrent: 1,
  defaultPageSize: 10,
  total: props.contestResultList.length
});

const publish = () => {
  emits('publish')
}

const exportResultAsExcel = () => {
  MessagePlugin.info('正在请求下载')
  exportContestResultAsExcel(router.currentRoute.value.params.contestId)
}

onMounted(() => {
  setTimeout(() => {
    pagination.total = props.contestResultList.length
  },500)
})
</script>
