import {mutationName} from "@/store/mutation/const.name";
import cookies from 'js-cookie'
import {store} from '@/store'
import {request} from "@/util/request";

export const saveToken = (token) => {
    store.commit(mutationName.SET_TOKEN,token)
    cookies.set('token',token,{ expires: 2 })
    request.get('/login/token/user/get').then(resp => {
        store.commit(mutationName.SET_USER,resp.data.data)
    })
}

export const removeToken = () => {
    try {
        store.commit(mutationName.SET_TOKEN, 'none')
        store.commit(mutationName.SET_USER, undefined)
        cookies.set('token', 'none')
        return true
    }catch (e){
        return false
    }

}