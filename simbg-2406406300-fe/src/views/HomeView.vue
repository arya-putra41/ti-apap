<script setup lang="ts">
import { RouterLink } from 'vue-router';
import VButton from '@/components/common/VButton.vue';
import { onMounted, ref } from 'vue';
import { homeService, type HomeInformation } from '@/services/homeService';

const homeInfo = ref<HomeInformation | null>(null)
const loading = ref(false)
const error = ref('')

async function loadHomeInfo() {
  loading.value = true
  error.value = ''

  try {
    homeInfo.value = await homeService.getHomeInformation()
  } catch (err) {
    console.error(err)
    error.value = 'Gagal memuat halaman home, coba lagi nanti'
  } finally {
    loading.value = false
  }
}

onMounted(loadHomeInfo)
</script>

<template>
  <main class="flex items-center justify-center w-full h-screen">
    <div class="flex flex-col w-fit gap-2 items-center">

      <!--Heading-->
      <h1 class="text-6xl font-bold text-center">
        Welcome to <span class="text-pink-600">SIMBG</span>
      </h1>
      <p class="text-2xl">Sistem Informasi Makan Bergizi Gratis</p>
      <p class="text-lg">Modul 2: Sekolah dan Penerima Manfaat</p>
      
      <!--Statistics-->
      <div class="flex flex-row mt-5">
        <p v-if="loading">Memuat informasi...</p>
        <p v-else-if="error">{{ error }}</p>

        <div v-else-if="homeInfo" class="flex flex-row gap-2">
          <div class="p-2 border border-black rounded-md">
            <h2 class="font-bold">Sekolah Aktif</h2>
            <p>{{ homeInfo.jumlahSekolahAktif }}</p>
          </div>

          <div class="p-2 border border-black rounded-md">
            <h2 class="font-bold">Rombel Aktif</h2>
            <p>{{ homeInfo.jumlahRombelAktif }}</p>
          </div>

          <div class="p-2 border border-black rounded-md">
            <h2 class="font-bold">Penerima Aktif</h2>
            <p>{{ homeInfo.jumlahPenerimaAktif }}</p>
          </div>

          <div class="p-2 border border-black rounded-md">
            <h2 class="font-bold">Kuota Terpakai</h2>
            <p>{{ homeInfo.persentaseKuotaTerpakai }}</p>
          </div>

          <div class="p-2 border border-black rounded-md">
            <h2 class="font-bold">Penerima Menunggu</h2>
            <p>{{ homeInfo.jumlahMenungguVerifikasi }}</p>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>