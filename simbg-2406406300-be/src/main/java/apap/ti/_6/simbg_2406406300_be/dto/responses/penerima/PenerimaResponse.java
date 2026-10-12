package apap.ti._6.simbg_2406406300_be.dto.responses.penerima;

import java.time.ZonedDateTime;
import java.util.List;

import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import apap.ti._6.simbg_2406406300_be.models.RiwayatStatusPenerima;
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
    private String rombelId;
    private String namaRombel;
    private String namaSekolah;
    private String jenisKelamin;
    private String kondisiKhusus;
    private String keterangan;
    private String status;
    private ZonedDateTime tanggalPengajuan;
    private ZonedDateTime tanggalVerifikasi;
    private ZonedDateTime tanggalAktivasi;
    private PetugasVerifikasi petugasVerifikasi;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    private List<RiwayatStatusPenerima> riwayat;
}
