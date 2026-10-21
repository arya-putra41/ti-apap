package apap.ti._6.simbg_2406406300_be.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaAutoOperationsService;
import jakarta.transaction.Transactional;

@Component
public class AutoRejecter {
    private PenerimaAutoOperationsService PenerimaAutoOperationsService;

    public AutoRejecter(PenerimaAutoOperationsService PenerimaAutoOperationsService) {
        this.PenerimaAutoOperationsService = PenerimaAutoOperationsService;
    }

    // dijalankan setiap 60.000 ms (1 menit) untuk membantu pengujian
    @Transactional
    @Scheduled(fixedRate = 60000)
    public void autoRejectIdleApplications() {
        PenerimaAutoOperationsService.autoRejectIdleApplications();
    }
}
