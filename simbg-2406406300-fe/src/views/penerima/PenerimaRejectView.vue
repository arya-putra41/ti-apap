<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { penerimaService, type RejectPenerimaRequest } from '@/services/penerimaService'
import RejectPenerimaForm from '@/components/forms/penerima/RejectPenerimaForm.vue'
import { toast } from 'vue-sonner'

const route = useRoute()
const router = useRouter()
const penerimaId = route.params.id as string

const isLoaded = ref(false)

const penerimaModel = ref<RejectPenerimaRequest>({
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

const handleReject = async (formData: RejectPenerimaRequest) => {
  const payload: RejectPenerimaRequest = {
    ...formData,
    id: penerimaId,
  }

  await penerimaService.rejectPenerima(payload)
  toast.success('Penerima berhasil ditolak!')
  router.push(`/penerima/${penerimaId}`)
}
</script>

<template>
  <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
    <div class="mx-auto w-full max-w-3xl bg-white shadow-lg rounded-2xl p-6 md:p-8 flex flex-col gap-4">
      <h1 class="text-pink-600 font-bold text-xl">Tolak Penerima</h1>
      
      <!-- Form hanya dirender saat data backend selesai dimuat -->
      <RejectPenerimaForm 
        v-if="isLoaded" 
        :profile-model="penerimaModel" 
        :action="handleReject"
      />

      <!-- Indicator saat fetching data -->
      <div v-else class="py-10 text-center text-slate-500">
        Memuat data penerima...
      </div>
    </div>
  </main>
</template>