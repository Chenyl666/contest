import {request} from "@/util/request";

export const uploadSimpleFile = async (file, fileName, publicPerm, timeLimit) => {
    let url = ''
    await request.postFile('/filesys/upload/simple', {
        file, fileName, publicPerm, timeLimit
    }, true).then(resp => {
        url = resp.data.data
    })
    return url
}