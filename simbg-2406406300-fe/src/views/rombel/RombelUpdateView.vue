<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { rombelService, type UpdateRombelRequest } from '@/services/rombelService'
import UpdateRombelForm from '@/components/forms/rombel/UpdateRombelForm.vue'
import { toast } from 'vue-sonner'

const route = useRoute()
const router = useRouter()
const rombelId = route.params.id as string

const isLoaded = ref(false)

const rombelModel = ref<UpdateRombelRequest>({
  id: '',
  kuotaPenerima: '',
  status: 'AKTIF'
})

onMounted(async () => {
  try {
    const data = await rombelService.findRombelById(rombelId)
    
    // Set data yang didapat dari backend ke model
    rombelModel.value = {
      id: data.id,
      kuotaPenerima: String(data.kuotaPenerima),
      status: data.status
    }
    
    isLoaded.value = true
  } catch (error) {
    toast.error('Gagal memuat data rombel.')
  }
})

const handleUpdate = async (formData: UpdateRombelRequest) => {
  const payload: UpdateRombelRequest = {
    ...formData,
    id: rombelId,
    kuotaPenerima: formData.kuotaPenerima // Pastikan dikirim sebagai number
  }

  if (await rombelService.updateRombel(payload)) {
        toast.success('Data rombel berhasil diperbarui!')
        router.push(`/rombel/${rombelId}`);
    } else {
        toast.error('Gagal memperbarui data rombel. Coba lagi nanti');
  }
}
</script>

<template>
  <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
    <div class="mx-auto w-full max-w-2xl bg-white shadow-lg rounded-2xl p-6 md:p-8 flex flex-col gap-4">
      <h1 class="text-pink-600 font-bold text-xl">Update Rombel</h1>
      
      <!-- Form hanya dirender saat data backend selesai dimuat -->
      <UpdateRombelForm 
        v-if="isLoaded" 
        :profile-model="rombelModel" 
        :action="handleUpdate"
      />

      <!-- Indicator saat fetching data -->
      <div v-else class="py-10 text-center text-slate-500">
        Memuat data rombel...
      </div>
    </div>
  </main>
</template>