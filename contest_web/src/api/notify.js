import {request} from "@/util/request";

export const loadNotifyMessage = (page) => {
    return request.get('/contest/notify/page/'.concat(page),true)
}

export const setHasRead = (messageId) => {
    return request.postWithForm('/contest/notify/read',{messageId},true)
}

export const getUnreadCount = () => {
    return request.get('/contest/notify/unread/count',true)
}

export const deleteNotifyMessage = (messageId) => {
    return request.delete('/contest/notify/delete/'.concat(messageId),true)
}