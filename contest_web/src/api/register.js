import {request} from "@/util/request";
import {result} from "@/const/request.result";

export const checkingEmailRegisteredOrNot = async (userEmail) => {
    let checkingResult = false
    await request.get('/register/email/exists/'+userEmail).then(resp => {
        checkingResult = (resp.data['resultCode'] !== result.code.SUCCESS);
    })
    return checkingResult
}

export const sendEmailCodeToUserEmail = async (userEmail) => {
    let requestResult = false
    await request.get('/register/email/code/' + userEmail).then(resp => {
        requestResult = (resp.data['resultCode'] === result.code.SUCCESS);
    })
    return requestResult
}

export const checkingUserIdRegisteredOrNot = async (userId) => {
    let checkingResult = false
    await request.get('/register/id/exists/'+userId).then(resp => {
        checkingResult = (resp.data['resultCode'] !== result.code.SUCCESS)
    })
    return checkingResult
}

export const saveUser = async (userRegisterDto) => {
    let response = {
        result: false,
        userEmail: '',
        msg: ''
    }
    await request.post('/register/submit',userRegisterDto).then(resp => {
        if(resp.data['resultCode'] === result.code.SUCCESS){
            response.result = true
            response.userEmail = userRegisterDto.userEmail
        }else{
            response.result = false
            response.msg = resp.data['message']
        }
    }).catch(() => {
        response.result = false
        response.msg = '系统繁忙！'
    })
    return response
}