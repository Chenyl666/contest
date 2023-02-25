<template>
  <t-space>
    <t-dialog
        v-model:visible="visible"
        :header="props.title"
        :body="props.content"
        :confirm-on-enter="true"
        :on-cancel="onCancel"
        :on-esc-keydown="onClose"
        :on-close-btn-click="onCancel"
        :on-overlay-click="onOverlay"
        :on-close="onClose"
        :on-confirm="onConfirm"
    />
  </t-space>
</template>
<script setup>
import {toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";

let props = defineProps({
  title: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  },
  isVisible: {
    type: Boolean,
    required: true
  }
});
const visible = toRef(props,'isVisible');
const emits = defineEmits(['on-close','on-cancel','on-confirm','on-overlay-click'])

const onCancel = () => {
  console.log('cancel')
}

const onConfirm = () => {
  emits('on-confirm')
}

const onClose = () => {
  emits('on-close')
}

const onOverlay = () => {
  console.log('蒙版')
}

</script>
