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
        :on-confirm="onConfirm">
      <template #body>
        <t-form>
          <t-form-item :label="`标题`">
            <t-input v-model="data.title" style="width: 50em"/>
          </t-form-item>
          <t-form-item :label="`内容`">
            <MyEditor height="30em" width="50em" :content="data.content" @content-change="v => data.content = v"/>
          </t-form-item>
        </t-form>
      </template>
    </t-dialog>
  </div>
</template>
<script setup>
import {reactive, toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";
import MyEditor from "@/page/component/editor/MyEditor";
import {MessagePlugin} from "tdesign-vue-next";
import {StringUtil} from "@/util/string.util";
import {publishNotify} from "@/api/notify";
import router from "@/router/router";
import {result} from "@/common/request.result";

const props = defineProps({
  visitable: {
    type: Boolean,
    required: true
  }
})
const data = reactive({
  content: null,
  title: null
})
const emits = defineEmits(['on-confirm','on-close'])

const visibleBody = toRef(props,'visitable');

const onClose = () => {
  emits('on-close')
}

const onConfirm = () => {
  if(data.title === '' || data.title === null){
    MessagePlugin.error('请输入标题！')
    return
  }
  if(data.content === null || StringUtil.parseFromHtml(data.content) === ''){
    MessagePlugin.error('请输入内容！')
    return
  }
  publishNotify({
    contestId: router.currentRoute.value.params.contestId,
    title: data.title,
    content: data.content
  }).then(resp => {
    if(resp.data.resultCode === result.code.SUCCESS){
      MessagePlugin.success('发布成功！')
      emits('on-confirm')
      onClose()
    }else{
      MessagePlugin.error('系统繁忙！')
    }
  }).catch(() => {
    MessagePlugin.error('系统繁忙！')
  })

}

</script>
<style scoped>
</style>
