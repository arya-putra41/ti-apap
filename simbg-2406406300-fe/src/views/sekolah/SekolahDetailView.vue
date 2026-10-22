<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router';
import VButton from '@/components/common/VButton.vue';
import { onMounted, nextTick, ref, onBeforeUnmount } from 'vue';
import { sekolahService, type SekolahResponse } from '@/services/sekolahService';
import { DataTable } from 'simple-datatables';
import { format } from 'date-fns';

import 'simple-datatables/dist/style.css'
import { type RombelBasicResponse } from '@/services/rombelService';

const route = useRoute()
const { id: sekolahId } = route.params as { id: string }

const sekolah = ref<SekolahResponse | null>(null)
const rombelList = ref<RombelBasicResponse[] | null>(null)

const loading = ref(false)
const error = ref('')

// State untuk Dialog Modal Nonaktifkan Sekolah
const showDeactivateModal = ref(false)
const isDeactivating = ref(false)
const deactivateError = ref('')

let dt: DataTable | null = null

async function loadSekolah() {
    loading.value = true
    error.value = ''

    try {
        sekolah.value = await sekolahService.findSekolahById(sekolahId)
        rombelList.value = sekolah.value.rombel
        await buildDataTable()
    } catch (err) {
        console.error(err)
        error.value = 'Gagal memuat data sekolah, coba lagi nanti'
    } finally {
        loading.value = false
    }
}

// Buka modal & reset error
const openDeactivateModal = () => {
    deactivateError.value = ''
    showDeactivateModal.value = true
}

// Eksekusi penonaktifan sekolah
const handleDeactivate = async () => {
    isDeactivating.value = true
    deactivateError.value = ''

    try {
        // Sesuaikan nama method service backend (misal: nonaktifkanSekolah atau deactivateSekolah)
        await sekolahService.deactivateSekolah(sekolahId)
        showDeactivateModal.value = false
        window.location.reload()
    } catch (err: any) {
        console.error(err)
        // Menangkap pesan error dari response backend (misal: "Sekolah masih memiliki rombel aktif")
        deactivateError.value = 
            err.response?.data?.message || 
            err.response?.data?.error || 
            'Gagal menonaktifkan sekolah. Pastikan tidak ada rombel yang masih aktif.'
    } finally {
        isDeactivating.value = false
    }
}

const buildDataTable = async () => {
    await nextTick()

    const tableElement = document.getElementById('rombel-table') as HTMLTableElement
    if (tableElement && typeof DataTable !== 'undefined') {
        if (dt) {
            dt.destroy()
            dt = null
        }
        dt = new DataTable(tableElement, {
            searchable: true,
            sortable: true,
            perPage: 10,
            perPageSelect: [5, 10, 25, 50],
            labels: {
                placeholder: "Cari rombel...",
                perPage: "entri per halaman",
                noRows: "Data rombel tidak ditemukan",
                noResults: "Data rombel tidak ditemukan",
                info: "Menampilkan {start} sampai {end} dari {rows} rombel",
            }
        })
    }
}

onMounted(loadSekolah)

onBeforeUnmount(() => {
    if (dt) {
        dt.destroy();
        dt = null;
    }
});
</script>

