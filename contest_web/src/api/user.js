import {request} from "@/util/request";

export const saveUserDetail = (user) => {
    return request.post('/user/save',user,true)
}

export const getUserDetail = () => {
    return request.get('/user/get/detail',true)
}

export const getUserDetailById = (userId) => {
    return request.get('/user/get/detail/'.concat(userId),true)
}

