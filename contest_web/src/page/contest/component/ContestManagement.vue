<template>
  <t-space direction="vertical" style="margin-top: 1em">
    <t-tabs :value="value" placement="left" :theme="theme" @change="handlerChange">
      <t-tab-panel value="first">
        <template #label> <t-icon name="user" class="tabs-icon-margin" /> 名单管理 </template>
        <p style="padding-left: 25px">
          <UserList/>
        </p>
      </t-tab-panel>
      <t-tab-panel value="second">
        <template #label> <t-icon name="cloud" class="tabs-icon-margin" /> 状态管理 </template>
        <p style="padding-left: 25px">
          状态管理
        </p>
      </t-tab-panel>
      <t-tab-panel value="third">
        <template #label> <t-icon name="notification" class="tabs-icon-margin" /> 通知管理 </template>
        <p style="padding-left: 25px">
          <NotifyList/>
        </p>
      </t-tab-panel>
      <t-tab-panel value="forth">
        <template #label> <t-icon name="root-list" class="tabs-icon-margin" /> 判题管理 </template>
        <p style="padding-left: 25px">
          <QuestionManagement/>
        </p>
      </t-tab-panel>
    </t-tabs>
  </t-space>
</template>
<script setup>
import { ref } from 'vue';
import UserList from "@/page/contest/component/detail_page/UserList";
import QuestionManagement from "@/page/contest/component/detail_page/QuestionManagement";
import NotifyList from "@/page/contest/component/detail_page/NotifyList";

const value = ref('third');
const theme = ref('normal');

const handlerChange = (newValue) => {
  value.value = newValue;
};
</script>
<style lang="less">
.tabs-icon-margin {
  margin-right: 4px;
}
</style>
