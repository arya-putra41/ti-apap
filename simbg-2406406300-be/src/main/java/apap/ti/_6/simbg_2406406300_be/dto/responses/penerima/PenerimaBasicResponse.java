package apap.ti._6.simbg_2406406300_be.dto.responses.penerima;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenerimaBasicResponse {
    private String id;
    private String nama;
    private String nisn;
    private String rombelId;
    private String namaRombel;
    private String namaSekolah;
    private String jenisKelamin;
    private String kondisiKhusus;
    private String status;
    private ZonedDateTime tanggalPengajuan;
}
