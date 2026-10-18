import { api } from "./api";
import type { ApiResponse } from "./api";

export interface RiwayatResponse {
    id: string
    statusSebelumnya: string
    statusBaru: string
    tanggalPerubahan: Date
    keterangan: string
}