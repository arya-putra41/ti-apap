<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { sekolahService, type UpdateSekolahRequest } from '@/services/sekolahService';
import { toast } from 'vue-sonner';
import { format } from 'date-fns';
import UpdateSekolahForm from '@/components/forms/sekolah/UpdateSekolahForm.vue';

const route = useRoute();
const router = useRouter();
const sekolahId = route.params.id as string;

// State untuk menandai apakah data backend sudah selesai dimuat
const isLoaded = ref(false);

const sekolahModel = ref<UpdateSekolahRequest>({
    id: '',
    npsn: '',
    namaSekolah: '',
    jenjang: '',
    provinsi: '',
    kota: '',
    kecamatan: '',
    desa: '',
    jalan: '',
    namaKepalaSekolah: '',
    teleponSekolah: '',
    tanggalBergabung: ''
});

const updateSekolah = async (formData: UpdateSekolahRequest) => {
    const updateRequest = {
        ...formData,
        id: sekolahId
    }

    if (await sekolahService.updateSekolah(updateRequest)) {
        toast.success('Data sekolah diperbarui!')
        router.push('/sekolah')
    } else {
        toast.error('Gagal memperbarui data sekolah. Coba lagi nanti')
    }
}

onMounted(async () => {
  try {
    const data = await sekolahService.findSekolahById(sekolahId)
    
    // Format tanggal ke YYYY-MM-DD
    const rawDate = data.tanggalBergabung ? new Date(data.tanggalBergabung) : new Date();

    sekolahModel.value = {
      id: data.id,
      npsn: data.npsn,
      namaSekolah: data.namaSekolah,
      jenjang: data.jenjang,
      provinsi: data.provinsi,
      kota: data.kota,
      kecamatan: data.kecamatan,
      desa: data.desa,
      jalan: data.jalan,
      namaKepalaSekolah: data.namaKepalaSekolah,
      teleponSekolah: data.teleponSekolah,
      tanggalBergabung: format(rawDate, "yyyy-MM-dd")
    }

    // Set true agar UpdateSekolahForm baru di-render
    isLoaded.value = true
  } catch (error) {
    toast.error('Gagal memuat data sekolah.')
  }
})
</script>

<template>
    <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
        <div class="mx-auto w-full bg-white shadow-lg rounded-2xl p-6 px-10 md:p-8 flex flex-col gap-4">
            <h1 class="text-pink-600 font-bold text-xl">Edit Sekolah</h1>
            
            <!-- Render form HANYA jika data sudah terisi -->
            <UpdateSekolahForm 
                v-if="isLoaded" 
                :profile-model="sekolahModel" 
                :action="updateSekolah"
            />

            <!-- Tampilan sementara saat loading -->
            <div v-else class="py-10 text-center text-slate-500">
                Memuat data sekolah...
            </div>
        </div>
    </main> 
</template>