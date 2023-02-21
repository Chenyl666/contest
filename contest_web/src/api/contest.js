import {request} from "@/util/request";

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
    return await request.get('/contest/enroll/creator/get/'.concat(contestId))
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