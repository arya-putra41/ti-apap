package apap.ti._6.simbg_2406406300_be.dto.responses.rombel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RombelResponse {
    
    private String id;
    private String sekolahId;
    private String namaSekolah;
    private String namaRombel;
    private int tingkat;
    private String tahunAjaran;
    private int kuotaPenerima;
    private int jumlahPenerimaTerdaftar;
    private int sisaKuota;
    private String status;
}
