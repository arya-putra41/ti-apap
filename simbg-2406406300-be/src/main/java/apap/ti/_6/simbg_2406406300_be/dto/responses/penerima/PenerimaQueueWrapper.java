package apap.ti._6.simbg_2406406300_be.dto.responses.penerima;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenerimaQueueWrapper {
    private List<PenerimaQueueResponse> queue;
    private int maxDays;
}
