import axios from "axios";
import {globalConfig} from "@/global.config";
import {store} from "@/store";
import router from "@/router/router";

export class request {
    static get(url,usingToken = false) {
        if(usingToken === true && (store.state.token === null || store.state.token === 'none')){
            router.push('/login').then()
            return;
        }
        return axios.get(url,{
            baseURL: globalConfig.requestConfig.baseURL,
            responseType: "json",
            headers: {'token': store.state.token}
        })
    }
    static post(url, data, usingToken = false,header = null){
        if(usingToken === true && (store.state.token === null || store.state.token === 'none')){
            router.push('/login').then()
            return;
        }
        if(header == null){
            header = {}
        }
        header['token'] = store.state.token
        return axios.post(url, data,{
            baseURL: globalConfig.requestConfig.baseURL,
            responseType: "json",
            headers: header,
        })
    }

    static postWithForm(url, data, usingToken = false, header = null){
        if(usingToken === true && (store.state.token === null || store.state.token === 'none')){
            router.push('/login').then()
            return;
        }
        if(header == null){
            header = {}
        }
        header['Content-Type'] = 'multipart/form-data'
        header['token'] = store.state.token
        return axios.post(url, data,{
            baseURL: globalConfig.requestConfig.baseURL,
            responseType: "json",
            headers: header,
        })
    }
}