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



export const deleteQuestionRepo = (questionRepoId) => {
    return request.delete('/contest/question/repo/delete/'.concat(questionRepoId),true)
}

export const deleteQuestionTag = (questionTagId) => {
    return request.delete('/contest/question/tag/delete/'.concat(questionTagId),true)
}