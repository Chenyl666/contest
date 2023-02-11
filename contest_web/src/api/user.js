import {store} from "@/store";
import {request} from "@/util/request";

const getUserPictureByToken = () => {
    let token = store.state.token
    request.get('/user/picture')
}