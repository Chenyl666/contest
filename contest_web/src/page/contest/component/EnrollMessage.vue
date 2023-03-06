<template>
  <t-form ref="form" :data="formData" :rules="rules" :label-width="150" :colon="true" @submit="onSubmit">
    <t-form-item name="enableGroup" label="是否可以组队">
      <t-radio-group v-model="formData.contestGrouping.isGroupContest">
        <t-radio :value="true">是</t-radio>
        <t-radio :value="false">否</t-radio>
      </t-radio-group>
    </t-form-item>

    <t-form-item v-if="formData.contestGrouping.isGroupContest" name="groupLimit" label="队伍人数">
      <t-range-input placeholder="请输入队伍人数区间" v-model="formData.contestGrouping.groupLimit"/>
    </t-form-item>

    <t-form-item name="requiredPayingForEnroll" label="是否通过平台收费">
      <t-radio-group v-model="formData.contestPaying.requiredPayingForEnroll">
        <t-radio :value="true">是</t-radio>
        <t-radio :value="false">否</t-radio>
      </t-radio-group>
    </t-form-item>

    <t-form-item name="contestPrice" v-if="formData.contestPaying.requiredPayingForEnroll" label="竞赛费用">
      <t-input type="number" v-model="formData.contestPaying.contestPrice" placeholder="提现时将扣除5%的使用费用"/>
    </t-form-item>

    <t-form-item name="requiredPayingForEnroll" label="是否自动评奖">
      <t-radio-group v-model="formData.contestAward.autoPrise">
        <t-radio :value="true">是</t-radio>
        <t-radio :value="false">否</t-radio>
      </t-radio-group>
    </t-form-item>

    <t-form-item v-if="formData.contestAward.autoPrise" name="usingPercent" label="采用人数比例评奖">
      <t-radio-group v-model="formData.contestAward.usePercent">
        <t-radio :value="true">是</t-radio>
        <t-radio :value="false">否</t-radio>
      </t-radio-group>
    </t-form-item>

    <t-form-item name="prize0" v-if="formData.contestAward.autoPrise" :label="formData.contestAward.usePercent?`特等奖比例`:`特等奖人数`">
      <t-input type="text" v-model="formData.contestAward.priseList[0].value"/>
      <div>{{formData.contestAward.usePercent?'%':''}}</div>
    </t-form-item>

    <t-form-item name="prize1" v-if="formData.contestAward.autoPrise" :label="formData.contestAward.usePercent?`一等奖比例`:`一等奖人数`">
      <t-input type="text" v-model="formData.contestAward.priseList[1].value"/>
      <div>{{formData.contestAward.usePercent?'%':''}}</div>
    </t-form-item>

    <t-form-item name="prize2" v-if="formData.contestAward.autoPrise" :label="formData.contestAward.usePercent?`二等奖比例`:`二等奖人数`">
      <t-input type="text" v-model="formData.contestAward.priseList[2].value"/>
      <div>{{formData.contestAward.usePercent?'%':''}}</div>
    </t-form-item>

    <t-form-item name="prize3" v-if="formData.contestAward.autoPrise" :label="formData.contestAward.usePercent?`三等奖比例`:`三等奖人数`">
      <t-input type="text" v-model="formData.contestAward.priseList[3].value"/>
      <div>{{formData.contestAward.usePercent?'%':''}}</div>
    </t-form-item>

    <t-form-item name="prize4" v-if="formData.contestAward.autoPrise" :label="formData.contestAward.usePercent?`优秀奖比例`:`优秀奖人数`">
      <t-input type="text" v-model="formData.contestAward.priseList[4].value"/>
      <div>{{formData.contestAward.usePercent?'%':''}}</div>
    </t-form-item>

    <t-form-item style="margin-bottom: 5em" name="submit">
      <t-button @click="toLastStep" size="large" style="margin-right: 1em" block theme="primary">上一步</t-button>
      <t-button size="large" block theme="success" type="submit">完成</t-button>
    </t-form-item>
  </t-form>
</template>
<script setup>
import {onMounted, reactive} from 'vue';
import {defineEmits} from "vue";
import {store} from "@/store";
import {mutationName} from "@/store/mutations/const.name";
import {saveContestDetail} from "@/api/contest";
import {result} from "@/common/request.result";

const emits = defineEmits(['last-step'])

