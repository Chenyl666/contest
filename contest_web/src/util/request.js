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

    static postFile(url, data, usingToken = false){
        if(usingToken === true && (store.state.token === null || store.state.token === 'none')){
            router.push('/login').then()
            return;
        }
        return axios.post(url, data,{
            baseURL: globalConfig.requestConfig.baseURL,
            responseType: "json",
            headers: {
                'Content-Type': 'multipart/form-data',
                'token': store.state.token
            },
        })
    }

    static delete(url, usingToken = false,header){
        if(usingToken === true && (store.state.token === null || store.state.token === 'none')){
            router.push('/login').then()
            return;
        }
        if(header == null){
            header = {}
        }
        header['token'] = store.state.token
        return axios.delete(url,{
            baseURL: globalConfig.requestConfig.baseURL,
            responseType: "json",
            headers: header
        })
    }
}