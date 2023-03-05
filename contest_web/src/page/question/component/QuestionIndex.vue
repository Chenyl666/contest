<template>
  <t-space direction="vertical" style="margin-top: 1em;width: 100%;">
    <t-collapse :default-value="[1]">
      <t-collapse-panel header="题目">
        <t-collapse default-expand-all>
          <t-collapse-panel :key="index" v-for="(questionIndexList,key,index) in list" class="questionBigTitle" :header="questionTypeNameMap[key]">
            <t-list-item
                :class="{questionIndexSelected: data.selected === questionIndex.questionId}"
                @click="onSelected(questionIndex)"
                :key="index" v-for="(questionIndex,index) in questionIndexList"
                class="questionIndex"
                style="cursor: pointer">
              <span style="font-size: 13px">{{index+1}}. {{toPrefixStr(questionIndex.questionContent)}}</span>
              <template #action>
                <t-button @click="ShowDeleteConfirmDialog(questionIndex)" size="small" theme="default" variant="text" ghost>X</t-button>
              </template>
            </t-list-item>
          </t-collapse-panel>
        </t-collapse>
      </t-collapse-panel>
    </t-collapse>
  </t-space>
  <ConfirmDialog :content="data.dialog.confirmDeleteDialog.content"
                 :is-visible="data.dialog.confirmDeleteDialog.visitable"
                 :title="data.dialog.confirmDeleteDialog.title"
                 @on-confirm="confirmDeleteQuestion"
                 @on-close="data.dialog.confirmDeleteDialog.visitable = false"/>
</template>

<script setup lang="jsx">
import {reactive, toRef} from "vue";
import {defineProps} from "vue";
import {StringUtil} from "@/util/string.util";
import {defineEmits} from "vue";
import {defineExpose} from "vue";
import ConfirmDialog from "@/page/component/dialog/ConfirmDialog";
import {deleteQuestionById} from "@/api/question";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";

const emits = defineEmits(['load-question'])

const clearIndexHighLight = () => {
  data.selected = null
}

defineExpose({clearIndexHighLight})

const props = defineProps({
  questionIndexList: {
    type: Array,
    default: null
  }
})

const data = reactive({
  selected: null,
  dialog: {
    confirmDeleteDialog: {
      visitable: false,
      content: '确定要删除改题目吗？',
      title: '提示',
      questionId: null
    }
  }
})

const onSelected = (questionIndex) => {
  data.selected = questionIndex.questionId
  emits('load-question',data.selected)
}

const questionTypeNameMap = reactive({
  SINGLE_OPTION_QUESTION: '单选题',
  SUPPLEMENT_QUESTION: '填空题',
  JUDGE_QUESTION: '判断题',
  ANSWER_QUESTION: '问答题',
  PROGRAMMING_QUESTION: '编程题'
})

const list = toRef(props,'questionIndexList')

const toPrefixStr = (str) => {
  return StringUtil.getStringPrefix(str,10)
}

const ShowDeleteConfirmDialog = (questionIndex) => {
  data.dialog.confirmDeleteDialog.questionId = questionIndex.questionId
  data.dialog.confirmDeleteDialog.visitable = true
}

const confirmDeleteQuestion = () => {
  deleteQuestionById(data.dialog.confirmDeleteDialog.questionId).then(resp => {
    if(resp.data['resultCode'] === result.code.SUCCESS){
      emits('onload-question-index')
      MessagePlugin.success("删除成功！")
    }else{
      MessagePlugin.error('系统繁忙！')
    }
    data.dialog.confirmDeleteDialog.visitable = false
  })
}


</script>

<style lang="less">
.accordion-demo {
  background-color: #f9f9f9;
  background-size: cover;
}
.button-area {
  margin-top: 20px;
  display: flex;
  align-items: center;
  background-size: cover;
}
.questionBigTitle{
  width: 100%;
  background-size: cover;
}
.questionIndex{
  width: 100%;
  margin-left: -2em;
  overflow: hidden;
}
.questionIndex:hover{
  background-color: #a6a6a6;
}
.questionIndexSelected{
  color: #2c9fe5;
  font-weight: bold;
}
</style>
