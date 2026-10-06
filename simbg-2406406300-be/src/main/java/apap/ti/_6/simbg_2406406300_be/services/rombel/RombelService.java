package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.CreateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.UpdateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.KuotaTooLowException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelIllegalChangeStatusException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.SekolahTidakAktifException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;

public interface RombelService {
    public List<RombonganBelajar> findAll();

    public RombonganBelajar findRombelById(String id) throws RombelNotFoundException;

    public RombonganBelajar createRombel(CreateRombelRequest request) throws SekolahNotFoundException, SekolahTidakAktifException;

    public RombonganBelajar updateRombel(UpdateRombelRequest request) throws RombelNotFoundException, KuotaTooLowException, RombelIllegalChangeStatusException;

    public RombelResponse rombelToResponse(RombonganBelajar rombel) throws SekolahNotFoundException;
}
