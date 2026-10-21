<script setup lang="ts">
import { onMounted, ref, watch, type PropType } from 'vue'
import { useRouter } from 'vue-router'
import { type UpdateSekolahRequest } from '@/services/sekolahService'
import { wilayahService, type Wilayah } from '@/services/wilayahService'
import VInput from '../../common/VInput.vue';
import VTextArea from '../../common/VTextArea.vue';
import VButton from '../../common/VButton.vue';
import VSelect from '../../common/VSelect.vue';

const props = defineProps({
  action: {
    type: Function as PropType<(data: UpdateSekolahRequest) => Promise<void>>,
    required: true,
  },
  profileModel: {
    type: Object as PropType<UpdateSekolahRequest>,
    required: true,
  }
})

const router = useRouter()
const model = ref<UpdateSekolahRequest>({ ...props.profileModel })

const provinceList = ref<Wilayah[]>([])
const regencyList = ref<Wilayah[]>([])
const districtList = ref<Wilayah[]>([])
const villageList = ref<Wilayah[]>([])

const loading = ref(true)
const errorMessage = ref('')

onMounted(async () => {
  try {
    // 1. Load SELURUH daftar provinsi
    provinceList.value = await wilayahService.getProvinsi()

    if (model.value.provinsi) {
      // Dapatkan kode provinsi dari nama lama DB (misal: "32" untuk "Jawa Barat")
      const provCode = await wilayahService.getProvinsiByName(model.value.provinsi)

      if (provCode) {
        regencyList.value = await wilayahService.getKota(provCode)
        model.value.provinsi = provCode // Set KODE ke model

        if (model.value.kota) {
          const regCode = await wilayahService.getKotaByName(model.value.kota, provCode)

          if (regCode) {
            districtList.value = await wilayahService.getKecamatan(regCode)
            model.value.kota = regCode // Set KODE ke model

            if (model.value.kecamatan) {
              const distCode = await wilayahService.getKecamatanByName(model.value.kecamatan, regCode)

              if (distCode) {
                villageList.value = await wilayahService.getKelurahan(distCode)
                model.value.kecamatan = distCode // Set KODE ke model

                if (model.value.desa) {
                  const villCode = await wilayahService.getKelurahanByName(model.value.desa, distCode)
                  if (villCode) {
                    model.value.desa = villCode // Set KODE ke model
                  }
                }
              }
            }
          }
        }
      }
    }
  } catch (err) {
    console.error('Gagal memuat wilayah:', err)
  } finally {
    loading.value = false
  }
})

// Watcher interaksi pengguna (selalu menggunakan kode angka)
watch(() => model.value.provinsi, async (newProv) => {
  if (loading.value) return
  model.value.kota = ''
  model.value.kecamatan = ''
  model.value.desa = ''
  regencyList.value = await wilayahService.getKota(newProv)
})

watch(() => model.value.kota, async (newKota) => {
  if (loading.value) return
  model.value.kecamatan = ''
  model.value.desa = ''
  districtList.value = await wilayahService.getKecamatan(newKota)
})

watch(() => model.value.kecamatan, async (newKec) => {
  if (loading.value) return
  model.value.desa = ''
  villageList.value = await wilayahService.getKelurahan(newKec)
})

const handleSubmit = async () => {
  try {
    // Cari kembali NAMA wilayah berdasarkan KODE sebelum dikirim ke backend
    const selectedProvName = provinceList.value.find(p => p.code === model.value.provinsi)?.name || model.value.provinsi
    const selectedKotaName = regencyList.value.find(r => r.code === model.value.kota)?.name || model.value.kota
    const selectedKecName = districtList.value.find(d => d.code === model.value.kecamatan)?.name || model.value.kecamatan
    const selectedDesaName = villageList.value.find(v => v.code === model.value.desa)?.name || model.value.desa

    const payload: UpdateSekolahRequest = {
      ...model.value,
      provinsi: selectedProvName,
      kota: selectedKotaName,
      kecamatan: selectedKecName,
      desa: selectedDesaName,
    }

    await props.action(payload)
  } catch (err: any) {
    errorMessage.value = err.response?.data?.message || 'Gagal mengubah data sekolah.'
  }
}
</script>

