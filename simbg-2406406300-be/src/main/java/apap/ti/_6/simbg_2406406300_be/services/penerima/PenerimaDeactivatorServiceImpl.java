package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.repo.PenerimaRepository;
import apap.ti._6.simbg_2406406300_be.services.riwayat.RiwayatService;
import jakarta.transaction.Transactional;

@Service
public class PenerimaDeactivatorServiceImpl implements PenerimaDeactivatorService {

    private PenerimaRepository penerimaRepository;
    private RiwayatService riwayatService;

    public PenerimaDeactivatorServiceImpl(PenerimaRepository penerimaRepository, RiwayatService riwayatService) {
        this.penerimaRepository = penerimaRepository;
        this.riwayatService = riwayatService;
    }

    @Override
    @Transactional
    public void deactivateAllPenerimaByRombelId(String rombelId) {
        List<PenerimaManfaat> listPenerima = penerimaRepository.findByRombelId(rombelId);

        if (!listPenerima.isEmpty()) {
            for (PenerimaManfaat penerima : listPenerima) {
                String prevStatus = penerima.getStatus();

                if (prevStatus == "DITOLAK" || prevStatus == "NONAKTIF") {
                    continue;
                }

                penerima.setStatus("NONAKTIF");

                riwayatService.createRiwayat(penerima, prevStatus, "NONAKTIF", "Dinonaktifkan otomatis karena rombel ditutup");
            }

            penerimaRepository.saveAll(listPenerima);
        }
    }
}
