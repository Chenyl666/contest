<template>
  <div style="width: 350px">
    <!-- abridgeName 省略中间文本，首尾保留的文本字符 -->
    <t-upload
        v-model="files"
        :abridge-name="[8, 6]"
        theme="file-input"
        placeholder="未选择文件"
        :request-method="requestMethod"
    ></t-upload>
  </div>
</template>
<script setup>
import { ref } from 'vue';
import {upload} from '@/util/upload.util'

const files = ref([]);

const requestMethod = async (file,publicPerm) => {
  await upload(file.raw, file.name, publicPerm)
}
</script>
