<template>
  <t-dialog
      width="40%"
      v-model:visible="isVisitable"
      :header="props.title"
      :body="props.content"
      attach="body"
      :confirm-on-enter="true"
      :on-confirm="onConfirm"
      :on-close="close"
      :footer="false"
      destroy-on-close>
    <template #body>
      <div>
        <div v-html="props.content"/>
      </div>
    </template>
  </t-dialog>
</template>
<script setup>
import {onMounted, toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  },
  visible: {
    type: Boolean,
    required: true
  }
})

const emits = defineEmits(['on-close'])

const isVisitable = toRef(props,'visible')

const close = () => {
  emits('on-close')
};

onMounted(() => {
  console.log(props.createdDate)
  console.log(props.creatorName)
})

</script>
