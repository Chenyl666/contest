import {createStore} from "vuex";
import {mutations} from "@/store/mutation/mutations";
import {state} from "@/store/state";

export const store = createStore({
    state,
    mutations
})