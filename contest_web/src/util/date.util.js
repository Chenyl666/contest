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
    return timeArray[0] + '-' + timeArray[1] + '-' + timeArray[2] + ' ' + timeArray[3] + ':' + timeArray[4] + timeArray[5]
}