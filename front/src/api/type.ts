export interface FuzzyQueryJsonData {
    type: string
    keyWord: string
    page: number
    size: number
}

export interface UpdateUserInfoJsonData {
    name: string
    email: string
    memberId: string
    teamId: string
}

export interface DelUserJsonData {
    id: string
    type: string
    keyWord: string
    page: number
    size: number
}

export interface DelHomeworkJsonData {
    id: string
    memberId: string
    page: number
    size: number
}