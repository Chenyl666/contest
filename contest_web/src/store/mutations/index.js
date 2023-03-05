export const mutations = {
    setToken(state,token){
        state.token = token
    },
    setPage(state,page){
        state.page = page
    },
    setUser(state,userDto){
        state.userDto = userDto
    },
    setFileUrl(state,url){
        state.fileUrl = url
    },
    setCreateContestStep(state,step){
        state.createdContestPage.createContestStep = step
    },
    setContestMessage(state,contestMessage){
        state.createdContestPage.contestMessage = contestMessage
    },
    setEnrollMessage(state,enrollMessage){
        state.createdContestPage.enrollMessage = enrollMessage
    },
    setQuestionContentText(state,questionContentText){
        state.questionContentText = questionContentText
    }
}