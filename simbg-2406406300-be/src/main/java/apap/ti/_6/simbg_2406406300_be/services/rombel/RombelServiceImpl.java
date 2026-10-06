package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.CreateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.UpdateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.KuotaTooLowException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelIllegalChangeStatusException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.SekolahInactiveException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.RombelRepository;
import apap.ti._6.simbg_2406406300_be.services.sekolah.SekolahQueryService;
import apap.ti._6.simbg_2406406300_be.utils.IdGeneratorService;

@Service
public class RombelServiceImpl implements RombelService {

    private RombelRepository rombelRepository;
    private SekolahQueryService sekolahQueryService;
    private IdGeneratorService idGeneratorService;

    public RombelServiceImpl(RombelRepository rombelRepository, SekolahQueryService sekolahQueryService, IdGeneratorService idGeneratorService) {
        this.rombelRepository = rombelRepository;
        this.sekolahQueryService = sekolahQueryService;
        this.idGeneratorService = idGeneratorService;
    }

    @Override
    public List<RombonganBelajar> findAll() {
        return rombelRepository.findAll();
    }

    @Override
    public RombonganBelajar findRombelById(String id) throws RombelNotFoundException {
        return rombelRepository.findById(id)
                .orElseThrow(() -> new RombelNotFoundException(id));
    }

    @Override
    public RombonganBelajar createRombel(CreateRombelRequest request)
            throws SekolahNotFoundException, SekolahInactiveException {
        // check sekolah ada (exception di throw oleh service sekolah)
        Sekolah sekolah = sekolahQueryService.findSekolahById(request.getSekolahId());
        
        boolean sekolahAktif = sekolah.getStatusMitra().equals("AKTIF");
        if (sekolahAktif == false) {
            throw new SekolahInactiveException(request.getSekolahId());
        }

        RombonganBelajar newRombel = RombonganBelajar.builder()
            .id(idGeneratorService.generateRombelId())
            .sekolahId(request.getSekolahId())
            .namaRombel(request.getNamaRombel())
            .tingkat(request.getTingkat())
            .tahunAjaran(request.getTahunAjaran())
            .kuotaPenerima(request.getKuotaPenerima())
            .jumlahPenerimaTerdaftar(0)
            .penerima(new ArrayList<>())
            .status("AKTIF")
        .build();

        return rombelRepository.save(newRombel);
    }

    @Override
    public RombonganBelajar updateRombel(UpdateRombelRequest request) throws RombelNotFoundException, KuotaTooLowException, RombelIllegalChangeStatusException {
        // check rombel ada (exception di throw oleh method)
        RombonganBelajar rombel = findRombelById(request.getId());

        // business requirements check
        if (request.getKuotaPenerima() < rombel.getJumlahPenerimaTerdaftar()) {
            throw new KuotaTooLowException(request.getKuotaPenerima(), rombel.getJumlahPenerimaTerdaftar());
        } else if (request.getStatus() == "AKTIF" && rombel.getStatus() != "AKTIF") {
            throw new RombelIllegalChangeStatusException(request.getId());
        }

        // TODO: setelah penerima implemented, jika rombel ditutup nonaktifkan semua penerima di rombel (cascading)

        rombel.updateFromRequest(request);
        return rombelRepository.save(rombel);
    }
    
    @Override
    public RombelResponse rombelToResponse(RombonganBelajar rombel) throws SekolahNotFoundException {
        Sekolah sekolahPemilikRombel = sekolahQueryService.findSekolahById(rombel.getSekolahId());

        RombelResponse response = RombelResponse.builder()
            .id(rombel.getId())
            .sekolahId(rombel.getSekolahId())
            .namaRombel(rombel.getNamaRombel())
            .tingkat(rombel.getTingkat())
            .tahunAjaran(rombel.getTahunAjaran())
            .kuotaPenerima(rombel.getKuotaPenerima())
            .jumlahPenerimaTerdaftar(rombel.getJumlahPenerimaTerdaftar())
            .sisaKuota(rombel.getKuotaPenerima() - rombel.getJumlahPenerimaTerdaftar())
            .status(rombel.getStatus())
            .penerima(rombel.getPenerima())

            .namaSekolah(sekolahPemilikRombel.getNamaSekolah())

            .build();

        return response;
    }
}
