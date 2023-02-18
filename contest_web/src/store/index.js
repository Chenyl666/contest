import {createStore} from "vuex";
import {mutations} from "@/store/mutations";
import {state} from "@/store/state";
import {getters} from "@/store/getters";

export const store = createStore({
    state,
    getters,
    mutations
})