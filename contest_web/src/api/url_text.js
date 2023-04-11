import {request} from "@/util/request";

export const getUrlText = (url) => {
    return request.get(url)
}