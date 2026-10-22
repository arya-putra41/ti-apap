<script setup lang="ts">
import VInput from '../../common/VInput.vue';
import VButton from '../../common/VButton.vue';
import VSelect from '../../common/VSelect.vue';
import { onMounted, type PropType, ref, toRefs, watch } from 'vue';
import { useRouter } from 'vue-router';
import { type UpdateRombelRequest } from '@/services/rombelService.ts';
import { sekolahService, type SekolahBasicResponse } from '@/services/sekolahService.ts';

const router = useRouter();

const loading = ref(false)
const errorMessage = ref('')
const showConfirmModal = ref(false)

const props = defineProps({
    action: {
        type: Function as PropType<(data: UpdateRombelRequest) => Promise<void>>,
        required: true,
    },
    profileModel: {
        type: Object as PropType<UpdateRombelRequest>,
        required: true,
    }
})

const model = toRefs(props).profileModel;
// Menyimpan status awal untuk pengecekan perubahan status
const initialStatus = ref(props.profileModel.status);

const emit = defineEmits(['update:modelValue'])
const listSekolah = ref<SekolahBasicResponse[]>([])

watch(() => model, (newValue) => {
    emit('update:modelValue', newValue);
}, { deep: true })

const getSekolah = async () => {
    listSekolah.value = await sekolahService.findActive();
}

// Fungsi eksekusi simpan ke backend
const executeSubmit = async () => {
    loading.value = true
    errorMessage.value = ''

    try {
        await props.action(model.value)
        showConfirmModal.value = false
    } catch (err: any) {
        console.error('Submit Error:', err)
        errorMessage.value = 
            err.response?.data?.message || 
            err.response?.data?.error || 
            'Terjadi kesalahan saat menyimpan data rombel. Silakan periksa kembali isian Anda.'
    } finally {
        loading.value = false
    }
}

// Handler form submit dengan pengecekan kondisi modal
const handleSubmit = async () => {
    // Jika status diubah dari AKTIF ke DITUTUP, tampilkan modal konfirmasi lebih dahulu
    if (initialStatus.value === 'AKTIF' && model.value.status === 'DITUTUP') {
        showConfirmModal.value = true
        return
    }

    await executeSubmit()
}

onMounted(getSekolah)
</script>

<template>
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-6 py-4">
        <!-- Error Alert -->
        <div 
            v-if="errorMessage" 
            class="p-4 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl flex items-center justify-between"
        >
            <div class="flex items-center gap-2">
                <svg class="w-5 h-5 text-red-500 shrink-0" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                </svg>
                <div class="flex flex-col gap-2">
                    <h2 class="text-xl font-bold">Terjadi kesalahan</h2>
                    <span>{{ errorMessage }}</span>
                </div>
            </div>
            <button 
                type="button" 
                @click="errorMessage = ''" 
                class="text-red-400 hover:text-red-600 font-bold ml-4"
            >
                &times;
            </button>
        </div>

        <div class="grid grid-cols-2 gap-4">
            <VInput v-model="model.kuotaPenerima" type="number" min="1" id="kuotaPenerima" name="kuotaPenerima" label="Kuota Penerima" required/>
            <VSelect v-model="model.status" id="status" name="status" label="Status Rombel">
                <option value="AKTIF">Aktif</option>
                <option value="DITUTUP">Ditutup</option>
            </VSelect>
        </div>

        <div class="flex justify-end gap-2 pt-4">
            <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Batal</VButton>
            <VButton type="submit" :disabled="loading" class="bg-pink-600 hover:bg-pink-800 text-white disabled:opacity-50 cursor-pointer">
                {{ loading ? 'Menyimpan...' : 'Simpan' }}
            </VButton>
        </div>

        <!-- Dialog Modal Konfirmasi Penutupan Rombel -->
        <div 
            v-if="showConfirmModal" 
            class="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-4 backdrop-blur-sm"
        >
            <div class="bg-white rounded-2xl max-w-md w-full p-6 shadow-2xl flex flex-col gap-4">
                <h2 class="text-xl font-bold text-gray-900">Konfirmasi Penutupan Rombel</h2>
                
                <p class="text-gray-600 text-sm">
                    Apakah Anda yakin ingin mengubah status rombel ini menjadi <span class="font-bold text-red-600">DITUTUP</span>? Rombel yang ditutup tidak dapat menerima pendaftaran siswa baru.
                </p>

                <div class="flex justify-end gap-3 pt-2">
                    <VButton 
                        type="button" 
                        @click="showConfirmModal = false" 
                        :disabled="loading"
                        class="bg-gray-200 hover:bg-gray-300 text-gray-800"
                    >
                        Batal
                    </VButton>
                    <VButton 
                        type="button" 
                        @click="executeSubmit" 
                        :disabled="loading"
                        class="bg-red-600 hover:bg-red-700 text-white font-semibold flex items-center gap-2"
                    >
                        {{ loading ? 'Memproses...' : 'Tutup' }}
                    </VButton>
                </div>
            </div>
        </div>
    </form>
</template>