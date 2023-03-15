<template>
  <t-space direction="vertical">
    <t-form style="display: table">
      <t-form-item style="display: table-cell" label="用户id">
        <t-input v-model="queryWrapper.userId"/>
      </t-form-item>
      <t-form-item style="display: table-cell" label="竞赛id">
        <t-input v-model="queryWrapper.contestId"/>
      </t-form-item>
      <t-form-item style="display: table-cell" label="竞赛名称">
        <t-input v-model="queryWrapper.contestSubject"/>
      </t-form-item>
    </t-form>

    <t-form style="display: table">
      <t-form-item  style="display: table-cell" label="报名费">
        <t-select style="width: 12.5em" v-model="queryWrapper.pay">
          <t-option value="已支付"/>
          <t-option value="未支付"/>
        </t-select>
      </t-form-item>
      <t-form-item style="display: table-cell" label="竞赛类型">
        <t-select style="width: 12.5em" v-model="queryWrapper.typeName">
          <t-option value="考试类"></t-option>
          <t-option value="编程类"></t-option>
          <t-option value="项目类"></t-option>
        </t-select>
      </t-form-item>
      <t-form-item style="display: table-cell" label="报名编号">
        <t-input v-model="queryWrapper.enrollId"/>
      </t-form-item>
    </t-form>
    <div>
      <t-button @click="query" style="margin-left: 48em;display: inline-block" theme="success">查询</t-button>
      <t-button @click="reset" style="display: inline-block;margin-left: 2em">重置</t-button>
    </div>

    <!-- 当数据为空需要占位时，会显示 cellEmptyContent -->
    <t-table
        :key="tableKey"
        row-key="index"
        :data="data"
        :columns="columns"
        :stripe="stripe"
        :bordered="bordered"
        :table-layout="tableLayout ? 'auto' : 'fixed'"
        :size="size"
        :pagination="pagination"
        :show-header="showHeader"
        cell-empty-content="-"
        @row-click="handleRowClick"
    >
      <template #operation="{row}">
        <t-link @click="getUserDetail(row.userId)" theme="primary" hover="color">
          查看
        </t-link>
        <t-link @click="deleteOganizer(row)" style="margin-left: 1em" theme="danger" hover="color">
          移除
        </t-link>
      </template>
    </t-table>
  </t-space>
  <UserDetailDialog @on-close="dialog.visitable = false"
                    :user="dialog.user"
                    :visible="dialog.visitable"/>
</template>

<script setup lang="jsx">
import {onMounted, reactive, ref} from 'vue';
// import { ErrorCircleFilledIcon, CheckCircleFilledIcon, CloseCircleFilledIcon } from 'tdesign-icons-vue-next';
import {deleteContestEnrollById, getEnrollMessageList} from "@/api/contest";
import {result} from "@/common/request.result";
import {getTimeStrOfChina} from "@/util/date.util";
import {getUserDetailById} from "@/api/user";
import UserDetailDialog from "@/page/component/dialog/UserDetailDialog";
import {MessagePlugin} from "tdesign-vue-next";
import router from "@/router/router";

const data = reactive([]);
let allData = [];
const total = 28;

const dialog = reactive({
  visitable: false,
  user: {}
})

const stripe = ref(true);
const bordered = ref(true);
const tableLayout = ref(false);
const size = ref('medium');
const showHeader = ref(true);

let queryWrapper = reactive({
  userId: null,
  contestId: null,
  contestSubject: null,
  pay: null,
  typeName: null,
  enrollId: null
})



const columns = ref([
  { colKey: 'userId', title: '用户id', width: '100', align: 'center' },
  { colKey: 'enrollId', title: '报名订单编号', width: '100', align: 'center' },
  { colKey: 'contestId', title: '竞赛id', width: '100', align: 'center' },
  { colKey: 'contestSubject', title: '竞赛名称', width: '100', align: 'center' },
  { colKey: 'typeName', title: '竞赛类型', width: '100', align: 'center' },
  {
    colKey: 'pay',
    title: '报名费',
    width: '70',
    cell: (h,{row}) => {
      return row.pay?'已支付':'未支付'
    }
    ,
    align: 'center'
  },
  {
    colKey: 'createdDate',
    title: '报名时间',
    width: '120',
    align: 'center',
    cell: (h,{row}) => {
      return getTimeStrOfChina(row.createdDate)
    }
  },
  {
    colKey: 'operation',
    title: '操作',
    width: '100',
    align: 'center',
  }
]);

const handleRowClick = (e) => {
  console.log(e);
};

const reset = () => {
  queryWrapper.pay = null
  queryWrapper.typeName = null
  queryWrapper.userId = null
  queryWrapper.contestId = null
  queryWrapper.contestSubject = null
  queryWrapper.enrollId = null
}

const query = () => {
  data.length = 0
  console.log(queryWrapper)
  for(let i=0;i<allData.length;i++){
    let line = allData[i]
    let get = true
    if(queryWrapper.userId !== null && queryWrapper.userId !== ''){
      get = get && (line.userId === queryWrapper.userId)
    }
    if(queryWrapper.contestId !== null && queryWrapper.contestId !== ''){
      get = get && (line.contestId === queryWrapper.contestId)
    }
    if(queryWrapper.contestSubject !== null && queryWrapper.contestSubject !== ''){
      get = get && (line.contestSubject === queryWrapper.contestSubject)
    }
    if(queryWrapper.pay !== null && queryWrapper.pay !== ''){
      get = get && (line.pay === (queryWrapper.pay === '已支付'))
    }
    if(queryWrapper.typeName !== null && queryWrapper.typeName !== ''){
      get = get && (line.typeName === queryWrapper.typeName)
    }
    if(queryWrapper.enrollId !== null && queryWrapper.enrollId !== ''){
      get = get && (line.enrollId === queryWrapper.enrollId)
    }
    if(get){
      data.push(line)
    }
  }
}

const getUserDetail = (userId) => {
  getUserDetailById(userId).then(resp => {
    dialog.user = resp.data.data
    dialog.visitable = true
  })
}

const deleteOganizer = (row) => {
  if(confirm('确定要删除用户"'.concat(row.userId).concat('在竞赛').concat(row.contestSubject).concat('的竞赛资格吗？'))){
    deleteContestEnrollById(row.enrollId).then(resp => {
      if(resp.data.resultCode === result.code.SUCCESS){
        MessagePlugin.success('删除成功！')
      }else{
        MessagePlugin.error('系统繁忙！')
      }
    }).catch(() => {
      MessagePlugin.error('系统繁忙！')
    })
  }
}

onMounted(() => {
  getEnrollMessageList(router.currentRoute.value.params.contestId).then(resp => {
    if(resp.data.resultCode === result.code.SUCCESS){
      allData = resp.data.data
      data.length = 0
      for(let i=0;i<allData.length;i++){
        data.push(allData[i])
      }
      pagination.total = data.length
    }
  })
})

const pagination = reactive({
  defaultCurrent: 1,
  defaultPageSize: 5,
  total,
});
</script>
