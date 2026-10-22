<script setup lang="ts">
import VButton from '../../common/VButton.vue';
import { type PropType, ref, toRefs, watch } from 'vue';
import { useRouter } from 'vue-router';
import { type ReviewPenerimaRequest } from '@/services/penerimaService.ts';
import VTextArea from '@/components/common/VTextArea.vue';

const router = useRouter();

const loading = ref(false)
const errorMessage = ref('')

const props = defineProps({
    action: {
        type: Function as PropType<(data: ReviewPenerimaRequest) => Promise<void>>,
        required: true,
    },
    profileModel: {
        type: Object as PropType<ReviewPenerimaRequest>,
        required: true,
    }
})

const model = toRefs(props).profileModel;
const emit = defineEmits(['update:modelValue'])

watch(() => model, (newValue) => {
    emit('update:modelValue', newValue);
}, { deep: true })

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

        <div class="grid grid-cols-3 gap-4">
            <VTextArea v-model="model.alasan" id="alasan" name="alasan" label="Alasan Peninjauan Ulang" required class="col-span-3"></VTextArea>
        </div>

        <div class="flex justify-end gap-2 pt-4">
            <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Batal</VButton>
            <VButton type="submit" :disabled="loading" class="bg-pink-600 hover:bg-pink-800 text-white disabled:opacity-50 cursor-pointer">
                {{ loading ? 'Menyimpan...' : 'Tinjau Ulang' }}
            </VButton>
        </div>
    </form>
</template>

<style scoped></style>