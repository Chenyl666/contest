<template>
  <t-upload
      :show-upload-progress="false"
      :request-method="requestMethod"
      theme="image"/>
</template>

<script setup>
import {uploadSimpleFile} from "@/api/file_upload.";
import {defineEmits} from "vue";

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

<style scoped>

</style>