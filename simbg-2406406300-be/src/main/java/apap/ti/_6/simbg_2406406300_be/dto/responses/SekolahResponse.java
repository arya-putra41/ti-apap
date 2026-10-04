package apap.ti._6.simbg_2406406300_be.dto.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SekolahResponse {
    
    private String id;
    private String npsn;
    private String namaSekolah;
    private String jenjang;
    private String provinsi;
    private String kota;
    private String kecamatan;
    private String desa;
    private String jalan;
    private String namaKepalaSekolah;
    private String teleponSekolah;
    private LocalDate tanggalBergabung;
    private String statusMitra;
}
