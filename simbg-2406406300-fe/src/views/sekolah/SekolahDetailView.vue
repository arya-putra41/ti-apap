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
                            <VButton class="border border-black hover:bg-pink-200 text-black">Edit Sekolah</VButton>
                        </RouterLink>

                        <RouterLink :to="`/rombel/create?sekolahId=${sekolah.id}`" >
                            <VButton class="border border-black hover:bg-pink-200 text-black">Tambah Rombel</VButton>
                        </RouterLink>

                        <RouterLink :to="`/sekolah/${sekolah.id}/nonaktifkan`">
                            <VButton class="bg-red-600 hover:bg-red-800 text-white font-semibold">Nonaktifkan Mitra</VButton>
                        </RouterLink>
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
                            <td class="py-3 px-4">{{ rombel.sisaKuota }}</td>
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
    </main>
</template>

<style scoped>
@reference "@/assets/main.css";

.detail-button {
    @apply bg-pink-600 hover:bg-pink-800 text-white px-3 py-1 rounded text-sm transition;
}
</style>