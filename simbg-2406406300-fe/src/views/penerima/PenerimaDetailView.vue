<script setup lang="ts">
import { RouterLink, useRoute } from 'vue-router';
import VButton from '@/components/common/VButton.vue';
import { onMounted, nextTick, ref, onBeforeUnmount } from 'vue';
import { penerimaService, type PenerimaResponse } from '@/services/penerimaService';
import { DataTable } from 'simple-datatables';
import { format } from 'date-fns';

import 'simple-datatables/dist/style.css'
import { type RiwayatResponse } from '@/services/riwayatService';

const route = useRoute()
const { id: penerimaId } = route.params as { id: string }

const penerima = ref<PenerimaResponse | null>(null)
const riwayatList = ref<RiwayatResponse[] | null>(null)

const loading = ref(false)
const error = ref('')

// State untuk Dialog Modal Aktivasi
const showActivateModal = ref(false)
const isActivating = ref(false)

let dt: DataTable | null = null

async function loadPenerima() {
    loading.value = true
    error.value = ''

    try {
        penerima.value = await penerimaService.findPenerimaById(penerimaId)
        riwayatList.value = penerima.value.riwayat
        await buildDataTable()
    } catch (err) {
        console.error(err)
        error.value = 'Gagal memuat data penerima, coba lagi nanti'
    } finally {
        loading.value = false
    }
}

// Fungsi eksekusi aktivasi
const handleActivate = async () => {
    isActivating.value = true
    try {
        // Sesuaikan nama method service dengan backend kamu (contoh: activatePenerima)
        await penerimaService.activatePenerima(penerimaId)
        showActivateModal.value = false
        window.location.reload()
    } catch (err) {
        console.error(err)
        alert('Gagal mengaktifkan penerima manfaat.')
    } finally {
        isActivating.value = false
    }
}

