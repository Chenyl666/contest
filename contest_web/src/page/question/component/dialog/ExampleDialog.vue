<template>
  <t-space>
    <t-dialog
        v-model:visible="visible"
        :header="`请输入测试用例`"
        attach="body"
        :on-cancel="onCancel"
        :on-esc-keydown="onEscKeydown"
        :on-close-btn-click="onCloseBtnClick"
        :on-overlay-click="onOverlayClick"
        :on-close="close"
        :on-confirm="onConfirm"
        destroy-on-close
        top="2px"
        width="80em">
      <template #body>
        <t-textarea v-model="data.text" placeholder="请输入测试用例" :autosize="{minRows: 25}"/>
      </template>
    </t-dialog>
  </t-space>
</template>
<script setup>
import {defineEmits, onMounted, reactive, toRef} from "vue";
import {defineProps} from "vue";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  }
})

const data = reactive({
  text: null
})

const visible = toRef(props,'visible')

const emits = defineEmits(['on-close','on-success'])

const onConfirm = () => {
  emits('on-success',data.text)
  data.text = null
}

const close = () => {
  emits('on-close')
};
const onCancel = () => {
  close()
};
const onEscKeydown = () => {
  close()
};
const onCloseBtnClick = () => {
  close()
};
const onOverlayClick = () => {
  close()
};
onMounted(() => {
  data.text = null
})
</script>
