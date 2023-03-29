<template>
  <div v-if="formData.contestDetail.contestTypeId === 2 || formData.contestDetail.contestStatus === 'RESULT' || formData.contestDetail.contestStatus === 'PUBLISH'">
<!--    <h2>该比赛为编程类竞赛，无需进行批改</h2>-->
    <ContestResult @publish="publishResult" :contest-result-list="formData.result" :contest-detail="formData.contestDetail"/>
  </div>
  <div v-if="formData.contestDetail.contestTypeId === 1 && formData.contestDetail.contestStatus !== 'RESULT' && formData.contestDetail.contestStatus !== 'PUBLISH'">
    <t-form style="display: table">
      <t-form-item :label="'题目类型'" style="display: table-cell;">
        <t-select v-model="queryWrapper.questionType">
          <t-option label="选择题" :value="'SINGLE_OPTION_QUESTION'"/>
          <t-option label="判断题" :value="'JUDGE_QUESTION'"/>
          <t-option label="填空题" :value="'SUPPLEMENT_QUESTION'"/>
          <t-option label="问答题" :value="'ANSWER_QUESTION'"/>
        </t-select>
      </t-form-item>
      <t-form-item :label="'状态'" style="display: table-cell;">
        <t-select v-model="queryWrapper.hasJudge">
          <t-option label="未批改" :value="false"/>
          <t-option label="已批改" :value="true"/>
        </t-select>
      </t-form-item>
      <t-form-item style="margin-left: -2em">
        <t-button @click="query" theme="success">筛选</t-button>
        <t-button style="margin-left: 2em" @click="reset" theme="primary">重置</t-button>
        <t-button @click="exportResult" style="margin-left: 2em" theme="default">导出成绩</t-button>
      </t-form-item>
    </t-form>
    <p style="margin-left: 1.4em">
      注意：为了竞赛公平以及保护参赛者的隐私，判题时将对参赛者匿名！
      <t-link @click="judgeAuto" theme="success" style="margin-left: 2em">自动判题</t-link>
    </p>
    <div v-if="formData.contestDetail.contestTypeId === 1">
      <t-space direction="vertical">
        <!-- 当数据为空需要占位时，会显示 cellEmptyContent -->
        <t-table
            row-key="index"
            :data="data"
            :columns="columns"
            :stripe="stripe"
            :bordered="bordered"
            :hover="hover"
            :table-layout="tableLayout ? 'auto' : 'fixed'"
            :size="size"
            :pagination="pagination"
            :show-header="showHeader"
            cell-empty-content="-"
            resizable>
          <template #operation="{row}">
            <t-link theme="primary" @click="showAnswerDetail(row)">查看详细</t-link>
          </template>
        </t-table>
      </t-space>
    </div>
    <AnswerDialog :answer="formData.currentAnswer"
                  @on-confirm="onQuestionJudgeConfirm"
                  @on-close="onQuestionJudgeClose"
                  :visitable="formData.currentVisitable"/>
    <t-loading :loading="formData.loading" text="加载中..." fullscreen/>
  </div>
</template>

<script setup lang="jsx">
import {onMounted, reactive, ref} from 'vue';
// import {ErrorCircleFilledIcon, CheckCircleFilledIcon, CloseCircleFilledIcon} from 'tdesign-icons-vue-next';
import {
  autoJudge,
  exportResultByContestId,
  getContestAnsList,
  getContestDetailById,
  getContestResultListByContestId,
  judgeAnswer, publishContestResult
} from "@/api/contest";
import router from "@/router/router";
import AnswerDialog from "@/page/contest/component/detail_page/dialog/AnswerDialog";
import {result} from "@/common/request.result";
import {MessagePlugin} from "tdesign-vue-next";
import {defineEmits} from "vue";
import ContestResult from "@/page/contest/component/detail_page/ContestResult";

const emits = defineEmits(['reload'])

const formData = reactive({
  contestDetail: {},
  allData: [],
  result: [],
  currentAnswer: {
    answerContent: '',
    answerId: '',
    contestId: '',
    hasJudge: false,
    judgeComment: false,
    questionDetailDto: {},
    questionType: '',
    score: 0
  },
  currentVisitable: false,
  loading: false
})

const QUESTION_TYPE = {
  SINGLE_OPTION_QUESTION: '选择题',
  SUPPLEMENT_QUESTION: '填空题',
  JUDGE_QUESTION: '判断题',
  ANSWER_QUESTION: '问答题',
  NULL: '其它'
}

