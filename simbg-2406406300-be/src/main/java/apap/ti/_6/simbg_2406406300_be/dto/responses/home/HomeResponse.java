package apap.ti._6.simbg_2406406300_be.dto.responses.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeResponse {
    private int jumlahSekolahAktif;
    private int jumlahRombelAktif;
    private int jumlahPenerimaAktif;
    private double persentaseKuotaTerpakai;
    private int jumlahMenungguVerifikasi;
}
