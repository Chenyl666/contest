<template>
  <div class="dialog-attach-wrap">
    <t-dialog
        v-model:visible="visit"
        :top="`30px`"
        attach="body"
        header="运行情况"
        @close="onClose"
        @confirm="onClose"
        width="60em"
        style="height: 80em"
        destroy-on-close>
      <template #body>
        <div v-if="running">
          <div v-if="!error">
            <t-loading style="margin-left: 9em;margin-top: 1em"/>
<!--            <CloseCircleFilledIcon style="color: red;margin-left: 23em;margin-top: 2.5em;width: 2.8em;height: 2.8em"-->
<!--                                   v-if="error"/>-->
            <span :style="{color: '#0075FF'}"
                  style="display: block;font-weight: bold;font-size: 16px;;float: right;margin-right: 21.5em;margin-top: 2.7em">
              运行中
            </span>
          </div>
          <div v-if="error" style="color: red">
            <h3 style="color: red">错误：</h3>
            {{errorMessage}}
          </div>
        </div>
        <div v-if="!running" style="overflow-y: auto;width: 55em;height: 35em">
          <div style="margin-top: 0.5em">
            <span :style="{color: statusMap[status].theme}">运行状态：{{ statusMap[status].label }}</span>
            <span style="margin-left: 5em;">得分：{{ sumScore }}</span>
          </div>
          <t-table
              style="margin-top: 1em"
              row-key="index"
              :data="data"
              :columns="columns"
              :stripe="stripe"
              :bordered="bordered"
              :hover="hover"
              :table-layout="tableLayout ? 'auto' : 'fixed'"
              :size="size"
              :show-header="showHeader"
              cell-empty-content="-"
              @row-click="handleRowClick"
          >
            <template #operation="{row}">
              <t-link @click="showExampleOutput(row)" theme="primary">查看输出</t-link>
            </template>
          </t-table>
        </div>
      </template>
    </t-dialog>
  </div>
  <ExampleOutputDialog @on-close="dialog.visit = false" :input="dialog.input" :output="dialog.output" :visitable="dialog.visit"/>
</template>
<script setup>
import {defineProps, defineEmits, toRef, reactive} from "vue";
import {ref} from 'vue';
import {ErrorCircleFilledIcon, CheckCircleFilledIcon, CloseCircleFilledIcon} from 'tdesign-icons-vue-next';
import {getTimeStrOfChina} from "@/util/date.util";
import {store} from "@/store";
import ExampleOutputDialog from "@/page/contest/component/ExampleOutputDialog";

const emits = defineEmits(['on-close'])

const statusMap = {
  1: {
    label: "完全正确",
    theme: 'limegreen'
  },
  2: {
    label: "部分正确",
    theme: 'darkorange'
  },
  3: {
    label: "答案错误",
    theme: 'red'
  }
}

const dialog = reactive({
  input: '',
  output: '',
  visit: false
})

const props = defineProps({
  visitable: {
    type: Boolean,
    required: true
  },
  running: {
    type: Boolean,
    required: true
  },
  data: {
    type: Object,
    required: true,
  },
  sumScore: {
    type: Number,
    required: true,
    default: 25
  },
  status: {
    type: Number,
    required: true,
    default: 1
  },
  error: {
    type: Boolean,
    required: true
  },
  errorMessage: {
    type: String,
    required: true
  }
})

const onClose = () => {
  dialog.visit = false
  emits('on-close')
}

const data = toRef(props, 'data')

const visit = toRef(props, 'visitable')

const statusNameListMap = {
  1: {label: '测试通过', theme: 'success', icon: <CheckCircleFilledIcon/>},
  2: {label: '答案错误', theme: 'danger', icon: <CloseCircleFilledIcon/>},
  3: {label: '内存溢出', theme: 'warning', icon: <ErrorCircleFilledIcon/>},
  4: {label: '运行超时', theme: 'warning', icon: <ErrorCircleFilledIcon/>},
};

const stripe = ref(true);
const bordered = ref(true);
const hover = ref(false);
const tableLayout = ref(false);
const size = ref('medium');
const showHeader = ref(true);

const columns = ref([
  {colKey: 'number', title: '测试点', width: '100', align: 'center'},
  {
    colKey: 'code',
    title: '测试结果',
    align: 'center',
    cell: (h, {row}) => {
      return (
          <t-tag shape="round" theme={statusNameListMap[row.code].theme} variant="light-outline">
            {statusNameListMap[row.code].icon}
            {statusNameListMap[row.code].label}
          </t-tag>
      );
    },
  },
  {colKey: 'score', title: '得分', align: 'center'},
  {
    colKey: 'username',
    title: '提交用户',
    align: 'center',
    cell: () => {
      return store.getters.getUserName
    }
  },
  {
    colKey: 'createTime',
    title: '提交时间',
    align: 'center',
    cell: () => {
      let date = new Date();
      return getTimeStrOfChina(date)
    }
  },
  {
    colKey: 'operation',
    title: '操作',
    align: 'center'
  }
]);

const handleRowClick = (e) => {
  console.log(e);
};

const showExampleOutput = (row) => {
  dialog.output = row.output
  dialog.input = row.input
  dialog.visit = true
}

</script>
<style scoped>
.dialog-attach-wrap {
  position: relative;
  height: 400px;
  padding: 20px;
  border: 1px solid #ebedf0;
  border-radius: 2px;
  overflow: hidden;
}

body {
  width: 3000px;
}
</style>
