import {DEV_CONFIG} from "@/config/dev.config";

const config = DEV_CONFIG

export const globalConfig = {
    requestConfig: {
        requestTimeOut: 100000,
        baseURL: config.BASE_URL
    }
}