package apap.ti._6.simbg_2406406300_be.dto.requests.rombel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRombelRequest {
    @NotEmpty(message = "ID rombel tidak boleh kosong")
    private String id;

    @PositiveOrZero(message = "Kuota penerima tidak boleh kurang dari 1")
    @NotNull(message = "Kuota penerima tidak boleh kosong")
    private int kuotaPenerima;

    @NotNull(message = "Status tidak boleh kosong")
    private String status;
}
