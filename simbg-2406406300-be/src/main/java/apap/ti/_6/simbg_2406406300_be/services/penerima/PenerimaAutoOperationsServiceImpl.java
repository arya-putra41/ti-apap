package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.configs.PenerimaDaysConfig;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaWrongStatusException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.repo.PenerimaRepository;
import apap.ti._6.simbg_2406406300_be.services.riwayat.RiwayatService;
import apap.ti._6.simbg_2406406300_be.services.rombel.RombelService;
import jakarta.transaction.Transactional;

@Service
public class PenerimaAutoOperationsServiceImpl implements PenerimaAutoOperationsService {

    private PenerimaRepository penerimaRepository;
    private RiwayatService riwayatService;
    private RombelService rombelService;
    private PenerimaDaysConfig penerimaDaysConfig;

    public PenerimaAutoOperationsServiceImpl(PenerimaRepository penerimaRepository, RiwayatService riwayatService, RombelService rombelService, PenerimaDaysConfig penerimaDaysConfig) {
        this.penerimaRepository = penerimaRepository;
        this.riwayatService = riwayatService;
        this.rombelService = rombelService;
        this.penerimaDaysConfig = penerimaDaysConfig;
    } 

    @Override
    @Transactional
    public void autoRejectIdleApplications() {
        int maxDays = penerimaDaysConfig.getAutoRejectMaxDays();

        ZonedDateTime cutoffDate = ZonedDateTime.now(ZoneId.of("UTC+07:00")).minusDays(maxDays);

        List<PenerimaManfaat> idleApplications = penerimaRepository.findByStatusAndUpdatedAtBefore("DIAJUKAN", cutoffDate);

        if (idleApplications.isEmpty()) {
            return;
        } else {
            for (PenerimaManfaat penerima : idleApplications) {
                rejectPenerimaAutomatically(penerima, maxDays);
            }
        }
    }

    private PenerimaManfaat rejectPenerimaAutomatically(PenerimaManfaat penerima, int maxDays)
            throws PenerimaWrongStatusException {
        String beforeStatus = penerima.getStatus();
        if (!beforeStatus.equalsIgnoreCase("DIAJUKAN") && !beforeStatus.equalsIgnoreCase("TERVERIFIKASI")) {
            throw new PenerimaWrongStatusException("Penolakan", "DIAJUKAN atau TERVERIFIKASI");
        }

        rombelService.removePenerima(penerima.getRombel().getId());
        penerima.reject();

        PenerimaManfaat saved = penerimaRepository.save(penerima);
        riwayatService.createRiwayat(saved, beforeStatus, "DITOLAK", String.format("Ditolak otomatis, kedaluwarsa %d hari tanpa verifikasi", maxDays));
        return saved;
    }
}
