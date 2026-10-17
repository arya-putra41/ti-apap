package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.riwayat.RiwayatResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.repo.PenerimaRepository;
import apap.ti._6.simbg_2406406300_be.services.riwayat.RiwayatService;

@Service
public class PenerimaQueryServiceImpl implements PenerimaQueryService {

    private PenerimaRepository penerimaRepository;
    private RiwayatService riwayatService;

    public PenerimaQueryServiceImpl(PenerimaRepository penerimaRepository, RiwayatService riwayatService) {
        this.penerimaRepository = penerimaRepository;
        this.riwayatService = riwayatService;
    }
    
    @Override
    public List<PenerimaManfaat> findAll() {
        return penerimaRepository.findAll();
    }

    @Override
    public PenerimaManfaat findPenerimaById(String id) throws PenerimaNotFoundException {
        return penerimaRepository.findById(id)
                .orElseThrow(() -> new PenerimaNotFoundException(id));
    }

    @Override
    public List<PenerimaManfaat> findPenerimaByRombelId(String rombelId) {
        return penerimaRepository.findByRombelId(rombelId);
    }

    @Override
    public int countAllActivePenerima() {
        return penerimaRepository.countByStatus("AKTIF");
    }

    @Override
    public int countAllPenerimaAwaitingVerification() {
        return penerimaRepository.countByStatus("DIAJUKAN");
    }

    @Override
    public PenerimaResponse penerimaToResponse(PenerimaManfaat penerima) {
        List<RiwayatResponse> listRiwayat = riwayatService.findRiwayatByPenerimaId(penerima.getId()).stream()
            .map(riwayat -> riwayat.toResponse())
            .toList();

        PenerimaResponse response = PenerimaResponse.builder()
            .id(penerima.getId())
            .nama(penerima.getNama())
            .nisn(penerima.getNisn())
            .namaRombel(penerima.getRombel().getNamaRombel())
            .namaSekolah(penerima.getRombel().getSekolah().getNamaSekolah())
            .jenisKelamin(penerima.getJenisKelamin())
            .kondisiKhusus(penerima.getKondisiKhusus())
            .keterangan(penerima.getKeterangan())
            .status(penerima.getStatus())
            .tanggalPengajuan(penerima.getTanggalPengajuan())
            .tanggalVerifikasi(penerima.getTanggalVerifikasi())
            .petugasVerifikasi(penerima.getPetugasVerifikasi())

            .riwayat(listRiwayat)
            .build();

        return response;
    }
}
