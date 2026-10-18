package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.CreateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.UpdateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.KuotaTooLowException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelIllegalChangeStatusException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.SekolahInactiveException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.RombelRepository;
import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaDeactivatorService;
import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaQueryService;
import apap.ti._6.simbg_2406406300_be.services.sekolah.SekolahService;
import apap.ti._6.simbg_2406406300_be.utils.IdGeneratorService;
import jakarta.transaction.Transactional;

@Service
public class RombelServiceImpl implements RombelService {

    private RombelRepository rombelRepository;
    private SekolahService sekolahService;
    private PenerimaQueryService penerimaQueryService;
    private PenerimaDeactivatorService penerimaDeactivatorService;
    private IdGeneratorService idGeneratorService;

    public RombelServiceImpl(RombelRepository rombelRepository, SekolahService sekolahService, PenerimaQueryService penerimaQueryService, PenerimaDeactivatorService penerimaDeactivatorService, IdGeneratorService idGeneratorService) {
        this.rombelRepository = rombelRepository;
        this.sekolahService = sekolahService;
        this.penerimaQueryService = penerimaQueryService;
        this.penerimaDeactivatorService = penerimaDeactivatorService;
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
    @Transactional
    public RombonganBelajar createRombel(CreateRombelRequest request)
            throws SekolahNotFoundException, SekolahInactiveException {
        // check sekolah ada (exception di throw oleh service sekolah)
        Sekolah sekolah = sekolahService.findSekolahById(request.getSekolahId());
        
        boolean sekolahAktif = sekolah.getStatusMitra().equals("AKTIF");
        if (sekolahAktif == false) {
            throw new SekolahInactiveException(request.getSekolahId());
        }

        RombonganBelajar newRombel = RombonganBelajar.builder()
            .id(idGeneratorService.generateRombelId())
            .sekolah(sekolah)
            .namaRombel(request.getNamaRombel())
            .tingkat(request.getTingkat())
            .tahunAjaran(request.getTahunAjaran())
            .kuotaPenerima(request.getKuotaPenerima())
            .jumlahPenerimaTerdaftar(0)
            .status("AKTIF")
        .build();

        return rombelRepository.save(newRombel);
    }

    @Override
    @Transactional
    public RombonganBelajar updateRombel(UpdateRombelRequest request) throws RombelNotFoundException, KuotaTooLowException, RombelIllegalChangeStatusException, SekolahNotFoundException {
        // check rombel ada (exception di throw oleh method)
        RombonganBelajar rombel = findRombelById(request.getId());

        // business requirements check
        if (request.getKuotaPenerima() < rombel.getJumlahPenerimaTerdaftar()) {
            throw new KuotaTooLowException(request.getKuotaPenerima(), rombel.getJumlahPenerimaTerdaftar());
        } else if (request.getStatus().equalsIgnoreCase("AKTIF") && !rombel.getStatus().equalsIgnoreCase("AKTIF")) {
            throw new RombelIllegalChangeStatusException(request.getId());
        }

        boolean statusLamaAktif = "AKTIF".equalsIgnoreCase(rombel.getStatus());
        boolean statusBaruNonaktif = !"AKTIF".equalsIgnoreCase(request.getStatus());

        if (statusLamaAktif && statusBaruNonaktif) {
            penerimaDeactivatorService.deactivateAllPenerimaByRombelId(rombel.getId());

            rombel.setJumlahPenerimaTerdaftar(0);
        }

        rombel.updateFromRequest(request);
        return rombelRepository.save(rombel);
    }

    @Override
    public void addPenerima(String rombelId) throws RombelNotFoundException {
        RombonganBelajar rombel = findRombelById(rombelId);
        rombel.setJumlahPenerimaTerdaftar(rombel.getJumlahPenerimaTerdaftar() + 1);
        rombelRepository.save(rombel);
    }

    @Override
    public void removePenerima(String rombelId) throws RombelNotFoundException {
        RombonganBelajar rombel = findRombelById(rombelId);
        rombel.setJumlahPenerimaTerdaftar(rombel.getJumlahPenerimaTerdaftar() - 1);
        rombelRepository.save(rombel);
    }
    
    @Override
    public RombelResponse rombelToResponse(RombonganBelajar rombel) throws SekolahNotFoundException {
        Sekolah sekolahPemilikRombel = rombel.getSekolah();

        List<PenerimaBasicResponse> listPenerima = penerimaQueryService.findPenerimaByRombelId(rombel.getId()).stream()
            .map(penerima -> penerima.toBasicResponse())
            .toList();

        RombelResponse response = RombelResponse.builder()
            .id(rombel.getId())
            .sekolahId(sekolahPemilikRombel.getId())
            .namaRombel(rombel.getNamaRombel())
            .tingkat(rombel.getTingkat())
            .tahunAjaran(rombel.getTahunAjaran())
            .kuotaPenerima(rombel.getKuotaPenerima())
            .jumlahPenerimaTerdaftar(rombel.getJumlahPenerimaTerdaftar())
            .sisaKuota(rombel.getKuotaPenerima() - rombel.getJumlahPenerimaTerdaftar())
            .status(rombel.getStatus())
            .penerima(listPenerima)

            .namaSekolah(sekolahPemilikRombel.getNamaSekolah())

            .build();

        return response;
    }

    @Override
    public RombelBasicResponse rombelToBasicResponse(RombonganBelajar rombel) throws SekolahNotFoundException {
        Sekolah sekolahPemilikRombel = rombel.getSekolah();

        RombelBasicResponse response = RombelBasicResponse.builder()
            .id(rombel.getId())
            .sekolahId(sekolahPemilikRombel.getId())
            .namaRombel(rombel.getNamaRombel())
            .tingkat(rombel.getTingkat())
            .tahunAjaran(rombel.getTahunAjaran())
            .kuotaPenerima(rombel.getKuotaPenerima())
            .jumlahPenerimaTerdaftar(rombel.getJumlahPenerimaTerdaftar())
            .sisaKuota(rombel.getKuotaPenerima() - rombel.getJumlahPenerimaTerdaftar())
            .status(rombel.getStatus())

            .namaSekolah(sekolahPemilikRombel.getNamaSekolah())

            .build();

        return response;
    }
}
