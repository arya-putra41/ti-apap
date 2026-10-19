<script setup lang="ts">
import { RouterLink } from 'vue-router';
import VButton from '@/components/common/VButton.vue';
import { onMounted, nextTick, ref, onBeforeUnmount } from 'vue';
import { sekolahService, type SekolahBasicResponse } from '@/services/sekolahService';
import { DataTable } from 'simple-datatables';
import VSelect from '@/components/common/VSelect.vue';

import 'simple-datatables/dist/style.css'

const listSekolah = ref<SekolahBasicResponse[] | null>(null)
const loading = ref(false)
const error = ref('')

const selectedJenjang = ref<string>('')

let dt: DataTable | null = null

async function loadListSekolah() {
    loading.value = true
    error.value = ''

    try {
        listSekolah.value = await sekolahService.findAll()
        await buildDataTable()
    } catch (err) {
        console.error(err)
        error.value = 'Gagal memuat halaman sekolah, coba lagi nanti'
    } finally {
        loading.value = false
    }
}

const buildDataTable = async () => {
    await nextTick()

    const tableElement = document.getElementById('sekolah-table') as HTMLTableElement
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
                placeholder: "Cari sekolah...",
                perPage: "entri per halaman",
                noRows: "Data sekolah tidak ditemukan",
                noResults: "Data sekolah tidak ditemukan",
                info: "Menampilkan {start} sampai {end} dari {rows} sekolah",
            }
        })
    }
}

const handleFilterChange = () => {
    if (!dt) return

    if (selectedJenjang.value === '') {
        dt.search('')
    } else {
        dt.search(selectedJenjang.value)
    }
}

onMounted(loadListSekolah)

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
                <h1 class="text-2xl font-bold text-gray-800">Daftar Sekolah</h1>
                <RouterLink to="/sekolah/create">
                    <VButton class="add-button">Tambah Sekolah</VButton>
                </RouterLink>
            </div>

            <div class="flex gap-3">
                <VSelect v-model="selectedJenjang" id="jenjang" name="jenjang" label="Jenjang" @change="handleFilterChange">
                    <option value="">Semua Jenjang</option>
                    <option value="SD">SD</option>
                    <option value="SMP">SMP</option>
                    <option value="SMA">SMA / SMK</option>
                </VSelect>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data sekolah...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadListSekolah"
                    class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-show="!loading && !error" class="w-full overflow-x-auto">
                <table id="sekolah-table" class="w-full table-fixed text-left border-collapse">
                    <thead>
                        <tr>
                            <th data-searchable="false" class="py-3 px-4"><span class="flex items-center"> ID </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> NPSN </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Nama Sekolah </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Jenjang </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Status Mitra </span></th>
                            <th data-searchable="false" class="py-3 px-4"><span class="flex items-center"> Aksi </span></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="s in listSekolah" :key="s.id">
                            <td class="font-bold text-gray-900 whitespace-nowrap py-3 px-4">
                                {{ s.id }}
                            </td>
                            <td class="py-3 px-4">{{ s.npsn }}</td>
                            <td class="py-3 px-4">{{ s.namaSekolah }}</td>
                            <td class="py-3 px-4">{{ s.jenjang }}</td>
                            <td class="py-3 px-4">
                                <span :class="[
                                    'px-2.5 py-1 text-xs font-semibold rounded-full',
                                    s.statusMitra === 'AKTIF' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                                ]">
                                    {{ s.statusMitra }}
                                </span>
                            </td>
                            <td class="py-3 px-4">
                                <RouterLink :to="`/sekolah/${s.id}`">
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
    @apply bg-blue-600 hover:bg-blue-800 text-white px-3 py-1 rounded text-sm transition;
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