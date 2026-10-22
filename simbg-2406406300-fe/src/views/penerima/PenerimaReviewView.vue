<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { penerimaService, type ReviewPenerimaRequest } from '@/services/penerimaService'
import ReviewPenerimaForm from '@/components/forms/penerima/ReviewPenerimaForm.vue'
import { toast } from 'vue-sonner'

const route = useRoute()
const router = useRouter()
const penerimaId = route.params.id as string

const isLoaded = ref(false)

const penerimaModel = ref<ReviewPenerimaRequest>({
    id: '',
    alasan: '',
})

onMounted(async () => {
  try {
    const data = await penerimaService.findPenerimaById(penerimaId)

    // Set data yang didapat dari backend ke model
    penerimaModel.value.id = data.id

    isLoaded.value = true
  } catch (error) {
    toast.error('Gagal memuat data rombel.')
  }
})

const handleReview = async (formData: ReviewPenerimaRequest) => {
  const payload: ReviewPenerimaRequest = {
    ...formData,
    id: penerimaId,
  }

  await penerimaService.reviewPenerima(payload)
  toast.success('Penerima berhasil ditinjau ulang - dikembalikan ke status Diajukan')
  router.push(`/penerima/${penerimaId}`)
}
</script>

<template>
  <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
    <div class="mx-auto w-full bg-white shadow-lg rounded-2xl p-6 px-10 md:p-8 flex flex-col gap-4">
      <h1 class="text-pink-600 font-bold text-xl">Tinjau Ulang Penerima</h1>
      
      <!-- Form hanya dirender saat data backend selesai dimuat -->
      <ReviewPenerimaForm 
        v-if="isLoaded" 
        :profile-model="penerimaModel" 
        :action="handleReview"
      />

      <!-- Indicator saat fetching data -->
      <div v-else class="py-10 text-center text-slate-500">
        Memuat data penerima...
      </div>
    </div>
  </main>
</template>