const buildDataTable = async () => {
    await nextTick()

    const tableElement = document.getElementById('riwayat-table') as HTMLTableElement
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
                noRows: "Data riwayat tidak ditemukan",
                noResults: "Data riwayat tidak ditemukan",
                info: "Menampilkan {start} sampai {end} dari {rows} riwayat",
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

onMounted(loadPenerima)

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
                <p class="text-gray-600 font-medium text-sm">Memuat data penerima...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadPenerima" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-if="penerima">
                <!--Title-->
                <div class="w-full flex flex-row justify-between">
                    <div>
                        <h1 class="text-pink-600 font-bold text-2xl">{{ penerima.nama }}</h1>
                        <p class="text-sm">NISN: {{ penerima.nisn }} | ID: {{ penerima.id }}</p>
                    </div>

                    <!--Actions-->
                    <div class="flex flex-row gap-2">
                        <RouterLink :to="`/penerima/${penerima.id}/verifikasi`" v-if="penerima.status === 'DIAJUKAN'">
                            <VButton class="bg-yellow-100 hover:bg-yellow-200 text-yellow-800">Verifikasi</VButton>
                        </RouterLink>

                        <!-- Tombol Aktivasi Pemicu Modal -->
                        <span>
                            <VButton v-if="penerima.status === 'TERVERIFIKASI'" @click="showActivateModal = true" class="bg-green-100 hover:bg-green-200 text-green-800 cursor-pointer">
                                Aktivasi
                            </VButton>
                        </span>

                        <RouterLink :to="`/penerima/${penerima.id}/tolak`" v-if="penerima.status === 'DIAJUKAN' || penerima.status === 'TERVERIFIKASI'">
                            <VButton class="bg-red-100 hover:bg-red-200 text-red-800">Tolak</VButton>
                        </RouterLink>

                        <RouterLink :to="`/penerima/${penerima.id}/nonaktifkan`" v-if="penerima.status === 'AKTIF'">
                            <VButton class="bg-red-100 hover:bg-red-200 text-red-800">Nonaktifkan</VButton>
                        </RouterLink>

                        <RouterLink :to="`/penerima/${penerima.id}/tinjau-ulang`" v-if="penerima.status === 'TERVERIFIKASI'">
                            <VButton class="bg-blue-100 hover:bg-blue-200 text-blue-800">Tinjau Ulang</VButton>
                        </RouterLink>

                        <RouterLink :to="`/penerima/${penerima.id}/update`" v-if="penerima.status != 'DITOLAK' && penerima.status != 'NONAKTIF'">
                            <VButton class="border border-black hover:bg-pink-200 text-black">Ubah Data</VButton>
                        </RouterLink>
                    </div>
                </div>

                <!--Information-->
                <div class="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4 py-6">
                    <div class="md:col-span-2">
                        <span class="text-sm text-gray-500">Status Penerima</span>
                        <p :class="['text-lg font-bold p-2 text-center', bindClassForStatus(penerima.status)]">{{ penerima.status }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Jenis Kelamin</span>
                        <p class="text-lg font-bold">{{ penerima.jenisKelamin === 'L' ? 'Laki-laki' : 'Perempuan' }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Tanggal Lahir</span>
                        <p class="text-lg font-bold">{{ format(new Date(penerima.tanggalLahir), 'd MMM yyyy') }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Rombel</span>
                        <p class="text-lg font-bold">{{ penerima.namaRombel }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Sekolah</span>
                        <p class="text-lg font-bold">{{ penerima.namaSekolah }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Kondisi Khusus</span>
                        <p class="text-lg font-bold">{{ formatKondisiKhusus(penerima.kondisiKhusus) }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Tanggal Pengajuan</span>
                        <p class="text-lg font-bold">{{ format(new Date(penerima.tanggalPengajuan), 'd MMM yyyy, hh:mm') }}</p>
                    </div>

                    <div class="md:col-span-2">
                        <span class="text-sm text-gray-500">Keterangan Kondisi</span>
                        <p class="text-lg font-bold">{{ penerima.keterangan ? penerima.keterangan : "Tidak Ada" }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Tanggal Verifikasi</span>
                        <p class="text-lg font-bold">{{ penerima.tanggalVerifikasi ? format(new Date(penerima.tanggalVerifikasi), 'd MMM yyyy, hh:mm') : 'Belum Diverifikasi' }}</p>
                    </div>

                    <div>
                        <span class="text-sm text-gray-500">Petugas Verifikasi</span>
                        <p class="text-lg font-bold">{{ penerima.petugasVerifikasi ? penerima.petugasVerifikasi.nama + " - " + penerima.petugasVerifikasi.wilayahTugas : 'Belum Diverifikasi' }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tabel Riwayat -->
        <div class="mx-auto w-full flex flex-col gap-4 bg-white drop-shadow-xl p-6 px-10 md:p-8 rounded-2xl divide-y">
            <h1 class="text-pink-600 font-bold text-2xl">Riwayat</h1>

            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data riwayat...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="loadPenerima" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-show="!loading && !error" class="w-full overflow-x-auto">
                <table id="riwayat-table" class="w-full table-fixed text-left border-collapse">
                    <thead>
                        <tr>
                            <th class="py-3 px-4"><span class="flex items-center"> Tanggal </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Perpindahan Status </span></th>
                            <th class="py-3 px-4"><span class="flex items-center"> Keterangan </span></th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="riwayat in riwayatList" :key="riwayat.id">
                            <td class="font-bold text-gray-900 whitespace-nowrap py-3 px-4">
                                {{ format(new Date(riwayat.tanggalPerubahan), 'dd MMM yyyy, hh:mm') }}
                            </td>
                            <td class="py-3 px-4 flex flex-row gap-1 items-center">
                                <div v-if="riwayat.statusSebelumnya" :class="[
                                    'px-2.5 py-1 text-xs font-semibold rounded-full',
                                    bindClassForStatus(riwayat.statusSebelumnya)
                                ]">
                                    {{ riwayat.statusSebelumnya }}
                                </div>
                                <span v-if="riwayat.statusSebelumnya">→</span>
                                <div :class="[
                                    'px-2.5 py-1 text-xs font-semibold rounded-full',
                                    bindClassForStatus(riwayat.statusBaru)
                                ]">
                                    {{ riwayat.statusBaru }}
                                </div>
                            </td>
                            <td class="py-3 px-4 font-bold">{{ riwayat.keterangan }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Dialog Modal Aktivasi -->
        <div v-if="showActivateModal" class="fixed inset-0 z-50 bg-black/50 flex items-center justify-center p-4 backdrop-blur-sm">
            <div class="bg-white rounded-2xl max-w-md w-full p-6 shadow-2xl flex flex-col gap-4">
                <h2 class="text-xl font-bold text-gray-900">Konfirmasi Aktivasi</h2>
                <p class="text-gray-600 text-sm">
                    Apakah Anda yakin ingin mengaktifkan penerima manfaat
                    <span class="font-bold text-gray-900">{{ penerima?.nama }}</span>?
                </p>

                <div class="flex justify-end gap-3 pt-2">
                    <VButton type="button" @click="showActivateModal = false" :disabled="isActivating" class="bg-gray-200 hover:bg-gray-300 text-gray-800">
                        Batal
                    </VButton>
                    <VButton type="button" @click="handleActivate" :disabled="isActivating" class="bg-green-600 hover:bg-green-700 text-white font-semibold flex items-center gap-2">
                        <span v-if="isActivating" class="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin"></span>
                        {{ isActivating ? 'Memproses...' : 'Aktivasi' }}
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