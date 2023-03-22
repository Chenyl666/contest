<template>
  <div class="dialog-attach-wrap">
    <t-dialog
        top="20px"
        width="70em"
        style="height: 50em"
        v-model:visible="visibleBody"
        attach="body"
        header="发布通知"
        destroy-on-close
        @close="onClose"
        :footer="false">
      <template #body>
        <h1>{{title}}</h1>
        <div v-html="content"/>
      </template>
    </t-dialog>
  </div>
</template>
<script setup>
import {toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";

const props = defineProps({
  visitable: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  }
})

const emits = defineEmits(['on-close'])

const visibleBody = toRef(props,'visitable');

const onClose = () => {
  emits('on-close')
}

</script>
<style scoped>
</style>
