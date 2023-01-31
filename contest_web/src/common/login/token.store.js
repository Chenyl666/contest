import {mutationName} from "@/store/mutation/const.name";
import cookies from 'js-cookie'
import {store} from '@/store'

export const saveToken = (token) => {
    store.commit(mutationName.SET_TOKEN,token)
    cookies.set('token',token)
}