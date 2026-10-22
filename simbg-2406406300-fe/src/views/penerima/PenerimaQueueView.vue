<script setup lang="ts">
import { RouterLink } from 'vue-router';
import VButton from '@/components/common/VButton.vue';
import { onMounted, nextTick, ref, onBeforeUnmount } from 'vue';
import { penerimaService, type PenerimaQueueResponse, type PenerimaQueueWrapper } from '@/services/penerimaService';
import { DataTable } from 'simple-datatables';
import VSelect from '@/components/common/VSelect.vue';

import 'simple-datatables/dist/style.css'
import { format } from 'date-fns';

const responsePenerima = ref<PenerimaQueueWrapper | null>(null)
const listPenerima = ref<PenerimaQueueResponse[] | null>(null)
const maxDays = ref(0)

const loading = ref(false)
const error = ref('')

let dt: DataTable | null = null

async function loadListPenerima() {
    loading.value = true
    error.value = ''

    try {
        responsePenerima.value = await penerimaService.getQueue()
        listPenerima.value = responsePenerima.value.queue
        maxDays.value = responsePenerima.value.maxDays

        await buildDataTable()
    } catch (err) {
        console.error(err)
        error.value = 'Gagal memuat penerima, coba lagi nanti'
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
            searchable: false,
            sortable: false,
            perPage: 10,
            perPageSelect: [5, 10, 25, 50],
            labels: {
                perPage: "entri per halaman",
                noRows: "Data penerima tidak ditemukan",
                noResults: "Data penerima tidak ditemukan",
                info: "Menampilkan {start} sampai {end} dari {rows} penerima",
            }
        })
    }
}

const bindClassForUrgency = (jumlahHariMenunggu: number): string => {
    if (maxDays.value - jumlahHariMenunggu <= 0) {
        return "bg-red-900 text-white"
    } else if (maxDays.value - jumlahHariMenunggu <= 2) {
        return "bg-red-300 text-red-900"
    } else if (maxDays.value - jumlahHariMenunggu <= 4) {
        return "bg-orange-200 text-orange-900"
    } else if (maxDays.value - jumlahHariMenunggu <= 7) {
        return "bg-yellow-200 text-yellow-900"
    } else {
        return ""
    }
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
            <div class="flex flex-col justify-between items-left pb-4 border-b">
                <h1 class="text-2xl font-bold text-gray-800">Antrian Verifikasi</h1>
                <h3 class="text-md text-gray-600">Penerima akan ditolak otomatis setelah menunggu verifikasi selama {{ maxDays }} hari</h3>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data penerima...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadListPenerima"
                    class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-show="!loading && !error" class="w-full overflow-x-auto">
                <table id="penerima-table" class="w-full table-fixed text-left border-collapse">
                    <thead>
                        <tr>
                            <th class="py-3 px-4"><span class="flex items-center"> ID </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Nama </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Rombel/Sekolah </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Tanggal Pengajuan </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Hari Menunggu </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Aksi </span></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="penerima in listPenerima" :key="penerima.id">
                            <td class="font-bold text-gray-900 whitespace-nowrap py-3 px-4">
                                {{ penerima.id }}
                            </td>
                            <td class="py-3 px-4">{{ penerima.nama }}</td>
                            <td class="py-3 px-4">{{ penerima.namaRombel }} - {{ penerima.namaSekolah }}</td>
                            <td class="py-3 px-4">{{ format(new Date(penerima.tanggalPengajuan), "d MMM yyyy") }}</td>
                            <td :class="['py-3 px-4', bindClassForUrgency(penerima.jumlahHariMenunggu)]">{{ penerima.jumlahHariMenunggu }} hari</td>
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