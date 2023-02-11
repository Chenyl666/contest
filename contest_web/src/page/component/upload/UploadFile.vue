<template>
  <div style="width: 350px">
    <!-- abridgeName 省略中间文本，首尾保留的文本字符 -->
    <t-upload
        v-model="files"
        action="https://service-bv448zsw-1257786608.gz.apigw.tencentcs.com/api/upload-demo"
        :abridge-name="[8, 6]"
        theme="file-input"
        placeholder="未选择文件"
        @fail="handleFail"
        :request-method="requestMethod"
    ></t-upload>
  </div>
</template>
<script setup>
import { ref } from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import {upload} from '@/util/upload.util'
// import {request} from "@/util/request";
// import {result} from "@/const/request.result";
// import {result} from "@/const/request.result";

const files = ref([]);

const handleFail = ({ file }) => {

  MessagePlugin.error(`文件 ${file.name} 上传失败`);
};

const requestMethod = async (file,publicPerm) => {
  await upload(file.raw, file.name, publicPerm)
  console.log('dddd')
  // console.log(Object.keys(file.raw))
  // data.fileName = file.name
  // data.fileMd5 = 'test'
  // data.publicPerm = true
  // let resultCode
  // let sessionId
  // await request.post('/filesys/upload/request', data, true).then(resp => {
  //   resultCode = resp.data['resultCode']
  //   if(resultCode === result.code.CONTINUE){
  //     sessionId = resp.data['data']
  //   }
  // })
  // if(resultCode === result.code.SUCCESS){
  //   await MessagePlugin.success('上传成功！')
  // }else if(resultCode === result.code.CONTINUE){
  //   await splitUpload(file.raw, sessionId)
  // }

}
</script>
