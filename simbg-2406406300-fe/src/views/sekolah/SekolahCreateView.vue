<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { sekolahService, type CreateSekolahRequest, type SekolahResponse } from '@/services/sekolahService';
import SekolahForm from '@/components/forms/sekolah/SekolahForm.vue';
import { toast } from 'vue-sonner';

const router = useRouter();

const sekolahModel = reactive<CreateSekolahRequest>({
    npsn: '',
    namaSekolah: '',
    jenjang: '',
    provinsi: '',
    kota: '',
    kecamatan: '',
    desa: '',
    jalan: '',
    namaKepalaSekolah: '',
    teleponSekolah: '',
    tanggalBergabung: ''
});

const addSekolah = async (bodyRequest: CreateSekolahRequest) => {
    const newSekolah = await sekolahService.createSekolah(bodyRequest)
    if (newSekolah) {
        const sekolahId = newSekolah.id
        router.push(`/sekolah/${sekolahId}`)
    } else {
        toast.error('Gagal mendaftarkan data sekolah. Coba lagi nanti')
        return
    }
    toast.success('Data sekolah didaftarkan!')
}
</script>


<template>
    <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
        <div class="mx-auto w-full bg-white shadow-lg rounded-2xl p-6 px-10 md:p-8 flex flex-col gap-4">
            <div>
                <h1 class="text-pink-600 font-bold text-xl">Tambah Sekolah</h1>
                <h2 class="text-gray-600 text-sm">Informasi wilayah (provinsi, kabupaten/kota, kecamatan, dan desa/kelurahan) diambil otomatis dari API wilayah.id. Jika pilihan wilayah tidak muncul, mohon tunggu beberapa detik dan coba lagi.</h2>
            </div>
            <SekolahForm :profile-model="sekolahModel" :action="addSekolah"/>
        </div>
    </main> 
</template>