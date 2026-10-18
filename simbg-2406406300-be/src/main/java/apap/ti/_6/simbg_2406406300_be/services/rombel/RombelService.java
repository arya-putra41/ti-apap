package apap.ti._6.simbg_2406406300_be.services.rombel;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.CreateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.UpdateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.KuotaTooLowException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelIllegalChangeStatusException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.SekolahInactiveException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;

public interface RombelService {
    public List<RombonganBelajar> findAll();

    public RombonganBelajar findRombelById(String id) throws RombelNotFoundException;

    public RombonganBelajar createRombel(CreateRombelRequest request) throws SekolahNotFoundException, SekolahInactiveException;

    public RombonganBelajar updateRombel(UpdateRombelRequest request) throws RombelNotFoundException, KuotaTooLowException, RombelIllegalChangeStatusException, SekolahNotFoundException;

    public void addPenerima(String rombelId) throws RombelNotFoundException;

    public void removePenerima(String rombelId) throws RombelNotFoundException;

    public RombelResponse rombelToResponse(RombonganBelajar rombel) throws SekolahNotFoundException;

    public RombelBasicResponse rombelToBasicResponse(RombonganBelajar rombel) throws SekolahNotFoundException;
}
