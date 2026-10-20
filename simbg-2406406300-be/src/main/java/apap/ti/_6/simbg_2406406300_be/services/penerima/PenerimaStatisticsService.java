package apap.ti._6.simbg_2406406300_be.services.penerima;

import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaStatistics;

public interface PenerimaStatisticsService {
    public PenerimaStatistics getStatistics(String period, Integer year);
}
