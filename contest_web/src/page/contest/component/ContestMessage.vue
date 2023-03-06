<template>
  <t-form ref="form" :rules="rules" :data="formData" :colon="true" @reset="onReset" @submit="onSubmit">
    <t-form-item :label="`竞赛封面`">
      <UploadImage @on-success="onSuccess"/>
      <t-input style="display: none" v-model="formData.contestPicture"/>
    </t-form-item>
    <t-form-item label="竞赛名称" name="subject">
      <t-input v-model="formData.subject" placeholder="请输入竞赛名称"></t-input>
    </t-form-item>
    <t-form-item label="主办方" name="organizeUnit">
      <t-input v-model="formData.organizeUnit" placeholder="请输入竞赛举办方，如有协办方请在竞赛详细中说明"></t-input>
    </t-form-item>
    <t-form-item label="竞赛类别" name="contestTypeId">
      <t-select v-model="formData.contestTypeId" placeholder="请输入竞赛类别">
        <t-option v-for="contestType in data.contestTypeList"
                  :key="contestType.typeId"
                  :label="contestType.typeName"
                  :value="contestType.typeId" />
      </t-select>
    </t-form-item>

    <t-form-item label="竞赛级别" name="contestLevel">
      <t-radio-group v-model="formData.contestLevel">
        <t-radio :value="CONTEST.LEVEL.COLLEGE">校级</t-radio>
        <t-radio :value="CONTEST.LEVEL.CITY">市级</t-radio>
        <t-radio :value="CONTEST.LEVEL.PROVINCE">省级</t-radio>
        <t-radio :value="CONTEST.LEVEL.NATIONAL">国家</t-radio>
        <t-radio :value="CONTEST.LEVEL.INTERNATIONAL">全球</t-radio>
        <t-radio :value="CONTEST.LEVEL.OTHER">其它</t-radio>
      </t-radio-group>
    </t-form-item>

    <t-form-item label="报名时间" name="enrollTimeRange">
      <TimeRangeSelector @change="enrollTimeChange"/>
    </t-form-item>

    <t-form-item label="参赛时间" name="contestTimeRange">
      <TimeRangeSelector @change="contestTimeChange"/>
    </t-form-item>

    <t-form-item name="description" label="竞赛详细">
      <MyEditor @content-change="(value) => formData.description = value" :content="formData.description"/>
    </t-form-item>

    <t-form-item style="margin-top: 10em;margin-bottom: 5em">
        <t-button size="large" block theme="primary" type="submit">下一步</t-button>
    </t-form-item>
  </t-form>
</template>
<script setup>
import {reactive, onMounted, toRef} from 'vue';
import { MessagePlugin } from 'tdesign-vue-next';
import UploadImage from "@/page/component/upload/UploadImage";
import {store} from "@/store";
import TimeRangeSelector from "@/page/component/time/TimeRangeSelector";
import {CONTEST} from '@/common/contest'
import {defineEmits} from "vue";
import {mutationName} from "@/store/mutations/const.name";
import {getContestTypeList} from "@/api/contest";
import {result} from "@/common/request.result";
import MyEditor from "@/page/component/editor/MyEditor";
const emits = defineEmits(['next-step','last-page'])

const formData = reactive({
  contestPicture: '',
  subject: '',
  contestTypeId: null,
  contestLevel: null,
  enrollTimeRange: [],
  contestTimeRange: [],
  description: '',
  organizeUnit: ''
});

const data = reactive({
  contestTypeList: [],
  textarea: {
    height: 500
  }
})

const validateTimeRange = () => {
  let enrollEndTime = new Date(formData.enrollTimeRange[1]);
  let contestStartTime = new Date(formData.contestTimeRange[0]);
  return enrollEndTime < contestStartTime;
}

const rules = {
  subject: [
      {required: true, message: '请输入竞赛名称',type: 'error'},
      {max: 100,message: '竞赛名称最多50个字',type: 'error'}
  ],
  contestTypeId: [{required: true, message: '请选择竞赛类别', type: 'error'}],
  contestLevel: [{required: true, message: '请选择竞赛级别', type: 'error'}],
  enrollTimeRange: [{required: true, message: '请选择报名时间', type: 'error'}],
  contestTimeRange: [
      {required: true, message: '请选择比赛时间', type: 'error'},
      {validator: validateTimeRange, message: '比赛开始时间必须是在报名结束时间之后', type: 'error'}
  ],
  description: [{required: true, message: '请输入竞赛详细',type: 'error'}],
  organizeUnit: [{required: true, message: '请输入举办方',type: 'error'}]
}

const onReset = () => {
  MessagePlugin.success('重置成功');
};

const onSubmit = ({ validateResult, e }) => {
  store.commit(mutationName.CREATED_CONTEST_PAGE.SET_CONTEST_MESSAGE,formData)
  e.preventDefault()
  if(validateResult === true){
    emits('next-step')
  }
};

const onSuccess = (url) => {
  formData.contestPicture = url
}

const enrollTimeChange = (timeRange) => {
  formData.enrollTimeRange[0] = timeRange[0]
  formData.enrollTimeRange[1] = timeRange[1]
}

const contestTimeChange = (timeRange) => {
  formData.contestTimeRange[0] = timeRange[0]
  formData.contestTimeRange[1] = timeRange[1]
}

const supplementContestMessage = (contestMessage) => {
  formData.description = contestMessage.description
  formData.contestTimeRange[0] = contestMessage.contestTimeRange[0]
  formData.contestTimeRange[1] = contestMessage.contestTimeRange[1]
  formData.contestTypeId = contestMessage.contestTypeId
  formData.contestLevel = contestMessage.contestLevel
  formData.contestPicture = contestMessage.contestPicture
  formData.enrollTimeRange[0] = contestMessage.enrollTimeRange[0]
  formData.enrollTimeRange[1] = contestMessage.enrollTimeRange[1]
  formData.organizeUnit = contestMessage.organizeUnit
  formData.subject = contestMessage.subject
}

onMounted(() => {
  if(store.state.createdContestPage.contestMessage !== undefined){
    supplementContestMessage(store.state.createdContestPage.contestMessage)
  }
  getContestTypeList().then(resp => {
    if(resp.data['resultCode'] === result.code.SUCCESS){
      data.contestTypeList = toRef(resp.data,'data')
    }
  })
})


</script>
<style scoped>
  .tdesign-demo__select-input-ul-single {
    display: flex;
    flex-direction: column;
    padding: 0;
    gap: 2px;
  }
  .tdesign-demo__select-input-ul-single > li {
    display: block;
    border-radius: 3px;
    line-height: 22px;
    cursor: pointer;
    padding: 3px 8px;
    color: var(--td-text-color-primary);
    transition: background-color 0.2s linear;
    white-space: nowrap;
    word-wrap: normal;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .tdesign-demo__select-input-ul-single > li:hover {
    background-color: var(--td-bg-color-container-hover);
  }
</style>