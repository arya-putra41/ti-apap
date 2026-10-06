package apap.ti._6.simbg_2406406300_be.dto.requests.rombel;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRombelRequest {
    @NotEmpty(message = "ID sekolah tidak boleh kosong")
    private String sekolahId;

    @NotEmpty(message = "Nama rombel tidak boleh kosong")
    private String namaRombel;

    @Min(value = 1, message = "Tingkat harus di antara 1 dan 12")
    @Max(value = 12, message = "Tingkat harus di antara 1 dan 12")
    @NotNull(message = "Tingkat tidak boleh kosong")
    private int tingkat;

    @NotEmpty(message = "Tahun ajaran tidak boleh kosong")
    private String tahunAjaran;

    @Positive(message = "Kuota penerima tidak boleh kurang dari 1")
    @NotNull(message = "Kuota penerima tidak boleh kosong")
    private int kuotaPenerima;
}
