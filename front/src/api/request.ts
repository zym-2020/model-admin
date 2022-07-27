import { get, post, del, patch } from './axios'

import { FuzzyQueryJsonData, UpdateUserInfoJsonData } from './type'

//user相关接口
export async function fuzzyQuery(jsonData: FuzzyQueryJsonData) {
    return await post(`/user/fuzzyQuery`, jsonData)
}

export async function updateUserInfo(jsonData: UpdateUserInfoJsonData, id: string) {
    return await patch(`/user/updateUserInfo/${id}`, jsonData)
}