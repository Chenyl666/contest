import {request} from "@/util/request";
import {globalConfig} from "@/global.config";

export const getContestTypeList = () => {
    return request.get('/contest/enroll/type')
}

export const getContestDetail = () => {
    return request.get('/contest/enroll/get/',true)
}

export const getContestDetailMessageById = async (contestId) => {
    return await request.get('/contest/enroll/message/get/'.concat(contestId),false)
}

export const getContestDetailById = async (contestId) => {
    return await request.get('/contest/enroll/get/'.concat(contestId),true)
}

export const getContestCreatorByContestId = async (contestId) => {
    return await request.get('/contest/enroll/creator/get/'.concat(contestId),true)
}

export const getContestAnsList = (contestId) => {
    return request.get('/contest/online/question/judge/'.concat(contestId),true)
}

export const updateContestDetail = async (contestDetail) => {
    return await request.post('/contest/enroll/update',contestDetail)
}

export const saveContestDetail = (contestDetail) => {
    let data = {
        contestId: null,
        contestSubject: contestDetail.contestMessage.subject,
        contestPrice: contestDetail.enrollMessage.contestPaying.contestPrice,
        requiredContestPaying: contestDetail.enrollMessage.contestPaying.requiredPayingForEnroll,
        contestDescription: contestDetail.contestMessage.description,
        enrollStartTime: contestDetail.contestMessage.enrollTimeRange[0],
        enrollEndTime: contestDetail.contestMessage.enrollTimeRange[1],
        contestStartTime: contestDetail.contestMessage.contestTimeRange[0],
        contestEndTime: contestDetail.contestMessage.contestTimeRange[1],
        contestStatus: null,
        createdBy: null,
        contestTypeId: contestDetail.contestMessage.contestTypeId,
        contestLevel: contestDetail.contestMessage.contestLevel,
        contestPicture: contestDetail.contestMessage.contestPicture,
        groupingContest: contestDetail.enrollMessage.contestGrouping.isGroupContest,
        groupingMaxNum: contestDetail.enrollMessage.contestGrouping.groupLimit[1],
        groupingMinNum: contestDetail.enrollMessage.contestGrouping.groupLimit[0],
        autoPrise: contestDetail.enrollMessage.contestAward.autoPrise,
        usePercent: contestDetail.enrollMessage.contestAward.usePercent,
        contestPriseDistributes: contestDetail.enrollMessage.contestAward.priseList,
        organizeUnit: contestDetail.contestMessage.organizeUnit
    }
    return request.post('contest/enroll/save',data,true)
}

export const getContestTypeByContestId = (contestId) => {
    return request.get('/contest/enroll/type/'.concat(contestId))
}

export const importContestQuestion = (contestId,questionRepoId) => {
    return request.post('/contest/enroll/question/import',{contestId,questionRepoId},true)
}

export const getUserEnrollSituation = (contestId) => {
    return request.get('/contest/enroll/situation/'.concat(contestId),true)
}

export const getOrganizerEnrollList = () => {
    return request.get('/contest/enroll/organizer/get',true)
}

export const getEnrollMessageList = (contestId) => {
    return request.get('/contest/enroll/enrollList/'.concat(contestId),true)
}

export const deleteContestEnrollById = (enrollId) => {
    return request.delete('/contest/enroll/delete/'.concat(enrollId),true)
}

export const judgeAnswer = (answerId,score) => {
    return request.postWithForm('/contest/online/judge',{answerId,score},true)
}

export const autoJudge = (contestId) => {
    return request.postWithForm('/contest/online/judge/auto',{contestId},true)
}

export const exportResultByContestId = (contestId) => {
    return request.postWithForm('/contest/result/generate',{contestId},true)
}

export const getContestResultListByContestId = (contestId) => {
    return request.get('/contest/result/list/'.concat(contestId),true)
}

export const publishContestResult = (contestId) => {
    return request.postWithForm("/contest/result/publish",{contestId},true)
}

export const exportContestResultAsExcel = (contestId) => {
    window.location.href = globalConfig.requestConfig.baseURL + '/contest/result/excel/'.concat(contestId)
}

export const getContestUserResult = (contestId) => {
    return request.get('/contest/result/get/'.concat(contestId),true)
}