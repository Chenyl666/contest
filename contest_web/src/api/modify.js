import {request} from "@/util/request";
import {result} from "@/common/request.result";

export const sendModifyEmailCode = async (userEmail) => {
    let requestResult = {
        isSuccess: false,
        msg: '系统繁忙！'
    }
    await request.get('/modify/email/code/'+userEmail).then(resp => {
        if(resp.status === 200){
            if(resp.data['resultCode'] === result.code.SUCCESS){
                requestResult.isSuccess = true
            }else{
                requestResult.isSuccess = false
                requestResult.msg = resp.data['message']
            }
        }
    })
    return requestResult
}

export const saveModifyDetail = async (modifyPwdDto) => {
    let requestResult = {
        isSuccess: false,
        msg: '系统繁忙！'
    }
    await request.post('/modify/save',modifyPwdDto).then(resp => {
        if(resp.status !== 200){
            return
        }
        if(resp.data['resultCode'] === result.code.SUCCESS){
            requestResult.isSuccess = true
            requestResult.msg = '操作成功！'
        }else{
            requestResult.msg = resp.data['message']
        }
    })
    return requestResult
}