const formData = reactive({
  contestGrouping: {
    isGroupContest: true,
    groupLimit: [],
  },
  contestPaying: {
    requiredPayingForEnroll: true,
    contestPrice: null
  },
  contestAward: {
    autoPrise: true,
    usePercent: true,
    priseList: [
      {
        level: 0,
        value: null
      },
      {
        level: 1,
        value: null
      },
      {
        level: 2,
        value: null
      },
      {
        level: 3,
        value: null
      },
      {
        level: 4,
        value: null
      },
    ]
  }
});

const validateGroupLimit = () => {
  if(!formData.contestGrouping.isGroupContest){
    return true
  }
  let groupLimit = formData.contestGrouping.groupLimit;
  if(groupLimit[0] === undefined || groupLimit[1] === undefined){
    return false
  }
  return groupLimit[0] < groupLimit[1]
}

const validatePrize = (index) => {
  if(formData.contestAward.autoPrise === false){
    return true
  }
  if(formData.contestAward.priseList[index].value === null){
    return false
  }
  if(formData.contestAward.usePercent){
    return (
        parseInt(formData.contestAward.priseList[index].value) >= 0
        && parseInt(formData.contestAward.priseList[index].value) <= 100
    )
  }else{
    console.log(formData.contestAward.priseList[index])
    return parseInt(formData.contestAward.priseList[index].value) >= 0
  }
}

const rules = {
  groupLimit: [{validator: validateGroupLimit,message: '填写组队信息',type: 'error'}],
  prize0: [{validator: () => {return validatePrize(0)},message: '请输入正确的评奖比例或人数',type: 'error'}],
  prize1: [{validator: () => {return validatePrize(1)},message: '请输入正确的评奖比例或人数',type: 'error'}],
  prize2: [{validator: () => {return validatePrize(2)},message: '请输入正确的评奖比例或人数',type: 'error'}],
  prize3: [{validator: () => {return validatePrize(3)},message: '请输入正确的评奖比例或人数',type: 'error'}],
  prize4: [{validator: () => {return validatePrize(4)},message: '请输入正确的评奖比例或人数',type: 'error'}],
  contestPrice: [{validator: () => {
    return formData.contestPaying.requiredPayingForEnroll?formData.contestPaying.contestPrice !== null:true
  },message: '请输入竞赛费用',type: 'error'}]
}

const toLastStep = () => {
  store.commit(mutationName.CREATED_CONTEST_PAGE.SET_ENROLL_MESSAGE, formData)
  emits('last-step')
}

const toNextStep = () => {
  emits('next-step')
}

const onSubmit = ({validateResult,e}) => {
  // alert(store.state.createdContestPage.contestMessage['organizeUnit'])
  e.preventDefault()
  if (validateResult === true) {
    store.commit(mutationName.CREATED_CONTEST_PAGE.SET_ENROLL_MESSAGE, formData)
    saveContestDetail(store.state.createdContestPage).then(resp => {
      if(resp.data['resultCode'] === result.code.SUCCESS){
        store.commit(mutationName.CREATED_CONTEST_PAGE.SET_CONTEST_MESSAGE,undefined)
        store.commit(mutationName.CREATED_CONTEST_PAGE.SET_ENROLL_MESSAGE,undefined)
        toNextStep()
      }
    })
  }
};



const supplementEnrollMessage = (enrollMessage) => {
  formData.contestGrouping.isGroupContest = enrollMessage.contestGrouping.isGroupContest
  formData.contestGrouping.groupLimit[0] = enrollMessage.contestGrouping.groupLimit[0]
  formData.contestGrouping.groupLimit[1] = enrollMessage.contestGrouping.groupLimit[1]
  formData.contestPaying.requiredPayingForEnroll = enrollMessage.contestPaying.requiredPayingForEnroll
  formData.contestPaying.contestPrice = enrollMessage.contestPaying.contestPrice
  formData.contestAward.autoPrise = enrollMessage.contestAward.autoPrise
  formData.contestAward.usePercent = enrollMessage.contestAward.usePercent
  for(let i=0;i<enrollMessage.contestAward.priseList.length;i++){
    formData.contestAward.priseList[i] = enrollMessage.contestAward.priseList[i]
  }
}

onMounted(() => {
  if(store.state.createdContestPage.enrollMessage !== undefined){
    supplementEnrollMessage(store.state.createdContestPage.enrollMessage)
  }
})
</script>