<template>
  <div v-if="loading" class="p-8 text-center text-gray-500">
    Memuat data sekolah dan wilayah...
  </div>

  <form v-else @submit.prevent="handleSubmit" class="flex flex-col gap-6 py-4">
    <div v-if="errorMessage" class="p-4 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl flex items-center justify-between">
      <div class="flex items-center gap-2">
        <svg class="w-5 h-5 text-red-500 shrink-0" fill="currentColor" viewBox="0 0 20 20">
          <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
        </svg>
        <div class="flex flex-col gap-2">
          <h2 class="text-xl font-bold">Terjadi kesalahan</h2>
          <span>{{ errorMessage }}</span>
        </div>
      </div>
      <button type="button" @click="errorMessage = ''" class="text-red-400 hover:text-red-600 font-bold ml-4">
        &times;
      </button>
    </div>

    <!-- Row 1: NPSN, Nama, Jenjang -->
    <div class="grid grid-cols-3 gap-4">
      <VInput v-model="model.npsn" id="npsn" name="npsn" label="NPSN" required />
      <VInput v-model="model.namaSekolah" id="namaSekolah" name="namaSekolah" label="Nama Sekolah" required />
      <VSelect v-model="model.jenjang" id="jenjang" name="jenjang" label="Jenjang" required>
        <option value="">Pilih Jenjang</option>
        <option value="SD">SD</option>
        <option value="SMP">SMP</option>
        <option value="SMA/SMK">SMA/SMK</option>
      </VSelect>
    </div>

    <!-- Row 2: Lokasi (Gunakan :value="item.code") -->
    <div class="grid grid-cols-3 gap-4">
      <VSelect v-model="model.provinsi" id="provinsi" name="provinsi" label="Provinsi" required>
        <option value="">Pilih Provinsi</option>
        <option v-for="prov in provinceList" :key="prov.code" :value="prov.code">{{ prov.name }}</option>
      </VSelect>
      <VSelect v-model="model.kota" id="kota" name="kota" label="Kabupaten/Kota" required>
        <option value="">Pilih Kabupaten/Kota</option>
        <option v-for="reg in regencyList" :key="reg.code" :value="reg.code">{{ reg.name }}</option>
      </VSelect>
      <VSelect v-model="model.kecamatan" id="kecamatan" name="kecamatan" label="Kecamatan" required>
        <option value="">Pilih Kecamatan</option>
        <option v-for="dist in districtList" :key="dist.code" :value="dist.code">{{ dist.name }}</option>
      </VSelect>
    </div>

    <!-- Row 3: Lokasi cont'd -->
    <div class="grid grid-cols-3 gap-4">
      <VSelect v-model="model.desa" id="desa" name="desa" label="Desa/Kelurahan" required>
        <option value="">Pilih Desa/Kelurahan</option>
        <option v-for="vil in villageList" :key="vil.code" :value="vil.code">{{ vil.name }}</option>
      </VSelect>
      <VTextArea v-model="model.jalan" id="jalan" name="jalan" label="Alamat Jalan" required class="col-span-2"></VTextArea>
    </div>

    <!-- Row 4: Data Ekstra -->
    <div class="grid grid-cols-3 gap-4">
      <VInput v-model="model.namaKepalaSekolah" id="namaKepalaSekolah" name="namaKepalaSekolah" label="Nama Kepala Sekolah" required />
      <VInput v-model="model.teleponSekolah" id="teleponSekolah" name="teleponSekolah" label="Telepon Sekolah" required />
      <VInput id="tanggalBergabung" name="tanggalBergabung" type="date" v-model="model.tanggalBergabung" label="Tanggal Bergabung" required />
    </div>

    <!-- Submit Button -->
    <div class="flex justify-end gap-2 pt-4">
      <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Batal</VButton>
      <VButton type="submit" :disabled="loading" class="bg-pink-600 hover:bg-pink-800 text-white disabled:opacity-50 cursor-pointer">
        {{ loading ? 'Menyimpan...' : 'Simpan' }}
      </VButton>
    </div>
  </form>
</template>