<template>
    <main class="flex flex-col gap-5 w-full min-h-screen pt-24 py-10 px-4 overflow-y-auto">
        <div class="mx-auto w-full flex flex-col gap-4 bg-white drop-shadow-xl p-6 px-10 md:p-8 rounded-2xl divide-y">
            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data sekolah...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadSekolah" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-if="sekolah">
                <!--Title-->
                <div class="w-full flex flex-row justify-between">
                    <div>
                        <h1 class="text-pink-600 font-bold text-2xl">{{ sekolah.namaSekolah }}</h1>
                        <p class="text-sm">ID: {{ sekolah.id }} | NPSN: {{ sekolah.npsn }}</p>
                    </div>

                    <!--Actions-->
                    <div class="flex flex-row gap-2">
                        <RouterLink :to="`/sekolah/${sekolah.id}/update`">
                            <VButton class="border border-black hover:bg-pink-200 text-black">Ubah Data Sekolah</VButton>
                        </RouterLink>

                        <RouterLink :to="`/rombel/create?sekolahId=${sekolah.id}`">
                            <VButton class="border border-black hover:bg-pink-200 text-black">Tambah Rombel</VButton>
                        </RouterLink>

                        <!-- Tombol Pemicu Modal Nonaktifkan -->
                        <span>
                            <VButton v-if="sekolah.statusMitra === 'AKTIF'" @click="openDeactivateModal" class="bg-red-600 hover:bg-red-800 text-white font-semibold cursor-pointer">
                                Nonaktifkan Mitra
                            </VButton>
                        </span>
                    </div>
                </div>

                <!--Information-->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4 py-6">
                    <div class="md:col-span-2">
                        <span class="text-sm text-gray-500">Status Mitra</span>
                        <p :class="['text-lg font-bold p-2 text-center', sekolah.statusMitra === 'AKTIF' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800']">{{ sekolah.statusMitra }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Jenjang</span>
                        <p class="text-lg font-bold">{{ sekolah.jenjang }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Nama Kepala Sekolah</span>
                        <p class="text-lg font-bold">{{ sekolah.namaKepalaSekolah }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Telepon Sekolah</span>
                        <p class="text-lg font-bold">{{ sekolah.teleponSekolah }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Tanggal Bergabung</span>
                        <p class="text-lg font-bold">{{ format(new Date(sekolah.tanggalBergabung), 'd MMM yyyy') }}</p>
                    </div>

                    <!--Location-->
                    <div>
                        <span class="text-sm text-gray-500">Provinsi</span>
                        <p class="text-lg font-bold">{{ sekolah.provinsi }}</p>
                    </div>
                    <div>
                        <span class="text-sm text-gray-500">Kota/Kabupaten</span>
                        <p class="text-lg font-bold">{{ sekolah.kota }}</p>
                    </div>
                    <div>
                        <span class="text-sm text-gray-500">Kecamatan</span>
                        <p class="text-lg font-bold">{{ sekolah.kecamatan }}</p>
                    </div>
                    <div>
                        <span class="text-sm text-gray-500">Desa</span>
                        <p class="text-lg font-bold">{{ sekolah.desa }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Alamat Jalan</span>
                        <p class="text-lg font-bold">{{ sekolah.jalan }}</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="mx-auto w-full flex flex-col gap-4 bg-white drop-shadow-xl p-6 px-10 md:p-8 rounded-2xl divide-y">
            <h1 class="text-pink-600 font-bold text-2xl">Rombongan Belajar</h1>

            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data rombongan belajar...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadSekolah" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-show="!loading && !error" class="w-full overflow-x-auto">
                <table id="rombel-table" class="w-full table-fixed text-left border-collapse">
                    <thead>
                        <tr>
                            <th class="py-3 px-4"><span class="flex items-center"> Nama Rombel </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Tahun Ajaran </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Kuota </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Terpakai </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Status </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Aksi </span></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="rombel in rombelList" :key="rombel.id">
                            <td class="font-bold text-gray-900 whitespace-nowrap py-3 px-4">
                                {{ rombel.namaRombel }}
                            </td>
                            <td class="py-3 px-4">{{ rombel.tahunAjaran }}</td>
                            <td class="py-3 px-4">{{ rombel.kuotaPenerima }}</td>
                            <td class="py-3 px-4">{{ rombel.jumlahPenerimaTerdaftar }}</td>
                            <td class="py-3 px-4">
                                <span :class="[
                                    'px-2.5 py-1 text-xs font-semibold rounded-full',
                                    rombel.status === 'AKTIF' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                                ]">
                                    {{ rombel.status }}
                                </span>
                            </td>
                            <td class="py-3 px-4 flex flex-row gap-2">
                                <RouterLink :to="`/rombel/${rombel.id}`">
                                    <VButton class="detail-button">Detail</VButton>
                                </RouterLink>
                                <RouterLink :to="`/rombel/${rombel.id}/update`">
                                    <VButton class="detail-button">Ubah</VButton>
                                </RouterLink>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Dialog Modal Nonaktifkan Sekolah -->
        <div 
            v-if="showDeactivateModal" 
            class="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-4 backdrop-blur-sm"
        >
            <div class="bg-white rounded-2xl max-w-md w-full p-6 shadow-2xl flex flex-col gap-4">
                <h2 class="text-xl font-bold text-gray-900">Konfirmasi Nonaktifkan Sekolah</h2>
                
                <p class="text-gray-600 text-sm">
                    Apakah Anda yakin ingin menonaktifkan mitra sekolah 
                    <span class="font-bold text-gray-900">{{ sekolah?.namaSekolah }}</span>?
                </p>

                <!-- Pesan Error dari Backend jika Gagal (Misal: Masih ada Rombel Aktif) -->
                <div v-if="deactivateError" class="p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded-xl flex items-start gap-2">
                    <svg class="w-5 h-5 text-red-500 shrink-0 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                    </svg>
                    <span>{{ deactivateError }}</span>
                </div>

                <div class="flex justify-end gap-3 pt-2">
                    <VButton 
                        type="button" 
                        @click="showDeactivateModal = false" 
                        :disabled="isDeactivating"
                        class="bg-gray-200 hover:bg-gray-300 text-gray-800"
                    >
                        Batal
                    </VButton>
                    <VButton 
                        type="button" 
                        @click="handleDeactivate" 
                        :disabled="isDeactivating"
                        class="bg-red-600 hover:bg-red-700 text-white font-semibold flex items-center gap-2"
                    >
                        <span v-if="isDeactivating" class="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></span>
                        {{ isDeactivating ? 'Memproses...' : 'Nonaktifkan' }}
                    </VButton>
                </div>
            </div>
        </div>
    </main>
</template>

<style scoped>
@reference "@/assets/main.css";

.detail-button {
    @apply bg-pink-600 hover:bg-pink-800 text-white px-3 py-1 rounded text-sm transition;
}
</style>