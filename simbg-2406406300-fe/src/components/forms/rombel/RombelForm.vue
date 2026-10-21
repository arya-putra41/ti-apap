<script setup lang="ts">
import VInput from '../../common/VInput.vue';
import VButton from '../../common/VButton.vue';
import VSelect from '../../common/VSelect.vue';
import { onMounted, type PropType, ref, toRefs, watch } from 'vue';
import { useRouter } from 'vue-router';
import { type CreateRombelRequest } from '@/services/rombelService.ts';
import { sekolahService, type SekolahBasicResponse } from '@/services/sekolahService.ts';

const router = useRouter();

const loading = ref(false)
const errorMessage = ref('')

const props = defineProps({
    action: {
        type: Function as PropType<(data: CreateRombelRequest) => Promise<void>>,
        required: true,
    },
    profileModel: {
        type: Object as PropType<CreateRombelRequest>,
        required: true,
    },
    sekolahId: {
        type: String,
        required: false
    }
})

const model = toRefs(props).profileModel;
const emit = defineEmits(['update:modelValue'])
const listSekolah = ref<SekolahBasicResponse[]>([])

watch(() => model, (newValue) => {
    emit('update:modelValue', newValue);
}, { deep: true })

// Otomatis set model.sekolahId saat prop sekolahId tersedia
watch(
    () => props.sekolahId,
    (newSekolahId) => {
        if (newSekolahId) {
            model.value.sekolahId = newSekolahId
        }
    },
    { immediate: true }
)

const getSekolah = async () => {
    listSekolah.value = await sekolahService.findAll();
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

onMounted(getSekolah)
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

        <!--Row 1: Sekolah & Nama Rombel-->
        <div class="grid grid-cols-3 gap-4">
            <VSelect v-model="model.sekolahId" id="sekolahId" name="sekolahId" label="Sekolah" :disabled="!!sekolahId" required class="col-span-2">
                <option value="">Pilih sekolah...</option>
                <option v-for="s in listSekolah" :key="s.id" :value="s.id">{{ s.namaSekolah }}</option>
            </VSelect>
            <VInput v-model="model.namaRombel" id="namaRombel" name="namaRombel" label="Nama Rombel" required/>
        </div>

        <!--Row 2: Tingkat, Tahun Ajaran, Kuota-->
        <div class="grid grid-cols-3 gap-4">
            <VInput v-model="model.tingkat" type="number" min="1" max="12" id="tingkat" name="tingkat" label="Tingkat" required/>
            <VInput v-model="model.tahunAjaran" id="tahunAjaran" name="tahunAjaran" label="Tahun Ajaran" required/>
            <VInput v-model="model.kuotaPenerima" type="number" min="1" id="kuotaPenerima" name="kuotaPenerima" label="Kuota Penerima" required/>
        </div>

        <div class="flex justify-end gap-2 pt-4">
            <VButton type="submit" class="bg-pink-600 hover:bg-pink-800 text-white">Simpan</VButton>
        </div>
    </form>
</template>

<style scoped></style>