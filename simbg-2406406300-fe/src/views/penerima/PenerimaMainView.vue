<script setup lang="ts">
import { RouterLink } from 'vue-router';
import VButton from '@/components/common/VButton.vue';
import { onMounted, nextTick, ref, onBeforeUnmount } from 'vue';
import { penerimaService, type PenerimaBasicResponse } from '@/services/penerimaService';
import { DataTable } from 'simple-datatables';
import VSelect from '@/components/common/VSelect.vue';

import 'simple-datatables/dist/style.css'

const listPenerima = ref<PenerimaBasicResponse[] | null>(null)
const loading = ref(false)
const error = ref('')

const selectedStatus = ref<string>('')

let dt: DataTable | null = null

async function loadListPenerima() {
    loading.value = true
    error.value = ''

    try {
        listPenerima.value = await penerimaService.findAll()
        await buildDataTable()
    } catch (err) {
        console.error(err)
        error.value = 'Gagal memuat halaman penerima, coba lagi nanti'
    } finally {
        loading.value = false
    }
}

const buildDataTable = async () => {
    await nextTick()

    const tableElement = document.getElementById('penerima-table') as HTMLTableElement
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
                placeholder: "Cari penerima...",
                perPage: "entri per halaman",
                noRows: "Data penerima tidak ditemukan",
                noResults: "Data penerima tidak ditemukan",
                info: "Menampilkan {start} sampai {end} dari {rows} penerima",
            }
        })
    }
}

const handleFilterChange = () => {
    if (!dt) return

    if (selectedStatus.value === '') {
        dt.search('')
    } else {
        dt.search(selectedStatus.value)
    }
}

const bindClassForStatus = (status: string): string => {
    switch (status.toUpperCase()) {
        case "DIAJUKAN":
            return 'bg-blue-100 text-blue-800'
        case "TERVERIFIKASI":
            return 'bg-yellow-100 text-yellow-800'
        case "AKTIF":
            return 'bg-green-100 text-green-800'
        case "NONAKTIF":
            return 'bg-gray-100 text-gray-800'
        case "DITOLAK":
            return 'bg-red-100 text-red-800'
        default:
            return 'bg-gray-100 text-gray-800'
    }
}

const formatKondisiKhusus = (val: string | null | undefined): string => {
    if (!val) return '-'
    return val.toLowerCase().split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(" ")
}

onMounted(loadListPenerima)

onBeforeUnmount(() => {
    if (dt) {
        dt.destroy();
        dt = null;
    }
});
</script>

<template>
    <main class="pt-20 w-full min-h-screen">
        <div class="w-full mx-auto flex flex-col gap-6 py-6 px-10 rounded-xl shadow-md">
            <div class="flex justify-between items-center pb-4 border-b">
                <h1 class="text-2xl font-bold text-gray-800">Daftar Penerima Manfaat</h1>
                <div class="flex flex-row gap-2">
                    <RouterLink to="/penerima/create">
                        <VButton class="add-button">Tambah Penerima</VButton>
                    </RouterLink>
                    <RouterLink to="/penerima/antrian-verifikasi">
                        <VButton class="add-button">Antrian Verifikasi</VButton>
                    </RouterLink>
                    <RouterLink to="/penerima/statistik">
                        <VButton class="add-button">Statistik Penerima</VButton>
                    </RouterLink>
                </div>
            </div>

            <div class="flex gap-3">
                <VSelect v-model="selectedStatus" id="status" name="status" label="Status" @change="handleFilterChange">
                    <option value="">Semua Status</option>
                    <option value="DIAJUKAN">Diajukan</option>
                    <option value="TERVERIFIKASI">Terverifikasi</option>
                    <option value="AKTIF">Aktif</option>
                    <option value="NONAKTIF">Nonaktif</option>
                    <option value="DITOLAK">Ditolak</option>
                </VSelect>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data penerima...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadListPenerima" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-show="!loading && !error" class="w-full overflow-x-auto">
                <table id="penerima-table" class="w-full table-fixed text-left border-collapse">
                    <thead>
                        <tr>
                            <th class="py-3 px-4"><span class="flex items-center"> ID </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Nama </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> NISN </span></th>
                            <th data-searchable="false" class="py-3 px-4"><span class="flex items-center"> Rombel/Sekolah </span></th>
                            <th data-searchable="false" class="py-3 px-4"><span class="flex items-center"> Kondisi Khusus </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Status </span></th>
                            <th data-searchable="false" class="py-3 px-4"><span class="flex items-center"> Aksi </span></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="penerima in listPenerima" :key="penerima.id">
                            <td class="font-bold text-gray-900 whitespace-nowrap py-3 px-4">
                                {{ penerima.id }}
                            </td>
                            <td class="py-3 px-4">{{ penerima.nama }}</td>
                            <td class="py-3 px-4">{{ penerima.nisn }}</td>
                            <td class="py-3 px-4">{{ penerima.namaRombel }} - {{ penerima.namaSekolah }}</td>
                            <td class="py-3 px-4">{{ formatKondisiKhusus(penerima.kondisiKhusus) }}</td>
                            <td class="py-3 px-4">
                                <span :class="[
                                    'px-2.5 py-1 text-xs font-semibold rounded-full',
                                    bindClassForStatus(penerima.status)
                                ]">
                                    {{ penerima.status }}
                                </span>
                            </td>
                            <td class="py-3 px-4">
                                <RouterLink :to="`/penerima/${penerima.id}`">
                                    <VButton class="detail-button">Detail</VButton>
                                </RouterLink>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</template>

<style scoped>
@reference "@/assets/main.css";

.add-button {
    @apply bg-pink-600 hover:bg-pink-800 text-white px-4 py-2 rounded-lg font-medium transition;
}

.detail-button {
    @apply bg-pink-600 hover:bg-pink-800 text-white px-3 py-1 rounded text-sm transition;
}

:deep(.datatable-top) {
    @apply flex justify-between items-center pb-4 mb-2 border-b border-gray-100 gap-4;
}

:deep(.datatable-search .datatable-input) {
    @apply border border-gray-300 rounded-lg px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-pink-500 focus:border-pink-500 transition w-64;
}

:deep(.datatable-selector) {
    @apply border border-gray-300 rounded-lg px-2 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-pink-500 transition mr-2;
}

:deep(.datatable-bottom) {
    @apply flex justify-between items-center pt-4 mt-2 border-t border-gray-100 text-sm text-gray-600;
}

:deep(.datatable-pagination .datatable-pagination-list) {
    @apply flex gap-1 items-center;
}

:deep(.datatable-pagination .datatable-pagination-list-item a) {
    @apply px-3 py-1 rounded border border-gray-200 text-sm hover:bg-pink-50 hover:text-pink-600 transition;
}

:deep(.datatable-pagination .datatable-pagination-list-item.datatable-active a) {
    @apply bg-pink-600 border-pink-600 text-white font-semibold;
}

:deep(.datatable-sorter) {
    @apply inline-flex items-center gap-1 cursor-pointer hover:text-pink-600 transition;
}

option {
    @apply w-full;
}
</style>