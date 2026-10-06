package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;

public interface RombelQueryService {
    public List<RombonganBelajar> findAll();

    public RombonganBelajar findRombelById(String id) throws RombelNotFoundException;

    public boolean rombelAktifExistsBySekolahId(String sekolahId);
}
