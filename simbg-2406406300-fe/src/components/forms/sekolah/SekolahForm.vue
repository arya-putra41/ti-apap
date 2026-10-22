<script setup lang="ts">
import VInput from '../../common/VInput.vue';
import VTextArea from '../../common/VTextArea.vue';
import VButton from '../../common/VButton.vue';
import VSelect from '../../common/VSelect.vue';
import { onMounted, type PropType, ref, toRefs, watch } from 'vue';
import { useRouter } from 'vue-router';
import { type CreateSekolahRequest } from '@/services/sekolahService.ts';
import { wilayahService, type Wilayah } from '@/services/wilayahService.ts';

const router = useRouter();

const props = defineProps({
    action: {
        type: Function as PropType<(data: CreateSekolahRequest) => Promise<void>>,
        required: true,
    },
    profileModel: {
        type: Object as PropType<CreateSekolahRequest>,
        required: true,
    }
})
const model = toRefs(props).profileModel;
const emit = defineEmits(['update:modelValue'])

const provinceList = ref<Wilayah[]>([])
const regencyList = ref<Wilayah[]>([])
const districtList = ref<Wilayah[]>([])
const villageList = ref<Wilayah[]>([])

const selectedProvinceCode = ref('')
const selectedRegencyCode = ref('')
const selectedDistrictCode = ref('')

// State untuk menangkap error dan status loading
const errorMessage = ref('')
const loading = ref(false)

watch(() => model, (newValue) => {
    emit('update:modelValue', newValue);
}, { deep: true }
)

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
            'Terjadi kesalahan saat menyimpan data sekolah. Silakan periksa kembali isian Anda.'
    } finally {
        loading.value = false
    }
}

onMounted(async () => {
    provinceList.value = await wilayahService.getProvinsi()
})

// provinsi
watch(selectedProvinceCode, async (newProvinceCode) => {
    model.value.provinsi =
        provinceList.value.find(p => p.code === newProvinceCode)?.name ?? ''

    // Reset tingkat wilayah di bawahnya
    selectedRegencyCode.value = ''
    selectedDistrictCode.value = ''

    model.value.kota = ''
    model.value.kecamatan = ''
    model.value.desa = ''

    regencyList.value = []
    districtList.value = []
    villageList.value = []

    if (newProvinceCode) {
        regencyList.value =
            await wilayahService.getKota(newProvinceCode)
    }
})

// kota
watch(selectedRegencyCode, async (newRegencyCode) => {
    model.value.kota =
        regencyList.value.find(r => r.code === newRegencyCode)?.name ?? ''

    selectedDistrictCode.value = ''
    model.value.kecamatan = ''
    model.value.desa = ''

    districtList.value = []
    villageList.value = []

    if (newRegencyCode) {
        districtList.value =
            await wilayahService.getKecamatan(newRegencyCode)
    }
})

// kecamatan
watch(selectedDistrictCode, async (newDistrictCode) => {
    model.value.kecamatan =
        districtList.value.find(d => d.code === newDistrictCode)?.name ?? ''

    model.value.desa = ''
    villageList.value = []

    if (newDistrictCode) {
        villageList.value =
            await wilayahService.getKelurahan(newDistrictCode)
    }
})
</script>

<template>
    <form @submit.prevent="handleSubmit" class="flex flex-col gap-6 py-4">
        
        <!-- Banner Error Alert -->
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

        <!--Row 1: NPSN, Nama, Jenjang-->
        <div class="grid grid-cols-3 gap-4">
            <VInput v-model="model.npsn" id="npsn" name="npsn" label="NPSN (8 digit)" required/>
            <VInput v-model="model.namaSekolah" id="namaSekolah" name="namaSekolah" label="Nama Sekolah" required/>
            <VSelect v-model="model.jenjang" id="jenjang" name="jenjang" label="Jenjang" required>
                <option value="">Pilih Jenjang</option>
                <option value="SD">SD</option>
                <option value="SMP">SMP</option>
                <option value="SMA/SMK">SMA/SMK</option>
            </VSelect>
        </div>

        <!-- Row 2: Lokasi -->
        <div class="grid grid-cols-3 gap-4">
            <VSelect v-model="selectedProvinceCode" id="provinsi" name="provinsi" label="Provinsi" required>
                <option value="">Pilih Provinsi</option>
                <option v-for="prov in provinceList" :key="prov.code" :value="prov.code">{{ prov.name }}</option>
            </VSelect>
            <VSelect v-model="selectedRegencyCode" id="kota" name="kota" label="Kabupaten/Kota" required>
                <option value="">Pilih Kabupaten/Kota</option>
                <option v-for="reg in regencyList" :key="reg.code" :value="reg.code">{{ reg.name }}</option>
            </VSelect>
            <VSelect v-model="selectedDistrictCode" id="kecamatan" name="kecamatan" label="Kecamatan" required>
                <option value="">Pilih Kecamatan</option>
                <option v-for="dist in districtList" :key="dist.code" :value="dist.code">{{ dist.name }}</option>
            </VSelect>
        </div>

        <!--Row 3: Lokasi cont'd-->
        <div class="grid grid-cols-3 gap-4">
            <VSelect v-model="model.desa" id="desa" name="desa" label="Desa/Kelurahan" required>
                <option value="">Pilih Desa/Kelurahan</option>
                <option v-for="vil in villageList" :key="vil.code" :value="vil.name">{{ vil.name }}</option>
            </VSelect>
            <VTextArea v-model="model.jalan" id="jalan" name="jalan" label="Alamat Jalan" required class="col-span-2"></VTextArea>
        </div>

        <!--Row 4: Data Ekstra-->
        <div class="grid grid-cols-3 gap-4">
            <VInput v-model="model.namaKepalaSekolah" id="namaKepalaSekolah" name="namaKepalaSekolah" label="Nama Kepala Sekolah" required/>
            <VInput v-model="model.teleponSekolah" id="teleponSekolah" name="teleponSekolah" label="Telepon Sekolah" required/>
            <VInput id="tanggalBergabung" name="tanggalBergabung" type="date" v-model="model.tanggalBergabung" label="Tanggal Bergabung" required/>
        </div>

        <!--Submit Button-->
        <div class="flex justify-end gap-2 pt-4">
            <VButton @click="router.back()" type="button" class="bg-slate-600 hover:bg-slate-800 text-white">Batal</VButton>
            <VButton type="submit" :disabled="loading" class="bg-pink-600 hover:bg-pink-800 text-white disabled:opacity-50 cursor-pointer">
                {{ loading ? 'Menyimpan...' : 'Simpan' }}
            </VButton>
        </div>
    </form>
</template>