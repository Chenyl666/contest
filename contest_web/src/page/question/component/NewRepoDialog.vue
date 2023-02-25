<template>
  <t-dialog
      v-model:visible="isVisitable"
      :header="`新建题库`"
      attach="body"
      :confirm-on-enter="true"
      :on-close="close"
      :on-confirm="onConfirmAnother">
    <template #body>
      <t-form-item>
        <t-input v-model="formData.input" placeholder="请输入题库名称"/>
      </t-form-item>
      <t-form-item>
        <t-select v-model="formData.option" placeholder="请输入题库类型">
          <t-option key="PROGRAMMING" label="编程题库" value="PROGRAMMING" />
          <t-option key="PAPER" label="试卷题库" value="PAPER"></t-option>
          <t-option key="PROJECT" label="项目题库" value="PROJECT" />
        </t-select>
      </t-form-item>
    </template>
  </t-dialog>
</template>
<script setup>
import {ref, toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";
import {MessagePlugin} from "tdesign-vue-next";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  },
})

const emits = defineEmits(['on-close','on-confirm'])

const isVisitable = toRef(props,'visible')

const formData = ref({
  input: null,
  option: null
})

const onConfirmAnother = () => {
  if((formData.value.input !== null && formData.value.input !== '') && formData.value.option !== null){
    emits('on-confirm',formData.value)
    formData.value.input = null
    formData.value.option = null
  }else{
    MessagePlugin.error('请将信息输入完整')
  }

};
const close = () => {
  emits('on-close')
};

</script>
