import axios from 'axios'

export interface ApiResponse<T> {
    code: string
    message: string
    timestamp: string
    data: T
}

export const api = axios.create({
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json'
    },
})