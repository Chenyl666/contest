<template>
  <t-dialog
      v-model:visible="isVisitable"
      :header="`参赛者信息`"
      attach="body"
      :confirm-on-enter="true"
      :on-close="close"
      :on-confirm="onConfirmAnother"
      :footer="false"
      destroy-on-close>
    <template #body>
      <div>
        <span>用户id:{{user.userId}}</span><br>
        <span>邮箱:{{user.userEmail}}</span><br>
        <span>联系方式:{{user.userEmail === null?'无':user.userEmail}}</span><br>
        <span>昵称：{{user.userName}}</span><br>
        <span>身份证号码：{{user.identify}}</span><br>
        <span>所在单位：{{user.unit}}</span><br>
      </div>
    </template>
  </t-dialog>
</template>
<script setup>
import {toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  },
  user: {
    type: Object,
    required: true
  }
})

const emits = defineEmits(['on-close','on-confirm'])

const isVisitable = toRef(props,'visible')

const onConfirmAnother = () => {
  emits('on-confirm')
};
const close = () => {
  emits('on-close')
};

</script>
