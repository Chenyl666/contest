<template>
  <div style="width: 350px">
    <!-- abridgeName 省略中间文本，首尾保留的文本字符 -->
    <t-upload
        :show-upload-progress="false"
        :request-method="requestMethod"
        theme="file"
        placeholder="未选择文件"
    ></t-upload>
  </div>
</template>
<script setup>
import {defineEmits} from 'vue';
import {uploadSimpleFile} from "@/api/file_upload.";

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
