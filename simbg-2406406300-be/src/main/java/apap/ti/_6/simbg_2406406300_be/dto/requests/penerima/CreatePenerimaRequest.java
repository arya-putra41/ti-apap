package apap.ti._6.simbg_2406406300_be.dto.requests.penerima;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePenerimaRequest {
    @NotEmpty(message = "ID rombel tidak boleh kosong")
    private String rombelId;

    @NotEmpty(message = "Nama siswa tidak boleh kosong")
    private String nama;

    @NotEmpty(message = "NISN siswa tidak boleh kosong")
    @Size(min = 10, max = 10, message = "NISN harus sepanjang 10 karakter")
    private String nisn;

    @NotEmpty(message = "Jenis kelamin siswa tidak boleh kosong")
    private String jenisKelamin;

    @NotEmpty(message = "Tanggal lahir siswa tidak boleh kosong")
    @PastOrPresent(message = "Tanggal lahir harus sudah terjadi")
    private LocalDate tanggalLahir;

    @NotEmpty(message = "Kondisi khusus siswa tidak boleh kosong")
    private String kondisiKhusus;

    private String keterangan;
}
