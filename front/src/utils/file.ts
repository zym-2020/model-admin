
import axios from 'axios';


export function createFileChunk(file: File) {
    const size = 5242880
    const fileChunkList = []
    let count = 0
    while (count < file.size) {
        fileChunkList.push({
            file: file.slice(count, count + size),
        })
        count += size
    }
    return fileChunkList
}

export async function handlePostFiles(chunkList: string[], fileChunk: { file: Blob }[], number: string, percentage: any) {
    return new Promise((res, rej) => {
        const totalChunks = chunkList.length 
        let successCount = 0
        const handle = () => {
            if (chunkList.length) {
                const name = chunkList.shift()
                const token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJ0ZWFtSWQiOiIxMjM0NTYiLCJuYW1lIjoi5byg5LiJIiwiaWQiOiI2MmRmYTM1NzVmODkxMTJhYmY4NDhlYTkiLCJleHAiOjE2NTkzMjM5MDcsImVtYWlsIjoiMTIzQHFxLmNvbSIsIm1lbWJlcklkIjoiMTIzIn0.U6k98MKnf29CTsrytbSkipo0H-d9zbEQY7t8gJDKMM1oUROi8A9pC0kWb4-nJL-2oepIJKfWsthKj40jheMemA"
                const formData = new FormData()
                formData.append("file", fileChunk[parseInt(name as string)].file)
                formData.append("name", name as string)
                formData.append("number", number)
                axios.post('http://localhost:8888/homework/uploadFile', formData, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }).then(response => {
                    if (response.status === 200 && response.data.code === 0) {
                        successCount++
                        percentage.value = parseFloat(((totalChunks - chunkList.length) / totalChunks * 100).toFixed(1))
                        handle()
                    } else {
                        successCount++
                        chunkList.push(name as string)
                        handle()
                    }
                }).catch(err => {
                    successCount++
                    chunkList.push(name as string)
                    handle()
                })
            }
            if (successCount >= totalChunks) {
                res(undefined);
            }
        }
        for (let i = 0; i < 5; i++) {
            handle();
        }
    })
}
