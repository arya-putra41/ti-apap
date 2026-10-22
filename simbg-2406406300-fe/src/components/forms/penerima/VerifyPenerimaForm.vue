<script setup lang="ts">
import VInput from '../../common/VInput.vue';
import VButton from '../../common/VButton.vue';
import VSelect from '../../common/VSelect.vue';
import { onMounted, type PropType, ref, toRefs, watch } from 'vue';
import { useRouter } from 'vue-router';
import { type VerifyPenerimaRequest } from '@/services/penerimaService.ts';
import { petugasService, type PetugasVerifikasi } from '@/services/petugasService.ts';
import VTextArea from '@/components/common/VTextArea.vue';

const router = useRouter();

const loading = ref(false)
const errorMessage = ref('')

const props = defineProps({
    action: {
        type: Function as PropType<(data: VerifyPenerimaRequest) => Promise<void>>,
        required: true,
    },
    profileModel: {
        type: Object as PropType<VerifyPenerimaRequest>,
        required: true,
    }
})

const model = toRefs(props).profileModel;
const emit = defineEmits(['update:modelValue'])
const listPetugas = ref<PetugasVerifikasi[]>([])

watch(() => model, (newValue) => {
    emit('update:modelValue', newValue);
}, { deep: true })

const getRombel = async () => {
    listPetugas.value = await petugasService.findAll()
}

const handleSubmit = async () => {
    loading.value = true
    errorMessage.value = ''

    try {
        await props.action(model.value)
    } catch (err: any) {
        console.error('Submit Error:', err)
        // Ekstraksi pesan error dari response backend (Axios / RestClient)
        errorMessage.value = 
            err.response?.data?.message || 
            err.response?.data?.error || 
            'Terjadi kesalahan saat menyimpan data rombel. Silakan periksa kembali isian Anda.'
    } finally {
        loading.value = false
    }
}

onMounted(getRombel)
</script>

<template>
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-6 py-4">
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
            <VSelect v-model="model.petugasVerifikasiId" id="petugasVerifikasiId" name="petugasVerifikasiId" label="Petugas Verifikasi" required>
                <option value="">Pilih petugas...</option>
                <option v-for="p in listPetugas" :key="p.id" :value="p.id">{{ p.nama }} - {{ p.wilayahTugas }}</option>
            </VSelect>
            <VTextArea v-model="model.catatan" id="catatan" name="catatan" label="Catatan Verifikasi" required></VTextArea>
        </div>

        <div class="flex justify-end gap-2 pt-4">
            <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Batal</VButton>
            <VButton type="submit" :disabled="loading" class="bg-pink-600 hover:bg-pink-800 text-white disabled:opacity-50 cursor-pointer">
                {{ loading ? 'Menyimpan...' : 'Verifikasi' }}
            </VButton>
        </div>
    </form>
</template>

<style scoped></style>