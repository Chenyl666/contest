import {StringUtil} from "@/util/string.util";
import {checkingEmailRegisteredOrNot, checkingUserIdRegisteredOrNot} from "@/api/register";

const userIdRegex = /^\w{8,16}$/
const emailRegex = /^\w+@\w+\.\w+$/
const digitalRegex = /^.*\d+.*$/
const alphaRegex = /^.*[a-zA-Z]+.*$/
const punctuationRegex = /^.*[^\da-zA-Z]+.*$/

export const emailChecking = async (userEmail) => {
    let checkingResult = {
        status: undefined,
        msg: ''
    }
    if (StringUtil.isEmpty(userEmail)) {
        checkingResult.msg = '邮箱不能为空'
        checkingResult.status = 'error'
        return checkingResult
    }
    if (!emailRegex.exec(userEmail)) {
        checkingResult.status = 'error'
        checkingResult.msg = '邮箱格式不正确'
        return checkingResult
    }
    await checkingEmailRegisteredOrNot(userEmail).then(r => {
        if (r === true) {
            checkingResult.status = 'error'
            checkingResult.msg = '该邮箱已被注册'
        } else {
            checkingResult.status = undefined
            checkingResult.msg = ''
        }
    })
    return checkingResult
}

export const userIdChecking = async (userId) => {
    let checkingResult = {
        status: undefined,
        msg: ''
    }
    if(StringUtil.isEmpty(userId)){
        checkingResult.status = 'error'
        checkingResult.msg = '用户名不能为空'
        return checkingResult
    }
    if(!userIdRegex.exec(userId)){
        checkingResult.status = 'error'
        checkingResult.msg = '用户名必须是8到16个字符，并且只能包含字母或数字'
        return checkingResult
    }
    await checkingUserIdRegisteredOrNot(userId).then(r => {
        if(r){
            checkingResult.status = 'error'
            checkingResult.msg = '该用户id已被注册'
        }
    })
    return checkingResult
}

export const passwordChecking = async (password) => {
    let checkingResult = {
        status: undefined,
        msg: ''
    }
    if(StringUtil.isEmpty(password)) {
        checkingResult.status = 'error'
        checkingResult.msg = '密码不能为空'
        return checkingResult
    }
    if(password.length < 8){
        checkingResult.status = 'error'
        checkingResult.msg = '密码长度不能少于8个字符'
        return checkingResult
    }
    let cnt = 0
    if(digitalRegex.exec(password)){
        cnt++
    }
    if(alphaRegex.exec(password)){
        cnt++
    }
    if(punctuationRegex.exec(password)){
        cnt++
    }
    if(cnt<2){
        checkingResult.status = 'error'
        checkingResult.msg = '密码至少包含字母、数字、标点中的两两种'
    }
    return checkingResult
}

export const emailCodeChecking = async (emailCode) => {
    let checkingResult = {
        status: undefined,
        msg: ''
    }
    if(emailCode === '' || emailCode === undefined ){
        checkingResult.status = 'error'
        checkingResult.msg = '验证码不能为空'
    }
    return checkingResult
}