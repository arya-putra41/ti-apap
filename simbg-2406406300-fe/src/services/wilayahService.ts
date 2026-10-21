import { api } from "./api"

export interface WilayahOption {
    code: string
    name: string
}

export const wilayahService = {
    async getProvinsi(): Promise<WilayahOption[]> {
        const res = await api.get('/wilayah/provinsi')
        return res.data.data
    },
    async getKota(provinceCode: string): Promise<WilayahOption[]> {
        const res = await api.get(`/wilayah/kota/${provinceCode}`)
        return res.data.data
    },
    async getKecamatan(regencyCode: string): Promise<WilayahOption[]> {
        const res = await api.get(`/wilayah/kecamatan/${regencyCode}`)
        return res.data.data
    },
    async getKelurahan(districtCode: string): Promise<WilayahOption[]> {
        const res = await api.get(`/wilayah/kelurahan/${districtCode}`)
        return res.data.data
    },
    async getProvinsiByName(name: string): Promise<string> {
        const res = await api.get('/wilayah/provinsi/code', { params: {name} })
        return res.data.data
    },
    async getKotaByName(name: string, provinceCode: string): Promise<string> {
        const res = await api.get(`/wilayah/kota/code`, { params: {name, provinceCode} })
        return res.data.data
    },
    async getKecamatanByName(name: string, regencyCode: string): Promise<string> {
        const res = await api.get(`/wilayah/kecamatan/code`, { params: {name, regencyCode} })
        return res.data.data
    },
    async getKelurahanByName(name: string, districtCode: string): Promise<string> {
        const res = await api.get(`/wilayah/kelurahan/${districtCode}`, { params: {name, districtCode} })
        return res.data.data
    },
}