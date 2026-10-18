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