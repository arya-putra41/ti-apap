<script setup lang="ts">
import { reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { rombelService, type CreateRombelRequest } from '@/services/rombelService';
import RombelForm from '@/components/forms/rombel/RombelForm.vue';
import { toast } from 'vue-sonner';

const route = useRoute()
const router = useRouter();

const sekolahIdParam = route.query.sekolahId as string | undefined

const profileModel = reactive<CreateRombelRequest>({
    sekolahId: '',
    namaRombel: '',
    tingkat: '',
    tahunAjaran: '',
    kuotaPenerima: ''
});

const addRombel = async (bodyRequest: CreateRombelRequest) => {
    const newRombel = await rombelService.createRombel(bodyRequest)
    if (newRombel) {
        const rombelId = newRombel.id
        router.push(`/rombel/${rombelId}`)
    } else {
        toast.error('Gagal mendaftarkan data rombel. Coba lagi nanti')
        return
    }
    toast.success('Rombel didaftarkan!')
}
</script>

<template>
    <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
        <div class="mx-auto w-full max-w-3xl bg-white shadow-lg rounded-2xl p-6 md:p-8 flex flex-col gap-4">
            <h1 class="text-pink-600 font-bold text-xl">Tambah Rombel</h1>
            <RombelForm :profile-model="profileModel" :action="addRombel" :sekolah-id="sekolahIdParam"/>
        </div>
    </main>
</template>