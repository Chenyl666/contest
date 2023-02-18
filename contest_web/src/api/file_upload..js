import {request} from "@/util/request";

export const uploadSimpleFile = async (file, fileName, publicPerm) => {
    let url = ''
    await request.postFile('/filesys/upload/simple', {
        file, fileName, publicPerm
    }, true).then(resp => {
        url = resp.data.data
    })
    return url
}