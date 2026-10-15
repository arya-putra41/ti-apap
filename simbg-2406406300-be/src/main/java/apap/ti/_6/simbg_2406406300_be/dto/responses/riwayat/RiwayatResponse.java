package apap.ti._6.simbg_2406406300_be.dto.responses.riwayat;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiwayatResponse {
    private String id;
    private String statusSebelumnya;
    private String statusBaru;
    private ZonedDateTime tanggalPerubahan;
    private String keterangan;
}
