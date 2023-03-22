export const getTimeStr = (dateTime) => {
    dateTime = new Date(dateTime)
    let timeArray = [
        dateTime.getFullYear(),
        dateTime.getMonth()+1,
        dateTime.getDate(),
        dateTime.getHours(),
        dateTime.getMinutes(),
        dateTime.getSeconds()
    ]
    for(let i=0;i<4;i++){
        if(timeArray[i] < 10){
            timeArray[i] = '0' + timeArray[i].toString()
        }else{
            timeArray[i] = '' + timeArray[i].toString()
        }
    }
    return timeArray[0] + '-' + timeArray[1] + '-' + timeArray[2] + ' ' + timeArray[3] + ':' + timeArray[4] + ":" + timeArray[5]
}

export const getTimeStrOfChina = (dateTime) => {
    dateTime = new Date(dateTime)
    let timeArray = [
        dateTime.getFullYear(),
        dateTime.getMonth()+1,
        dateTime.getDate(),
        dateTime.getHours(),
        dateTime.getMinutes(),
        dateTime.getSeconds()
    ]
    for(let i=0;i<6;i++){
        if(timeArray[i] < 10){
            timeArray[i] = '0' + timeArray[i].toString()
        }else{
            timeArray[i] = '' + timeArray[i].toString()
        }
    }
    return timeArray[0] + '年' + timeArray[1] + '月' + timeArray[2] + '日 ' + timeArray[3] + ':' + timeArray[4] + ":" + timeArray[5]
}

export const getTimeDiff = (timeStamp) => {
    let hours = (timeStamp/3600)-24;    // 小时 60*60 总小时数-过去的小时数=现在的小时数
    let minutes = timeStamp%3600/60; // 分钟 -(day*24) 以60秒为一整份 取余 剩下秒数 秒数/60 就是分钟数
    let seconds = timeStamp%60;  // 以60秒为一整份 取余 剩下秒数
    if(hours < 10){
        hours = '0'.concat(hours)
    }
    if(minutes < 10){
        minutes = '0'.concat(minutes)
    }
    if(seconds < 10){
        seconds = '0'.concat(seconds)
    }
    return hours + ":" + minutes + ":" + seconds
}