package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.RombelRepository;

@Service
public class RombelQueryServiceImpl implements RombelQueryService {

    private RombelRepository rombelRepository;

    public RombelQueryServiceImpl(RombelRepository rombelRepository) {
        this.rombelRepository = rombelRepository;
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
    public RombelResponse rombelToResponse(RombonganBelajar rombel) {
        Sekolah sekolahPemilikRombel = rombel.getSekolah();

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
            .penerima(rombel.getPenerima())

            .namaSekolah(sekolahPemilikRombel.getNamaSekolah())

            .build();

        return response;
    }
}