const queryWrapper = reactive({
  questionType: null,
  hasJudge: null
})

const data = reactive([]);
const allData = []

const stripe = ref(true);
const bordered = ref(true);
const hover = ref(false);
const tableLayout = ref(false);
const size = ref('medium');
const showHeader = ref(true);

const columns = ref([
  {colKey: 'answerId', title: '题目序列号', width: '100', align: 'center'},
  {
    colKey: 'questionType',
    title: '题目类型',
    width: '100',
    align: 'center',
    cell: (h, {row}) => {
      return QUESTION_TYPE[row.questionType]
    }
  },
  {colKey: 'score', title: '题目得分', width: '100', align: 'center'},
  {
    colKey: 'hasJudge',
    title: '状态',
    width: '100',
    align: 'center',
    cell: (h, {row}) => {
      if (row.hasJudge) {
        return <span style="color: #0075FF">已批改</span>
      } else {
        return <span style="color: red">未批改</span>
      }
    }
  },
  {colKey: 'operation', title: '操作', width: '100', align: 'center'},
]);

const showAnswerDetail = (row) => {
  formData.currentAnswer = row
  formData.currentVisitable = true
}

const query = () => {
  data.length = 0
  for(let i=0;i<allData.length;i++){
    let qs = true
    if(queryWrapper.questionType !== null){
      qs = qs && queryWrapper.questionType === allData[i].questionType
    }
    if(queryWrapper.hasJudge !== null){
      qs = qs && queryWrapper.hasJudge === allData[i].hasJudge
    }
    if(qs){
      data.push(allData[i])
    }
  }
}

const reset = () => {
  queryWrapper.hasJudge = null
  queryWrapper.questionType = null
}

const onQuestionJudgeConfirm = (answerId, score) => {
  formData.currentVisitable = false
  judgeAnswer(answerId, score).then(resp => {
    if (resp.data.resultCode === result.code.SUCCESS) {
      formData.currentAnswer.score = score
      formData.currentAnswer.hasJudge = true
    }
  })
}

const onQuestionJudgeClose = () => {
  formData.currentVisitable = false
}

const pagination = reactive({
  defaultCurrent: 1,
  defaultPageSize: 5,
  total: 5,
});

const judgeAuto = () => {
  formData.loading = true
  autoJudge(router.currentRoute.value.params.contestId).then(resp => {
    if(resp.data.resultCode === result.code.SUCCESS){
      formData.loading = false
      getContestAnsList(router.currentRoute.value.params.contestId).then(resp => {
        data.length = 0
        allData.length = 0
        let list = resp.data.data
        for (let i = 0; i < list.length; i++) {
          data.push(list[i])
          allData.push(list[i])
        }
        pagination.total = data.length
        pagination.defaultPageSize = 5
      })
    }else{
      formData.loading = false
      MessagePlugin.error('系统繁忙！')
    }
  }).catch(() => {
    formData.loading = false
    MessagePlugin.error('系统繁忙！')
  })
}

const reload = () => {
  emits('reload')
}

const exportResult = () => {
  if(confirm('确定要导出成绩？导出成绩后将无法再修改分数！')){
    exportResultByContestId(formData.contestDetail.contestId).then(resp => {
      if(resp.data.resultCode === result.code.SUCCESS){
        reload()
      }
    })
  }
}

const publishResult = () => {
  if(confirm('确定要发布成绩？发布成绩后所有的参赛者将收到通知！')){
    publishContestResult(formData.contestDetail.contestId).then(resp => {
      if(resp.data.resultCode === result.code.SUCCESS){
        MessagePlugin.success('发布成功')
        reload()
      }else{
        MessagePlugin.error(resp.data.message)
      }
    })
  }
}

onMounted(async () => {
  await getContestDetailById(router.currentRoute.value.params.contestId).then(resp => {
    formData.contestDetail = resp.data.data
  })
  if(formData.contestDetail.contestTypeId === 2 || formData.contestDetail.contestStatus === 'RESULT' || formData.contestDetail.contestStatus === 'PUBLISH'){
    await getContestResultListByContestId(formData.contestDetail.contestId).then(resp => {
      console.log(resp.data.data)
      formData.result = resp.data.data
    })
  }else{
    getContestAnsList(router.currentRoute.value.params.contestId).then(resp => {
      let list = resp.data.data
      for (let i = 0; i < list.length; i++) {
        data.push(list[i])
        allData.push(list[i])
      }
      pagination.total = data.length
      pagination.defaultPageSize = 5
    })
  }
})

</script>
