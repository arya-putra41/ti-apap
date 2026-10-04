package apap.ti._6.simbg_2406406300_be.dto.requests.sekolah;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSekolahRequest {
    @NotEmpty
    private String id;

    @NotEmpty(message = "NPSN tidak boleh kosong")
    private String npsn;

    @NotEmpty(message = "Nama sekolah tidak boleh kosong")
    private String namaSekolah;

    @NotEmpty(message = "Jenjang tidak boleh kosong")
    private String jenjang;

    @NotEmpty(message = "Provinsi tidak boleh kosong")
    private String provinsi;

    @NotEmpty(message = "Kota/Kabupaten tidak boleh kosong")
    private String kota;

    @NotEmpty(message = "Kecamatan tidak boleh kosong")
    private String kecamatan;

    @NotEmpty(message = "Desa/Kelurahan tidak boleh kosong")
    private String desa;

    @NotEmpty(message = "Alamat jalan tidak boleh kosong")
    private String jalan;

    @NotEmpty(message = "Nama kepala sekolah tidak boleh kosong")
    private String namaKepalaSekolah;

    @NotEmpty(message = "Nomor telepon sekolah tidak boleh kosong")
    private String teleponSekolah;

    @NotNull(message = "Tanggal bergabung menjadi mitra tidak boleh kosong")
    @PastOrPresent(message = "Tanggal bergabung menjadi mitra harus sudah terjadi")
    private LocalDate tanggalBergabung;
}
