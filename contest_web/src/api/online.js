import {request} from "@/util/request";

export const checkContestStatus = (contestId) => {
    return request.postWithForm('/contest/init/check',{contestId},true)
}

export const initContest = (contestId) => {
    return request.get('/contest/init/'.concat(contestId),true)
}

export const getAnswer = (contestId) => {
    return request.get('/contest/online/answer/paper/'.concat(contestId),true)
}

export const getProgram = (contestId) => {
    return request.get('/contest/online/answer/program/'.concat(contestId),true)
}

export const savePaperQuestionAnswer = (ans) => {
    return request.post('/contest/online/answer/paper/save',ans,true)
}

export const setIngStatus = (contestId) => {
    return request.postWithForm('/contest/online/status/update',{contestId,status:1},true)
}

export const setEndStatus = (contestId) => {
    return request.postWithForm('/contest/online/status/update',{contestId,status:2},true)
}

export const submitProgramCode = (answerId,questionId,code,languageType) => {
    return request.post('/contest/program/submit',{answerId, questionId, code, languageType},true)
}
