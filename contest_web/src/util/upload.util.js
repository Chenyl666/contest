import {request} from "@/util/request";
import SparkMD5 from "spark-md5"
import {result} from "@/const/request.result";


export const upload = async (file,fileName,publicPerm) => {
    generateMd5(file, async (fileMd5) => {
        console.log('md5: ' + fileMd5)
        let resultCode
        let sessionId
        await request.post('/filesys/upload/request', {
            fileName, fileMd5, publicPerm
        }, true).then(resp => {
            resultCode = resp.data['resultCode']
            if (resultCode === result.code.CONTINUE) {
                sessionId = resp.data['data']
            }
        })
        if (resultCode === result.code.SUCCESS) {
            return true
        } else if (resultCode === result.code.CONTINUE) {
            await splitUpload(file, sessionId)
        }
    })
}

export const splitUpload = async (file, sessionId) => {
    // eslint-disable-next-line no-async-promise-executor
    return new Promise(async (resolve, reject) => {
        try {
            const eachSize = 1024 * 1024 * 2;
            const chunks = Math.ceil(file.size / eachSize);
            const fileChunks = await splitFile(file, eachSize, chunks);
            let currentChunk = 0;
            for (let i = 0; i < fileChunks.length; i++) {
                console.log(currentChunk, i);
                if (Number(currentChunk) === i) {
                    let isLast = (i === fileChunks.length-1)
                    await postFile(
                        fileChunks[i],isLast,sessionId
                    );
                    currentChunk++
                }
            }
            resolve();
        } catch (e) {
            reject(e);
        }
    });
}

const splitFile = (file, eachSize, chunks) => {
    return new Promise((resolve, reject) => {
        try {
            setTimeout(() => {
                const fileChunk = [];
                for (let chunk = 0; chunks > 0; chunks--) {
                    fileChunk.push(file.slice(chunk, chunk + eachSize));
                    chunk += eachSize;
                }
                resolve(fileChunk);
            }, 0);
        } catch (e) {
            console.error(e);
            reject(new Error("文件切块发生错误"));
        }
    });
}

const postFile = async (filePiece,isLast,sessionId,usingToken = false) => {
    await request.postFile('/filesys/upload/file', {
        filePiece: filePiece,
        isLast: isLast,
        sessionId: sessionId,
    }, usingToken)
}

const generateMd5 = (f,callback) => {
    let result;
    let fileReader = new FileReader();
    let isLoaded=false;
    let blobSlice = File.prototype.mozSlice || File.prototype.webkitSlice || File.prototype.slice,
        file = f,
        chunkSize = 2097152,
        chunks = Math.ceil(file.size / chunkSize),
        currentChunk = 0,
        spark = new SparkMD5();

    fileReader.onload = function(e) {
        console.log("read chunk nr", currentChunk + 1, "of", chunks);
        spark.appendBinary(e.target.result); // append binary string
        currentChunk++;
        if (currentChunk < chunks) {
            loadNext();
        }
        else {
            console.log("finished loading");
            result = spark.end();
            isLoaded=true;
        }
    };
    function loadNext() {
        let start = currentChunk * chunkSize,
            end = start + chunkSize >= file.size ? file.size : start + chunkSize;
        fileReader.readAsBinaryString(blobSlice.call(file, start, end));
    }
    loadNext();
    fileReader.onloadend = () => {
        if(isLoaded){
            callback(result);
        }
    };
}

