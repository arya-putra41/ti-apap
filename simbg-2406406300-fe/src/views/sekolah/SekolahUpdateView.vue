<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { sekolahService, type UpdateSekolahRequest } from '@/services/sekolahService';
import { wilayahService, type Wilayah } from '@/services/wilayahService';
import { findWilayahByName } from '@/utils/wilayahMatcher';
import { toast } from 'vue-sonner';
import { format } from 'date-fns';
import UpdateSekolahForm from '@/components/forms/sekolah/UpdateSekolahForm.vue';

const route = useRoute();
const router = useRouter();
const sekolahId = route.params.id as string;

const isLoaded = ref(false);
const isMatchingWilayah = ref(false);
const wilayahWarning = ref('');

const provinces = ref<Wilayah[]>([]);
const cities = ref<Wilayah[]>([]);
const districts = ref<Wilayah[]>([]);
const villages = ref<Wilayah[]>([]);

// Kode dipakai untuk memilih opsi dropdown.
// Nama yang tersimpan tetap berada di sekolahModel.
const provinceCode = ref('');
const cityCode = ref('');
const districtCode = ref('');
const villageCode = ref('');

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

async function autoMatchWilayah() {
    isMatchingWilayah.value = true;
    wilayahWarning.value = '';

    // Reset state pencocokan tanpa mengubah nama yang tersimpan.
    provinceCode.value = '';
    cityCode.value = '';
    districtCode.value = '';
    villageCode.value = '';

    cities.value = [];
    districts.value = [];
    villages.value = [];

    try {
        // LEVEL 1: Provinsi
        provinces.value = await wilayahService.getProvinsi();

        const province = findWilayahByName(
            provinces.value,
            sekolahModel.value.provinsi
        );

        if (!province) {
            wilayahWarning.value =
                `Provinsi "${sekolahModel.value.provinsi}" tidak ditemukan. ` +
                'Silakan pilih ulang wilayah secara manual.';
            return;
        }

        provinceCode.value = province.code;

        // LEVEL 2: Kota/Kabupaten
        cities.value = await wilayahService.getKota(province.code);

        const city = findWilayahByName(
            cities.value,
            sekolahModel.value.kota
        );

        if (!city) {
            wilayahWarning.value =
                `Kota/kabupaten "${sekolahModel.value.kota}" tidak ditemukan. ` +
                'Pencocokan dihentikan. Silakan pilih ulang wilayah secara manual.';
            return;
        }

        cityCode.value = city.code;

        // LEVEL 3: Kecamatan
        districts.value = await wilayahService.getKecamatan(city.code);

        const district = findWilayahByName(
            districts.value,
            sekolahModel.value.kecamatan
        );

        if (!district) {
            wilayahWarning.value =
                `Kecamatan "${sekolahModel.value.kecamatan}" tidak ditemukan. ` +
                'Pencocokan dihentikan. Silakan pilih ulang wilayah secara manual.';
            return;
        }

        districtCode.value = district.code;

        // LEVEL 4: Desa/Kelurahan
        villages.value = await wilayahService.getKelurahan(district.code);

        const village = findWilayahByName(
            villages.value,
            sekolahModel.value.desa
        );

        if (!village) {
            wilayahWarning.value =
                `Desa/kelurahan "${sekolahModel.value.desa}" tidak ditemukan. ` +
                'Pencocokan dihentikan. Silakan pilih ulang wilayah secara manual.';
            return;
        }

        villageCode.value = village.code;

    } catch (error) {
        console.error('Gagal mencocokkan wilayah:', error);

        wilayahWarning.value =
            'Data wilayah gagal dimuat. Nama wilayah sekolah tetap dipertahankan.';
    } finally {
        isMatchingWilayah.value = false;
    }
}

const updateSekolah = async (formData: UpdateSekolahRequest) => {
    const updateRequest = {
        ...formData,
        id: sekolahId
    };

    if (await sekolahService.updateSekolah(updateRequest)) {
        toast.success('Data sekolah diperbarui!');
        router.push('/sekolah');
    } else {
        toast.error('Gagal memperbarui data sekolah. Coba lagi nanti');
    }
};

onMounted(async () => {
    try {
        const data = await sekolahService.findSekolahById(sekolahId);

        const rawDate = data.tanggalBergabung
            ? new Date(data.tanggalBergabung)
            : new Date();

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
            tanggalBergabung: format(rawDate, 'yyyy-MM-dd')
        };

        // Render form setelah data sekolah tersedia.
        isLoaded.value = true;

        // Cocokkan wilayah setelah model sekolah terisi.
        await autoMatchWilayah();

    } catch (error) {
        console.error('Gagal memuat data sekolah:', error);
        toast.error('Gagal memuat data sekolah.');
    }
});
</script>

<template>
    <main class="w-full min-h-screen bg-pink-500/20 pt-24 py-10 px-4 overflow-y-auto">
        <div class="mx-auto w-full bg-white shadow-lg rounded-2xl p-6 px-10 md:p-8 flex flex-col gap-4">
            <h1 class="text-pink-600 font-bold text-xl">
                Ubah Sekolah
            </h1>

            <div
                v-if="wilayahWarning"
                role="alert"
                class="rounded-lg border border-amber-300 bg-amber-50 p-4 text-sm text-amber-800"
            >
                {{ wilayahWarning }}
            </div>

            <div
                v-if="isMatchingWilayah"
                class="text-sm text-slate-500"
            >
                Mencocokkan wilayah dengan data Wilayah.id...
            </div>

            <UpdateSekolahForm
                v-if="isLoaded"
                :profile-model="sekolahModel"
                :action="updateSekolah"
            />

            <div
                v-else
                class="py-10 text-center text-slate-500"
            >
                Memuat data sekolah...
            </div>
        </div>
    </main>
</template>
