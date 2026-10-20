package apap.ti._6.simbg_2406406300_be.dto.responses.penerima;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.responses.riwayat.RiwayatResponse;
import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PenerimaResponse {
    private String id;
    private String nama;
    private String nisn;
    private LocalDate tanggalLahir;
    private String namaRombel;
    private String namaSekolah;
    private String jenisKelamin;
    private String kondisiKhusus;
    private String keterangan;
    private String status;
    private ZonedDateTime tanggalPengajuan;
    private ZonedDateTime tanggalVerifikasi;
    private PetugasVerifikasi petugasVerifikasi;

    private List<RiwayatResponse> riwayat;
}
