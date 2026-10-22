<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { penerimaService, type UpdatePenerimaRequest } from '@/services/penerimaService'
import UpdatePenerimaForm from '@/components/forms/penerima/UpdatePenerimaForm.vue'
import { toast } from 'vue-sonner'
import { format } from 'date-fns'

const route = useRoute()
const router = useRouter()
const penerimaId = route.params.id as string

const isLoaded = ref(false)
const penerimaIsActive = ref(false)

const penerimaModel = ref<UpdatePenerimaRequest>({
    id: '',
    nama: '',
    jenisKelamin: '',
    tanggalLahir: '',
    kondisiKhusus: '',
    keterangan: ''
})

onMounted(async () => {
  try {
    const data = await penerimaService.findPenerimaById(penerimaId)

    const rawDate = data.tanggalLahir
        ? new Date(data.tanggalLahir)
        : new Date();
    
    // Set data yang didapat dari backend ke model
    penerimaModel.value = {
      id: data.id,
      nama: data.nama,
      jenisKelamin: data.jenisKelamin,
      tanggalLahir: format(rawDate, 'yyyy-MM-dd'),
      kondisiKhusus: data.kondisiKhusus,
      keterangan: data.keterangan,
    }

    if (data.status == "AKTIF") {
        penerimaIsActive.value = true
    }
    
    isLoaded.value = true
  } catch (error) {
    toast.error('Gagal memuat data rombel.')
  }
})

const handleUpdate = async (formData: UpdatePenerimaRequest) => {
  const payload: UpdatePenerimaRequest = {
    ...formData,
    id: penerimaId,
  }

  await penerimaService.updatePenerima(payload)
  toast.success('Data penerima berhasil diperbarui!')
  router.push('/penerima')
}
</script>

<template>
  <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
    <div class="mx-auto w-full bg-white shadow-lg rounded-2xl p-6 px-10 md:p-8 flex flex-col gap-4">
      <h1 class="text-pink-600 font-bold text-xl">Update Penerima</h1>
      
      <!-- Form hanya dirender saat data backend selesai dimuat -->
      <UpdatePenerimaForm 
        v-if="isLoaded" 
        :profile-model="penerimaModel" 
        :action="handleUpdate"
        :penerima-is-active-display="penerimaIsActive"
      />

      <!-- Indicator saat fetching data -->
      <div v-else class="py-10 text-center text-slate-500">
        Memuat data penerima...
      </div>
    </div>
  </main>
</template>