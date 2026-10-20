package apap.ti._6.simbg_2406406300_be.dto.responses.penerima;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PenerimaStatistics {
    private String period;
    private int year;
    private List<String> labels;
    private List<Integer> diajukan;
    private List<Integer> diverifikasi;
    private List<Integer> ditolak;
}
