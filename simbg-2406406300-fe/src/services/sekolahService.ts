import { api } from "./api";
import type { ApiResponse } from "./api";
import type { RombelResponse } from "./rombelService";

export interface CreateSekolahRequest {
    npsn: string
    namaSekolah: string
    jenjang: string
    provinsi: string
    kota: string
    kecamatan: string
    desa: string
    jalan: string
    namaKepalaSekolah: string
    teleponSekolah: string
    tanggalBergabung: Date
}

export interface UpdateSekolahRequest {
    id: string
    npsn: string
    namaSekolah: string
    jenjang: string
    provinsi: string
    kota: string
    kecamatan: string
    desa: string
    jalan: string
    namaKepalaSekolah: string
    teleponSekolah: string
    tanggalBergabung: Date
}

export interface SekolahBasicResponse {
    id: string
    npsn: string
    namaSekolah: string
    jenjang: string
    statusMitra: string
}

export interface SekolahResponse {
    id: string
    npsn: string
    namaSekolah: string
    jenjang: string
    provinsi: string
    kota: string
    kecamatan: string
    desa: string
    jalan: string
    namaKepalaSekolah: string
    teleponSekolah: string
    tanggalBergabung: Date
    statusMitra: string
    rombel: RombelResponse[]
}

export const sekolahService = {
    async findAll(): Promise<SekolahBasicResponse[]> {
        const response = await api.get<ApiResponse<SekolahBasicResponse[]>>('/sekolah')

        return response.data.data
    },
    async findSekolahById(id: string): Promise<SekolahResponse> {
        const response = await api.get<ApiResponse<SekolahResponse>>(`/sekolah/${id}`)

        return response.data.data
    }
}