<script setup lang="ts">
import { RouterLink, useRoute, useRouter } from 'vue-router';
import VButton from '@/components/common/VButton.vue';
import { onMounted, nextTick, ref, onBeforeUnmount } from 'vue';
import { rombelService, type RombelResponse } from '@/services/rombelService';
import { type PenerimaBasicResponse } from '@/services/penerimaService';
import { DataTable } from 'simple-datatables';
import { format } from 'date-fns';

import 'simple-datatables/dist/style.css'

const route = useRoute()
const { id: rombelId } = route.params as { id: string }

const rombel = ref<RombelResponse | null>(null)
const penerimaList = ref<PenerimaBasicResponse[] | null>(null)

const loading = ref(false)
const error = ref('')

let dt: DataTable | null = null

async function loadRombel() {
    loading.value = true
    error.value = ''

    try {
        rombel.value = await rombelService.findRombelById(rombelId)
        penerimaList.value = rombel.value.penerima
        await buildDataTable()
    } catch (err) {
        console.error(err)
        error.value = 'Gagal memuat data rombel, coba lagi nanti'
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
                placeholder: "Cari ...",
                perPage: "entri per halaman",
                noRows: "Data penerima tidak ditemukan",
                noResults: "Data penerima tidak ditemukan",
                info: "Menampilkan {start} sampai {end} dari {rows} penerima",
            }
        })
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

onMounted(loadRombel)

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
                <p class="text-gray-600 font-medium text-sm">Memuat data rombongan belajar...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadRombel" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-if="rombel">
                <!--Title-->
                <div class="w-full flex flex-row justify-between">
                    <div>
                        <h1 class="text-pink-600 font-bold text-2xl">{{ rombel.namaRombel }}</h1>
                        <p class="text-sm">Nama sekolah: {{ rombel.namaSekolah }} | ID: {{ rombel.id }}</p>
                    </div>

                    <!--Actions-->
                    <div class="flex flex-row gap-2">
                        <RouterLink :to="`/rombel/${rombel.id}/update`">
                            <VButton class="border border-black hover:bg-pink-200 text-black">Ubah Data</VButton>
                        </RouterLink>

                        <RouterLink v-if="parseInt(rombel.sisaKuota) > 0 && rombel.status == 'AKTIF'" :to="`/penerima/create?rombelId=${rombelId}`">
                            <VButton class="border border-black hover:bg-pink-200 text-black">Tambah Penerima</VButton>
                        </RouterLink>
                    </div>
                </div>

                <!--Information-->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4 py-6">
                    <div class="md:col-span-2">
                        <span class="text-sm text-gray-500">Status Rombel</span>
                        <p :class="['text-lg font-bold p-2 text-center', rombel.status === 'AKTIF' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800']">{{ rombel.status }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Tingkat</span>
                        <p class="text-lg font-bold">{{ rombel.tingkat }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Tahun Ajaran</span>
                        <p class="text-lg font-bold">{{ rombel.tahunAjaran }}</p>
                    </div>
                    
                    <div>
                        <span class="text-sm text-gray-500">Kuota</span>
                        <p class="text-lg font-bold">{{ rombel.kuotaPenerima }}</p>
                    </div>
                    
                    <div>
                        <span class="text-sm text-gray-500">Terpakai</span>
                        <p class="text-lg font-bold">{{ rombel.jumlahPenerimaTerdaftar }}</p>
                    </div>
                    
                    <div>
                        <span class="text-sm text-gray-500">Sisa Kuota</span>
                        <p class="text-lg font-bold">{{ rombel.sisaKuota }}</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="mx-auto w-full flex flex-col gap-4 bg-white drop-shadow-xl p-6 px-10 md:p-8 rounded-2xl divide-y">
            <h1 class="text-pink-600 font-bold text-2xl">Penerima Manfaat</h1>

            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data penerima manfaat...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadRombel" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
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
                            <th class="py-3 px-4"><span class="flex items-center"> Kondisi Khusus </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Tanggal Pengajuan </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Status </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Aksi </span></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="penerima in penerimaList" :key="penerima.id">
                            <td class="font-bold text-gray-900 whitespace-nowrap py-3 px-4">
                                {{ penerima.id }}
                            </td>
                            <td class="py-3 px-4">{{ penerima.nama }}</td>
                            <td class="py-3 px-4">{{ penerima.nisn }}</td>
                            <td class="py-3 px-4">{{ formatKondisiKhusus(penerima.kondisiKhusus) }}</td>
                            <td class="py-3 px-4">{{ format(new Date(penerima.tanggalPengajuan), 'dd MMM yyyy') }}</td>
                            <td class="py-3 px-4">
                                <span :class="[
                                    'px-2.5 py-1 text-xs font-semibold rounded-full',
                                    bindClassForStatus(penerima.status)
                                ]">
                                    {{ penerima.status }}
                                </span>
                            </td>
                            <td class="py-3 px-4 flex flex-row gap-2">
                                <RouterLink :to="`/penerima/${penerima.id}`">
                                    <VButton class="detail-button">Detail</VButton>
                                </RouterLink>
                                <RouterLink :to="`/penerima/${penerima.id}/update`">
                                    <VButton class="detail-button">Ubah</VButton>
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

.detail-button {
    @apply bg-pink-600 hover:bg-pink-800 text-white px-3 py-1 rounded text-sm transition;
}
</style>