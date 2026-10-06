package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
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
}
