import { api } from "./api";
import type { ApiResponse } from "./api";

export interface PetugasVerifikasi {
    id: string
    nip: string
    nama: string
    wilayahTugas: string
    email: string
    telepon: string
}

export const petugasService = {
    async findAll(): Promise<PetugasVerifikasi[]> {
        const response = await api.get<ApiResponse<PetugasVerifikasi[]>>('/petugas')
        
        return response.data.data
    }
}
