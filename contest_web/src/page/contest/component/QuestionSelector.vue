<template>
  <t-dialog
      v-model:visible="isVisitable"
      header="导入题库"
      :body="props.content"
      attach="body"
      :confirm-on-enter="true"
      :on-close="close"
      :on-confirm="onConfirmAnother"
      placement="top"
      top="50px"
      width="40em"
      :footer="false"
      destroy-on-close>
    <template #body>
      <t-collapse default-expand-all>
        <t-collapse-panel header="题库">
          <t-collapse :key="index" v-for="(questionTag,index) in data">
            <t-collapse-panel :header="questionTag.tagName">
              <t-list>
                <t-list-item :key="index" v-for="(questionRepo,index) in questionTag.questionRepoList">
                  <t-link @click="toQuestionRepo(questionRepo.questionRepoId)" theme="primary">{{questionRepo.questionRepoName}}</t-link>
                  <template #action>
                    <t-link @click="importQuestion(questionRepo)" theme="primary" style="margin-left: 16px">导入</t-link>
                  </template>
                </t-list-item>
              </t-list>
            </t-collapse-panel>
          </t-collapse>
        </t-collapse-panel>
      </t-collapse>
      <t-button @click="close" theme="danger" style="float: right;margin-top: 2em;">关闭</t-button>
      <t-button @click="refresh" style="float: right;margin-top: 2em;margin-right: 1em">刷新</t-button>
    </template>
  </t-dialog>
</template>
<script setup>
import {onMounted, reactive, toRef} from 'vue';
import {defineProps} from "vue";
import {defineEmits} from "vue";
import {getQuestionTagList} from "@/api/question";
import router from "@/router/router";

const props = defineProps({
  visible: {
    type: Boolean,
    required: true
  }
})

const data = reactive([])

const emits = defineEmits(['on-close', 'on-confirm','import-repo'])

const isVisitable = toRef(props, 'visible')

const onConfirmAnother = () => {
  emits('on-confirm')
};
const close = () => {
  emits('on-close')
};
const importQuestion = (questionRepo) => {
  emits('import-repo',questionRepo.questionRepoId)
}

const toQuestionRepo = (questionRepoId) => {
  close()
  router.push('/question/repo/detail/'.concat(questionRepoId))
}

const refresh = () => {
  getQuestionTagList().then(resp => {
    data.length = 0
    for (let i = 0; i < resp.data.data.length; i++) {
      data.push(resp.data.data[i])
    }
  })
}

onMounted(() => {
 refresh()
})

</script>
