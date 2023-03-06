import {request} from "@/util/request";
import {result} from "@/common/request.result";

export const addQuestionTag = (questionTagDto) => {
    return request.post('/contest/question/tag/add',questionTagDto,true)
}

export const getQuestionTag = (tagList) => {
    return request.get('/contest/question/tag/get',true).then(resp => {
        if (resp.data['resultCode'] === result.code.SUCCESS) {
            tagList.length = 0
            if (resp.data['data'] !== null) {
                if (resp.data['data'].length === 0) {
                    addQuestionTag({questionTagName: '默认'}).then(r => {
                        tagList.push({questionTagName: '默认',questionTagId: r.data.data})
                    })
                }else{
                    for (let i = 0; i < resp.data['data'].length; i++) {
                        tagList.push(resp.data['data'][i])
                    }
                }
            }
        }
    })
}

export const getQuestionRepo = (tagId,questionRepoList) => {
    return request.get('/contest/question/repo/get/'.concat(tagId),true).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
            questionRepoList.length = 0
            if(resp.data['data'].length !== null && resp.data['data'].length !== 0){
                for(let i=0;i<resp.data['data'].length;i++){
                    questionRepoList.push(resp.data['data'][i])
                }
            }
        }
    })
}

export const addQuestionRepo = (questionRepoDto) => {
    return request.post('/contest/question/repo/add',questionRepoDto,true)
}

export const updateQuestionRepoNameById = (questionRepoDto) => {
    return request.post('/contest/question/repo/update/name',questionRepoDto,true);
}

export const deleteQuestionRepo = (questionRepoId) => {
    return request.delete('/contest/question/repo/delete/'.concat(questionRepoId),true)
}

export const getQuestionRepoById = (questionRepoId) => {
    return request.get('/contest/question/repo/msg/'.concat(questionRepoId),true)
}

export const deleteQuestionTag = (questionTagId) => {
    return request.delete('/contest/question/tag/delete/'.concat(questionTagId),true)
}

export const addQuestion = (questionDto) => {
    return request.post('/contest/question/save',questionDto,true)
}

export const getQuestionIndex = (questionRepoId) => {
    return request.get('/contest/question/index/'.concat(questionRepoId),true)
}

export const getQuestionById = (questionId) => {
    return request.get('/contest/question/'.concat(questionId))
}

export const deleteQuestionById = (questionId) => {
    return request.delete('/contest/question/delete/'.concat(questionId),true)
}

export const saveQuestionProgram = (questionProgramDto) => {
    return request.post('/contest/question/program/save',questionProgramDto,true)
}

export const getQuestionProgramIndex = (questionRepoId) => {
    return request.get('/contest/question/program/index/'.concat(questionRepoId))
}

export const getQuestionProgramById = (questionId) => {
    return request.get('/contest/question/program/get/'.concat(questionId))
}

export const deleteProgramExample = (questionId,exampleNumber) => {
    return request.post('/contest/question/program/example/delete',{questionId,exampleNumber},true)
}

export const getQuestionTagList = () => {
    return request.get('/contest/question/repo/list',true)
}