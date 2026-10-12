package apap.ti._6.simbg_2406406300_be.services.riwayat;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.exceptions.riwayat.RiwayatNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.models.RiwayatStatusPenerima;
import apap.ti._6.simbg_2406406300_be.repo.RiwayatRepository;
import apap.ti._6.simbg_2406406300_be.utils.IdGeneratorService;

@Service
public class RiwayatServiceImpl implements RiwayatService {

    private RiwayatRepository riwayatRepository;
    private IdGeneratorService idGeneratorService;

    public RiwayatServiceImpl(RiwayatRepository riwayatRepository, IdGeneratorService idGeneratorService) {
        this.riwayatRepository = riwayatRepository;
        this.idGeneratorService = idGeneratorService;
    }

    @Override
    public List<RiwayatStatusPenerima> findAll() {
        return riwayatRepository.findAll();
    }

    @Override
    public RiwayatStatusPenerima findRiwayatById(String id) throws RiwayatNotFoundException {
        return riwayatRepository.findById(id)
                .orElseThrow(() -> new RiwayatNotFoundException(id));
    }

    @Override
    public List<RiwayatStatusPenerima> findRiwayatByPenerimaId(String penerimaId) {
        return riwayatRepository.findByPenerimaId(penerimaId);
    }

    @Override
    public ZonedDateTime findTanggalAktivasiOfPenerima(String penerimaId) {
        RiwayatStatusPenerima activation = riwayatRepository
                .findFirstByPenerimaIdAndStatusBaruOrderByTanggalPerubahanDesc("AKTIF", penerimaId);
        if (activation == null) {
            return null;
        } else {
            return activation.getTanggalPerubahan();
        }
    }

    @Override
    public RiwayatStatusPenerima createRiwayat(PenerimaManfaat penerima, String statusSebelumnya, String statusBaru,
            String keterangan) {
        RiwayatStatusPenerima newRiwayat = RiwayatStatusPenerima.builder()
                .id(idGeneratorService.generateRiwayatId())
                .penerima(penerima)
                .statusSebelumnya(statusSebelumnya)
                .statusBaru(statusBaru)
                .keterangan(keterangan)
                .tanggalPerubahan(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
                .build();

        return riwayatRepository.save(newRiwayat);
    }

}
