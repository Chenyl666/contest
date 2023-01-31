import { createApp } from 'vue'
import App from './App.vue'
import TDesign from 'tdesign-vue-next'
import router from "@/router/router";
import {store} from "@/store";

createApp(App)
    .use(TDesign)
    .use(router)
    .use(store)
    .mount('#app')
