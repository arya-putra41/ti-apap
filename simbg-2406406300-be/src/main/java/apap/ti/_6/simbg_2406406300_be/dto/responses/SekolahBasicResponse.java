package apap.ti._6.simbg_2406406300_be.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SekolahBasicResponse {
    private String id;
    private String npsn;
    private String namaSekolah;
    private String jenjang;
    private String statusMitra;
}
