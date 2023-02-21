import {request} from "@/util/request";
import {result} from "@/common/request.result";

export const addQuestionTag = (questionTagDto) => {
    return request.post('/contest/question/tag/add',questionTagDto,true)
}

export const getQuestionTag = (tagList) => {
    return request.get('/contest/question/tag/get',null,true).then(resp => {
        tagList.length = 0
        tagList.push({questionTagId: '0',questionTagName: '默认'})
        if(resp.data['resultCode'] === result.code.SUCCESS){
            console.log(resp.data.data)
            if(resp.data['data'].length !== null && resp.data['data'].length !== 0){
                for(let i=0;i<resp.data['data'].length;i++){
                    console.log(i)
                    tagList.push(resp.data['data'][i])
                }
            }
        }
    })
}