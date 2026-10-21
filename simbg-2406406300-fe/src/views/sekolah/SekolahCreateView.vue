<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
import { sekolahService, type CreateSekolahRequest } from '@/services/sekolahService';
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
    if (await sekolahService.createSekolah(bodyRequest)) {
        router.push('/sekolah')
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
            <h1 class="text-pink-600 font-bold text-xl">Tambah Sekolah</h1>
            <SekolahForm :profile-model="sekolahModel" :action="addSekolah"/>
        </div>
    </main> 
</template>