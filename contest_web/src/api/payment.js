// /contest/pay/alipay
import {request} from "@/util/request";

export const createEnrollPaymentOrder = (contestId) => {
    return request.postWithForm('/contest/pay/alipay',{contestId},true)
}