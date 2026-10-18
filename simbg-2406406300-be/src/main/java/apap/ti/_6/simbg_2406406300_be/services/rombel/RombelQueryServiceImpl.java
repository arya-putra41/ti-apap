package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.RombelRepository;
import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaQueryService;

@Service
public class RombelQueryServiceImpl implements RombelQueryService {

    private RombelRepository rombelRepository;
    private PenerimaQueryService penerimaQueryService;

    public RombelQueryServiceImpl(RombelRepository rombelRepository, PenerimaQueryService penerimaQueryService) {
        this.rombelRepository = rombelRepository;
        this.penerimaQueryService = penerimaQueryService;
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
    public boolean rombelAktifExistsBySekolahId(String id) {
        return rombelRepository.existsBySekolahIdAndStatus(id, "AKTIF");
    }

    @Override
    public List<RombonganBelajar> findBySekolahId(String sekolahId) {
        return rombelRepository.findBySekolahId(sekolahId);
    }

    @Override
    public int countAllActiveRombel() {
        return rombelRepository.countByStatus("AKTIF");
    }

    @Override
    public String getKuotaRombelTerpakai() {
        double quota = rombelRepository.getKuotaTerpakai();
        String rounded = String.format("%.2f", quota);
        return rounded;
    }

    @Override
    public RombelResponse rombelToResponse(RombonganBelajar rombel) {
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
