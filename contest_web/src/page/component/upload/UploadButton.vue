<template>
  <div>
    <!-- abridgeName 省略中间文本，首尾保留的文本字符 -->
    <t-upload
        :show-upload-progress="false"
        :request-method="requestMethod"
        theme="custom"
        placeholder="未选择文件"
    >
      <t-button :variant="props.variant" :theme="props.theme">{{props.value}}</t-button>
    </t-upload>
  </div>
</template>
<script setup>
import {defineEmits} from 'vue';
import {uploadSimpleFile} from "@/api/file_upload.";
import {defineProps} from "vue";

const props = defineProps({
  value: {
    required: true
  },
  theme: {
    type: String,
    default: 'primary'
  },
  variant: {
    type: String
  },
})

const emit = defineEmits(['on-success'])

const requestMethod = async (file) => {
  let url = ''
  await uploadSimpleFile(file.raw, file.name, true, true).then(res => {
    url = res
  })
  return new Promise((resolve) => {
    emit('on-success',url)
    resolve({
      status: 'success',
      response: {url: url}
    });
  });
}
</script>
