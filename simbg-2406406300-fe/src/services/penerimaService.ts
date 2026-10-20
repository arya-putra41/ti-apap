import { api } from "./api";
import type { ApiResponse } from "./api";
import type { PetugasVerifikasi } from "./petugasService";
import type { RiwayatResponse } from "./riwayatService";

export interface CreatePenerimaRequest {
    rombelId: string
    nama: string
    nisn: string
    jenisKelamin: string
    tanggalLahir: Date
    kondisiKhusus: string
    keterangan: string
}

export interface PenerimaBasicResponse {
    id: string
    nama: string
    nisn: string
    rombelId: string
    namaRombel: string
    namaSekolah: string
    jenisKelamin: string
    kondisiKhusus: string
    status: string
    tanggalPengajuan: Date
}

export interface PenerimaResponse {
    id: string
    nama: string
    nisn: string
    tanggalLahir: Date
    rombelId: string
    namaRombel: string
    namaSekolah: string
    jenisKelamin: string
    kondisiKhusus: string
    keterangan: string
    status: string
    tanggalPengajuan: Date
    tanggalVerifikasi: Date
    petugasVerifikasi: PetugasVerifikasi
    riwayat: RiwayatResponse[]
}

export interface UpdatePenerimaRequest {
    id: string
    nama: string
    jenisKelamin: string
    tanggalLahir: Date
    kondisiKhusus: string
    keterangan: string
}

export interface VerifyPenerimaRequest {
    id: string
    petugasVerifikasiId: string
    catatan: string
}

export interface RejectPenerimaRequest {
    id: string
    alasan: string
}

export interface DeactivatePenerimaRequest {
    id: string
    alasan: string
}

export interface ReviewPenerimaRequest {
    id: string
    alasan: string
}

export const penerimaService = {
    async findAll(): Promise<PenerimaBasicResponse[]> {
        const response = await api.get<ApiResponse<PenerimaBasicResponse[]>>('/penerima')

        return response.data.data
    },

    async findPenerimaById(id: string): Promise<PenerimaResponse> {
        const response = await api.get<ApiResponse<PenerimaResponse>>(`/penerima/${id}`)

        return response.data.data
    }
}

