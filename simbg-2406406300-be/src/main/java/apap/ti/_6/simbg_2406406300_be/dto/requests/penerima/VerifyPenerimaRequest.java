package apap.ti._6.simbg_2406406300_be.dto.requests.penerima;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPenerimaRequest {
    @NotEmpty(message = "ID penerima tidak boleh kosong")
    private String id;

    @NotEmpty(message = "ID petugas verifikasi tidak boleh kosong")
    private String petugasVerifikasiId;

    @NotEmpty(message = "Catatan verifikasi tidak boleh kosong")
    private String catatan;
}
