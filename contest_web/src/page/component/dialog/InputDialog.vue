<template>
    <t-dialog
        v-model:visible="isVisitable"
        :header="props.title"
        :body="props.content"
        attach="body"
        :confirm-on-enter="true"
        :on-close="close"
        :on-confirm="onConfirmAnother"
        destroy-on-close>
      <template #body>
        <t-input :maxcharacter="props.maxCharacter" :placeholder="props.placeholder" v-model="inputData"></t-input>
      </template>
    </t-dialog>
</template>
<script setup>
import {ref, toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";

const props = defineProps({
  title: {
    type: String,
    required: true
  },
  content: {
    type: String
  },
  visible: {
    type: Boolean,
    required: true
  },
  placeholder: {
    type: String
  },
  maxCharacter: {
    type: Number
  }
})

const emits = defineEmits(['on-close','on-confirm'])

const isVisitable = toRef(props,'visible')

const inputData = ref('')

const onConfirmAnother = () => {
  emits('on-confirm',inputData.value)
  inputData.value = ''
};
const close = () => {
  emits('on-close')
};

</script>
