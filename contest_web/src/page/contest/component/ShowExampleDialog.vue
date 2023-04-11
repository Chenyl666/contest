<template>
    <t-dialog
        v-model:visible="visitable"
        attach="body"
        header="测试用例"
        destroy-on-close
        :on-close="onClose"
        top="2em"
        :on-confirm="onClose">
      <template #body>
        <h4>用例输入</h4>
        <div>
          {{inputUrl}}
        </div>
        <h4>用例输出</h4>
        <div>
          {{outputUrl}}
        </div>
      </template>
    </t-dialog>
</template>
<script setup>
import {defineProps, toRef} from "vue";
import {defineEmits} from "vue";

const props = defineProps({
  visitable: {
    type: Boolean,
    required: true
  },
  inputUrl: {
    type: String,
    required: true
  },
  outputUrl: {
    type: String,
    required: true
  }
})
const emits = defineEmits(['on-close'])
const visitable = toRef(props,'visitable')

const onClose = () => {
  emits('on-close')
}

</script>

<style scoped>
</style>
