export class StringUtil{
    static rex = new RegExp("<.+?>","g")
    static isEmpty(str){
        return str === undefined || str === ''
    }
    static getStringPrefix(str,number){
        if(str.length <= number+3){
            return str
        }
        return str.substr(0,number) + '...'
    }
    static parseFromHtml(html){
        if(html === null){
            return null
        }
        return html.replace(this.rex, '');
    }
}
