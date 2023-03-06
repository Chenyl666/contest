import LoginBoard from "@/page/login/LoginBoard";
import EmailCodeLoginForm from "@/page/login/component/EmailCodeLoginForm";
import PasswordLoginForm from "@/page/login/component/PasswordLoginForm";
import UsrBoard from '@/page/usr/UsrBoard'
import PasswordModifyForm from "@/page/usr/component/PasswordModifyForm";
import RegisterForm from '@/page/usr/component/RegisterForm'
import OperationSuccess from "@/page/usr/component/OperationSuccess";
import MainPage from "@/page/main/MainPage";
import {store} from "@/store";
import {mutationName} from "@/store/mutations/const.name";
import {style} from "@/common/style";
import ContestPage from "@/page/main/ContestPage";
import QuestionRepoPage from "@/page/main/QuestionRepoPage";
import CoursePage from "@/page/main/CoursePage";
import BookPage from "@/page/main/BookPage";
import CreatedContestPage from "@/page/contest/CreatedContestPage";
import ContestMessage from '@/page/contest/component/ContestMessage'
import EnrollMessage from "@/page/contest/component/EnrollMessage";
import ContestChecking from "@/page/contest/component/ContestChecking";
import ContestList from "@/page/contest/ContestListPage";
import ContestDetailPage from "@/page/contest/ContestDetailPage";
import QuestionRepository from "@/page/question/QuestionRepository";
import QuestionRepositoryDetail from "@/page/question/QuestionRepositoryDetail";
import UserDetail from "@/page/usr/UserDetail";
import NotFound from "@/page/error/NotFound";

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
    },
    {
        path: '/contest',
        component: ContestPage,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.CONTEST_PAGE)
        }
    },
    {
        path: '/ques',
        component: QuestionRepoPage,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.QUESTION_REPO)
        }
    },
    {
        path: '/course',
        component: CoursePage,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.COURSE_PAGE)
        }
    },
    {
        path: '/book',
        component: BookPage,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.BOOK_PAGE)
        }
    },
    {
        path: '/contest/create',
        component: CreatedContestPage,
        redirect: '/contest/create/first',
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.CONTEST_PAGE)
        },
        children: [
            {
                path: '/contest/create/first',
                component: ContestMessage,
                beforeEnter: () => {
                    if(store.state.createdContestPage.createContestStep !== 0){
                        store.commit(mutationName.SET_CREATE_CONTEST_STEP,0)
                    }
                }
            },
            {
                path: '/contest/create/second',
                component: EnrollMessage,
                beforeEnter: () => {
                    if(store.state.createdContestPage.createContestStep !== 1){
                        store.commit(mutationName.SET_CREATE_CONTEST_STEP,1)
                    }
                }
            },
            {
                path: '/contest/create/third',
                component: ContestChecking,
                beforeEnter: () => {
                    if(store.state.createdContestPage.createContestStep !== 2){
                        store.commit(mutationName.SET_CREATE_CONTEST_STEP,2)
                    }
                }
            }
        ]
    },
    {
        path: '/contest/list',
        component: ContestList,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.CONTEST_PAGE)
        }
    },
    {
        path: '/contest/detail/:contestId',
        component: ContestDetailPage,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.CONTEST_PAGE)
        }
    },
    {
        path: '/question/repo',
        component: QuestionRepository,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.QUESTION_REPO)
        }
    },
    {
        path: '/question/repo/detail/:questionRepoId',
        component: QuestionRepositoryDetail,
        beforeEnter: () => {
            store.commit(mutationName.SET_PAGE, style.HEADER_MENU.QUESTION_REPO)
        }
    },
    {
        path: '/usr/detail',
        component: UserDetail
    },
    {
        path: '/404',
        component: NotFound
    }
]