import { api } from "./api";
import type { ApiResponse } from "./api";

export interface HomeInformation {
    jumlahSekolahAktif: number
    jumlahRombelAktif: number
    jumlahPenerimaAktif: number
    persentaseKuotaTerpakai: number
    jumlahMenungguVerifikasi: number
}

export const homeService = {
    async getHomeInformation(): Promise<HomeInformation> {
        const response = await api.get<ApiResponse<HomeInformation>>('/home')
        
        return response.data.data
    }
}