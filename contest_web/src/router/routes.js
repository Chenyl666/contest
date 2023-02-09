import LoginBoard from "@/page/login/LoginBoard";
import EmailCodeLoginForm from "@/page/login/component/EmailCodeLoginForm";
import PasswordLoginForm from "@/page/login/component/PasswordLoginForm";
import UsrBoard from '@/page/usr/UsrBoard'
import PasswordModifyForm from "@/page/usr/component/PasswordModifyForm";
import RegisterForm from '@/page/usr/component/RegisterForm'
import OperationSuccess from "@/page/usr/component/OperationSuccess";
import MainPage from "@/page/main/MainPage";
import {store} from "@/store";
import {mutationName} from "@/store/mutation/const.name";
import {style} from "@/const/style";

export const routes = [
    {
        path: '/login', component: LoginBoard,
        redirect: '/login/email-code',
        children: [
            {path: 'email-code', component: EmailCodeLoginForm},
            {path: 'password', component: PasswordLoginForm}
        ]
    },
    {
        path: '/usr', component: UsrBoard,
        redirect: '/usr/register',
        children: [
            {path: 'register', component: RegisterForm},
            {path: 'modify', component: PasswordModifyForm},
            {path: 'success', component: OperationSuccess}
        ]
    },
    {
        path: '/main',
        component: MainPage,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.MAIN_PAGE)
        }
    }
]