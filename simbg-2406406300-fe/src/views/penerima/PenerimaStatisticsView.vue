<script setup lang="ts">
import Chart from 'chart.js/auto';
import { penerimaService, type PenerimaStatistics } from '@/services/penerimaService';
import VButton from '@/components/common/VButton.vue';
import VSelect from '@/components/common/VSelect.vue';
import { nextTick, onMounted, onBeforeUnmount, ref } from 'vue';
import { DataTable } from 'simple-datatables';

const chartCanvas = ref<HTMLCanvasElement | null>(null)
const chartData = ref<PenerimaStatistics | null>(null)

const loading = ref(false)
const error = ref('')

const selectedPeriode = ref<'BULANAN' | 'KUARTAL'>('BULANAN')
const selectedYear = ref<string>(new Date().getFullYear().toString())

// Opsi list tahun (misal: 3 tahun terakhir hingga tahun depan)
const currentYear = new Date().getFullYear()
const listTahun = ref<number[]>([
    currentYear - 2,
    currentYear - 1,
    currentYear,
    currentYear + 1,
])

let dt: DataTable | null = null
let chart: Chart | null = null

async function getChartData() {
    loading.value = true
    error.value = ''

    try {
        chartData.value = await penerimaService.getPenerimaStatistics(selectedPeriode.value, selectedYear.value)
    } catch (err) {
        console.error(err)
        error.value = 'Gagal memuat data statistik, coba lagi nanti'
    } finally {
        loading.value = false

        await nextTick()
        drawChart()
    }
}

const drawChart = () => {
    // Pastikan ref canvas dan data tersedia
    if (!chartCanvas.value || !chartData.value) return

    if (chart) {
        chart.destroy()
        chart = null
    }

    chart = new Chart(chartCanvas.value, {
        type: "bar",
        data: {
            labels: chartData.value.labels,
            datasets: [
                {
                    label: "Diajukan",
                    data: chartData.value.diajukan,
                    backgroundColor: "#93c5fd"
                },
                {
                    label: "Diverifikasi",
                    data: chartData.value.diverifikasi,
                    backgroundColor: "#fef08a"
                },
                {
                    label: "Ditolak",
                    data: chartData.value.ditolak,
                    backgroundColor: "#fca5a5"
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false
        }
    })
}

onMounted(getChartData)

// Bersihkan memori saat komponen di-unmount
onBeforeUnmount(() => {
    if (chart) {
        chart.destroy()
        chart = null
    }
    if (dt) {
        dt.destroy()
        dt = null
    }
})
</script>

<template>
    <main class="pt-20 w-full min-h-screen">
        <div class="w-full mx-auto flex flex-col gap-6 py-6 px-10 rounded-xl shadow-md">
            <div class="flex flex-col justify-between items-left pb-4 border-b">
                <h1 class="text-2xl font-bold text-gray-800">Statistik Penerima</h1>
            </div>

            <div v-if="loading" class="flex flex-col items-center justify-center py-16 gap-3">
                <div class="w-10 h-10 border-4 border-pink-500 border-t-transparent rounded-full animate-spin">
                </div>
                <p class="text-gray-600 font-medium text-sm">Memuat data penerima...</p>
            </div>

            <div v-else-if="error" class="p-4 bg-red-50 border border-red-200 text-red-600 rounded-lg text-center">
                <p>{{ error }}</p>
                <button @click="getChartData" class="mt-2 px-4 py-1.5 bg-red-600 text-white text-sm rounded-md hover:bg-red-700 transition">
                    Coba Lagi
                </button>
            </div>

            <div v-show="!loading && !error" class="w-full overflow-x-auto flex flex-col gap-2">
                <!-- Filter Periode & Tahun -->
                <div class="flex flex-row items-center gap-4 py-2">
                    <div>
                        <VSelect v-model="selectedPeriode" id="periode" name="periode" label="Jenis Periode" @change="getChartData">
                            <option value="BULANAN">Bulanan</option>
                            <option value="KUARTAL">Kuartal</option>
                        </VSelect>
                    </div>

                    <div>
                        <VSelect v-model="selectedYear" id="year" name="year" label="Tahun" @change="getChartData">
                            <option v-for="tahun in listTahun" :key="tahun" :value="tahun">
                                {{ tahun }}
                            </option>
                        </VSelect>
                    </div>
                </div>

                <!--Grafik-->
                <div class="pb-20">
                    <h1 class="text-xl font-bold">Grafik</h1>
                    <div>
                        <canvas class="h-100 relative" ref="chartCanvas"></canvas>
                    </div>
                </div>

                <!--Rincian per Periode-->
                <div>
                    <h1 class="text-xl font-bold">Rincian per Periode</h1>
                    <table id="rincian-table" class="w-full table-fixed text-left border-collapse" v-if="chartData">
                        <thead class="text-lg border-b">
                            <tr>
                                <th class="py-3 px-4"><span class="flex items-center"> Periode </span></th>
                                <th class="py-3 px-4"><span class="flex items-center"> Diajukan </span></th>
                                <th class="py-3 px-4"><span class="flex items-center"> Diverifikasi </span></th>
                                <th class="py-3 px-4"><span class="flex items-center"> Ditolak </span></th>
                            </tr>
                        </thead>

                        <tbody>
                            <tr v-for="(label, index) in chartData.labels" :key="index">
                                <td class="font-bold text-gray-900 whitespace-nowrap py-3 px-4">
                                    {{ label }}
                                </td>
                                <td class="py-3 px-4">{{ chartData.diajukan[index] }}</td>
                                <td class="py-3 px-4">{{ chartData.diverifikasi[index] }}</td>
                                <td class="py-3 px-4">{{ chartData.ditolak[index] }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
</template>