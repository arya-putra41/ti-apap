package apap.ti._6.simbg_2406406300_be.services.home;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.responses.home.HomeResponse;
import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaQueryService;
import apap.ti._6.simbg_2406406300_be.services.rombel.RombelQueryService;
import apap.ti._6.simbg_2406406300_be.services.sekolah.SekolahQueryService;

@Service
public class HomeServiceImpl implements HomeService {

    private SekolahQueryService sekolahQueryService;
    private RombelQueryService rombelQueryService;
    private PenerimaQueryService penerimaQueryService;

    public HomeServiceImpl(SekolahQueryService sekolahQueryService, RombelQueryService rombelQueryService, PenerimaQueryService penerimaQueryService) {
        this.sekolahQueryService = sekolahQueryService;
        this.rombelQueryService = rombelQueryService;
        this.penerimaQueryService = penerimaQueryService;
    }

    private int countAllActiveSekolah() {
        return sekolahQueryService.countAllActiveSekolah();
    }

    private int countAllActiveRombel() {
        return rombelQueryService.countAllActiveRombel();
    }

    private int countAllActivePenerima() {
        return penerimaQueryService.countAllActivePenerima();
    }

    private String countQuotaPercent() {
        return rombelQueryService.getKuotaRombelTerpakai();
    }

    private int countAwaitingVerification() {
        return penerimaQueryService.countAllPenerimaAwaitingVerification();
    }
    
    @Override
    public HomeResponse buildResponse() {
        HomeResponse response = HomeResponse.builder()
            .jumlahSekolahAktif(countAllActiveSekolah())
            .jumlahRombelAktif(countAllActiveRombel())
            .jumlahPenerimaAktif(countAllActivePenerima())
            .persentaseKuotaTerpakai(Double.valueOf(countQuotaPercent()))
            .jumlahMenungguVerifikasi(countAwaitingVerification())
            .build();

        return response;
    }
}
