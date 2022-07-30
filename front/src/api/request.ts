import { get, post, del, patch } from './axios'

import { FuzzyQueryJsonData, UpdateUserInfoJsonData, DelUserJsonData, DelHomeworkJsonData } from './type'

//user相关接口
export async function fuzzyQuery(jsonData: FuzzyQueryJsonData) {
    return await post(`/user/fuzzyQuery`, jsonData)
}

export async function updateUserInfo(jsonData: UpdateUserInfoJsonData, id: string) {
    return await patch(`/user/updateUserInfo/${id}`, jsonData)
}

export async function addUserInfo(jsonData: UpdateUserInfoJsonData, page: number, size: number) {
    return await post(`/user/addUserInfo/${page}/${size}`, jsonData)
}

export async function delUser(jsonData: DelUserJsonData) {
    return await del(`/user/delUser`, jsonData)
}

//homework接口
export async function findHomeworkAll(page: number, size: number) {
    return await get(`/homework/findAll/${page}/${size}`)
}

export async function updateHomework(id: string, state: number) {
    return await patch(`/homework/updateHomework/${id}/${state}`)
}

export async function delHomework(jsonData: DelHomeworkJsonData) {
    return await del(`/homework/delHomework`, jsonData)
}