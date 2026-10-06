package apap.ti._6.simbg_2406406300_be.dto.requests.penerima;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPenerimaRequest {
    @NotEmpty(message = "ID penerima tidak boleh kosong")
    private String id;

    @NotEmpty(message = "Alasan penonaktifan penerima tidak boleh kosong")
    private String alasan;
}
