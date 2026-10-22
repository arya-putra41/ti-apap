import { api } from "./api";
import type { ApiResponse } from "./api";
import type { PetugasVerifikasi } from "./petugasService";
import type { RiwayatResponse } from "./riwayatService";

export interface CreatePenerimaRequest {
    rombelId: string
    nama: string
    nisn: string
    jenisKelamin: string
    tanggalLahir: string
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
    tanggalLahir: string
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
    },

    async createPenerima(request: CreatePenerimaRequest): Promise<PenerimaResponse> {
        const response = await api.post<ApiResponse<PenerimaResponse>>('/penerima/create', request)

        return response.data.data
    },

    async updatePenerima(request: UpdatePenerimaRequest): Promise<PenerimaResponse> {
        const response = await api.put<ApiResponse<PenerimaResponse>>('/penerima/update', request)

        return response.data.data
    },

    async verifyPenerima(request: VerifyPenerimaRequest): Promise<PenerimaResponse> {
        const response = await api.put<ApiResponse<PenerimaResponse>>('/penerima/verifikasi', request)

        return response.data.data
    },

    async activatePenerima(penerimaId: string): Promise<PenerimaResponse> {
        const response = await api.put<ApiResponse<PenerimaResponse>>(`/penerima/${penerimaId}/aktivasi`)

        return response.data.data
    },

    async rejectPenerima(request: RejectPenerimaRequest): Promise<PenerimaResponse> {
        const response = await api.put<ApiResponse<PenerimaResponse>>('/penerima/tolak', request)

        return response.data.data
    },

    async deactivatePenerima(request: DeactivatePenerimaRequest): Promise<PenerimaResponse> {
        const response = await api.put<ApiResponse<PenerimaResponse>>('/penerima/nonaktifkan', request)

        return response.data.data
    },

    async reviewPenerima(request: ReviewPenerimaRequest): Promise<PenerimaResponse> {
        const response = await api.put<ApiResponse<PenerimaResponse>>('/penerima/tinjau-ulang', request)

        return response.data.data
    },
}

