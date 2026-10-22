<script setup lang="ts">
import { reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { penerimaService, type CreatePenerimaRequest } from '@/services/penerimaService';
import PenerimaForm from '@/components/forms/penerima/PenerimaForm.vue';
import { toast } from 'vue-sonner';

const route = useRoute()
const router = useRouter();

const rombelIdParam = route.query.rombelId as string | undefined

const profileModel = reactive<CreatePenerimaRequest>({
    rombelId: '',
    nama: '',
    nisn: '',
    jenisKelamin: '',
    tanggalLahir: '',
    kondisiKhusus: '',
    keterangan: ''
});

const addPenerima = async (bodyRequest: CreatePenerimaRequest) => {
    const penerima = await penerimaService.createPenerima(bodyRequest)
    if (penerima) {
        const penerimaId = penerima.id
        router.push(`/penerima/${penerimaId}`)
    } else {
        toast.error('Gagal mendaftarkan data penerima. Coba lagi nanti')
        return
    }
    toast.success('Penerima didaftarkan!')
}
</script>

<template>
    <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
        <div class="mx-auto w-full bg-white shadow-lg rounded-2xl p-6 px-10 md:p-8 flex flex-col gap-4">
            <h1 class="text-pink-600 font-bold text-xl">Tambah Penerima</h1>
            <PenerimaForm :profile-model="profileModel" :action="addPenerima" :rombel-id="rombelIdParam"/>
        </div>
    </main>
</template>