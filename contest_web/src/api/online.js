import {request} from "@/util/request";

export const initContest = (contestId) => {
    return request.get('/contest/init/'.concat(contestId),true)
}

export const getAnswer = (contestId) => {
    return request.get('/contest/online/answer/paper/'.concat(contestId),true)
}

export const getProgram = (contestId) => {
    return request.get('/contest/online/answer/program/'.concat(contestId),true)
}