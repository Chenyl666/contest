import {DEV_CONFIG} from "@/config/dev.config";

export const getters = {
    getUserId(state) {
        if(state.userDto === undefined){
            return ''
        }
        return state.userDto.userId
    },
    getUserPic(state) {
        if(state.userDto === undefined){
            return ''
        }
        return DEV_CONFIG.BASE_URL.concat('/user/pic/get/').concat(state.userDto.userId)
    },
    getUserType(state) {
        if(state.userDto === undefined){
            return ''
        }
        return state.userDto.userType
    },
    getUserName(state) {
        if(state.userDto === undefined){
            return ''
        }
        return state.userDto.userName
    }
}