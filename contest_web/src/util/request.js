import axios from "axios";
import {globalConfig} from "@/global.config";
import {store} from "@/store";

export class request {
    static get(url) {
        return axios.get(url,{
            baseURL: globalConfig.requestConfig.baseURL,
            responseType: "json",
            headers: {'token': store.state.token}
        });
    }
    static post(url, data){
        return axios.post(url, data,{
            baseURL: globalConfig.requestConfig.baseURL,
            responseType: "json",
            headers: {'token': store.state.token}
        });
    }
}