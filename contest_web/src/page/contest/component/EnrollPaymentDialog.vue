<template>
  <t-dialog
      width="70%"
      style="height: 70%"
      v-model:visible="isVisitable"
      :header="props.title"
      attach="body"
      :confirm-on-enter="true"
      :on-close="close"
      :on-confirm="onConfirmAnother"
      destroy-on-close>
    <template #body>

    </template>
  </t-dialog>
</template>
<script setup>
import {toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  visible: {
    type: Boolean,
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
