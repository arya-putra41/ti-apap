import { api } from "./api";
import type { ApiResponse } from "./api";
import type { PenerimaBasicResponse } from "./penerimaService";

export interface CreateRombelRequest {
    sekolahId: string
    namaRombel: string
    tingkat: string
    tahunAjaran: string
    kuotaPenerima: number
}

export interface UpdateRombelRequest {
    id: string
    kuotaPenerima: number
    status: string
}

export interface RombelResponse {
    id: string
    sekolahId: string
    namaSekolah: string
    namaRombel: string
    tingkat: string
    tahunAjaran: string
    kuotaPenerima: number
    jumlahPenerimaTerdaftar: number
    sisaKuota: string
    status: string
    penerima: PenerimaBasicResponse[]
}

export interface RombelBasicResponse {
    id: string
    sekolahId: string
    namaSekolah: string
    namaRombel: string
    tingkat: string
    tahunAjaran: string
    kuotaPenerima: number
    jumlahPenerimaTerdaftar: number
    sisaKuota: string
    status: string
}

export const rombelService = {
    async findAll(): Promise<RombelBasicResponse[]> {
        const response = await api.get<ApiResponse<RombelBasicResponse[]>>('/rombel')

        return response.data.data
    },

    async findRombelById(id: string): Promise<RombelResponse> {
        const response = await api.get<ApiResponse<RombelResponse>>(`/rombel/${id}`)

        return response.data.data
    }
}