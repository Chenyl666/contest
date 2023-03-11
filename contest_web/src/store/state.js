export const state = () => {
    return {
        token: 'none',
        page: 'none',
        userDto: undefined,
        fileUrls: '',
        createdContestPage: {
            createContestStep: 0,
            contestMessage: undefined,
            enrollMessage: undefined
        },
        questionContentText: null,
        fullScreen: false
    }
}