<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { penerimaService, type DeactivatePenerimaRequest } from '@/services/penerimaService'
import DeactivatePenerimaForm from '@/components/forms/penerima/DeactivatePenerimaForm.vue'
import { toast } from 'vue-sonner'

const route = useRoute()
const router = useRouter()
const penerimaId = route.params.id as string

const isLoaded = ref(false)

const penerimaModel = ref<DeactivatePenerimaRequest>({
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

const handleDeactivate = async (formData: DeactivatePenerimaRequest) => {
  const payload: DeactivatePenerimaRequest = {
    ...formData,
    id: penerimaId,
  }

  await penerimaService.deactivatePenerima(payload)
  toast.success('Penerima berhasil dinonaktifkan!')
  router.push(`/penerima/${penerimaId}`)
}
</script>

<template>
  <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
    <div class="mx-auto w-full max-w-3xl mg-white shadow-lg rounded-2xl p-6 md:p-8 flex flex-col gap-4">
      <h1 class="text-pink-600 font-bold text-xl">Nonaktifkan Penerima</h1>
      
      <!-- Form hanya dirender saat data backend selesai dimuat -->
      <DeactivatePenerimaForm 
        v-if="isLoaded" 
        :profile-model="penerimaModel" 
        :action="handleDeactivate"
      />

      <!-- Indicator saat fetching data -->
      <div v-else class="py-10 text-center text-slate-500">
        Memuat data penerima...
      </div>
    </div>
  </main>
</template>