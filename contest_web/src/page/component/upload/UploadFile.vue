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
import {request} from "@/util/request";
import {result} from "@/const/request.result";

const files = ref([]);

const data = {
  fileName: '',
  fileMd5: '',
  publicPerm: true
}

const handleFail = ({ file }) => {

  MessagePlugin.error(`文件 ${file.name} 上传失败`);
};

const requestMethod = async (file) => {
  data.fileName = file.name
  data.fileMd5 = 'test'
  data.publicPerm = true
  let resultCode
  let sessionId
  await request.post('/filesys/upload/request', data, true).then(resp => {
    resultCode = resp.data['resultCode']
    if(resultCode === result.code.CONTINUE){
      sessionId = resp.data['data']
    }
  })
  if(resultCode === result.code.SUCCESS){
    await MessagePlugin.success('上传成功！')
  }else if(resultCode === result.code.CONTINUE){
    let data = {
      sessionId: sessionId,
      isLast: true,
      filePiece: file.raw
    }
    await request.postWithForm('/filesys/upload/file',data,true).then(async resp => {
      if (resp.data['resultCode'] === result.code.SUCCESS) {
        await MessagePlugin.success('上传成功！')
      }
    })
  }

}
</script>
