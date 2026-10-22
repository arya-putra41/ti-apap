import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import { format } from "date-fns";
import { type PenerimaResponse } from "@/services/penerimaService";

const formatKondisiKhusus = (val: string | null | undefined): string => {
    if (!val) return '-'
    return val.toLowerCase().split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(" ")
}

export const exportToPdf = (penerima: PenerimaResponse) => {
    if (!penerima) return

    const riwayatList = penerima.riwayat

    // Inisialisasi dokumen PDF (A4, portrait, unit milimeter)
    const doc = new jsPDF({
        orientation: 'portrait',
        unit: 'mm',
        format: 'a4'
    })

    // 1. Judul Dokumen
    doc.setFontSize(16)
    doc.setFont('helvetica', 'bold')
    doc.text('DETAIL PENERIMA MANFAAT', 14, 20)

    // Subtitle / Tanggal Cetak
    doc.setFontSize(9)
    doc.setFont('helvetica', 'normal')
    doc.text(`Dicetak pada: ${format(new Date(), 'dd MMM yyyy, HH:mm')}`, 14, 26)

    // Garis Pemisah
    doc.setLineWidth(0.5)
    doc.line(14, 29, 196, 29)

    // 2. Data Informasi Penerima (Format Key - Value)
    doc.setFontSize(10)

    const details = [
        ['Nama Lengkap', `: ${penerima.nama}`],
        ['NISN', `: ${penerima.nisn}`],
        ['Status Penerima', `: ${penerima.status}`],
        ['Sekolah', `: ${penerima.namaSekolah}`],
        ['Rombel', `: ${penerima.namaRombel}`],
        ['Jenis Kelamin', `: ${penerima.jenisKelamin === 'L' ? 'Laki-laki' : 'Perempuan'}`],
        ['Tanggal Lahir', `: ${format(new Date(penerima.tanggalLahir), 'd MMM yyyy')}`],
        ['Kondisi Khusus', `: ${ formatKondisiKhusus(penerima.kondisiKhusus) }`],
        ['Tanggal Pengajuan', `: ${format(new Date(penerima.tanggalPengajuan), 'd MMM yyyy, HH:mm')}`],
        ['Tanggal Verifikasi', `: ${penerima.tanggalVerifikasi ? format(new Date(penerima.tanggalVerifikasi), 'd MMM yyyy, HH:mm') : "Belum Diverifikasi"}`],
        ['Tanggal Aktivasi', `: ${penerima.tanggalAktivasi ? format(new Date(penerima.tanggalAktivasi), 'd MMM yyyy, HH:mm') : "Belum Diaktivasi"}`],
    ]

    let startY = 36
    details.forEach(([key, value]) => {
        doc.setFont('helvetica', 'bold')
        doc.text(key ?? '', 14, startY) // Fallback ke '' untuk menjamin tipe string murni

        doc.setFont('helvetica', 'normal')
        doc.text(value ?? '', 60, startY)

        startY += 6
    })

    // 3. Tabel Riwayat Perubahan Status (Menggunakan jspdf-autotable)
    if (riwayatList && riwayatList.length > 0) {
        doc.setFontSize(12)
        doc.setFont('helvetica', 'bold')
        doc.text('Riwayat Status', 14, startY + 6)

        const tableBody = riwayatList.map(r => [
            format(new Date(r.tanggalPerubahan), 'dd MMM yyyy, HH:mm'),
            r.statusSebelumnya ? `${r.statusSebelumnya} -> ${r.statusBaru}` : r.statusBaru,
            r.keterangan || '-'
        ])

        autoTable(doc, {
            startY: startY + 10,
            head: [['Tanggal', 'Perpindahan Status', 'Keterangan']],
            body: tableBody,
            headStyles: {
                fillColor: [219, 39, 119], // Warna pink-600 Tailwind
                textColor: [255, 255, 255],
                fontStyle: 'bold'
            },
            styles: {
                fontSize: 9,
                cellPadding: 3
            },
            alternateRowStyles: {
                fillColor: [250, 250, 250]
            }
        })
    }

    // 4. Unduh File PDF
    doc.save(`Kartu-${penerima.id}.pdf`)
}