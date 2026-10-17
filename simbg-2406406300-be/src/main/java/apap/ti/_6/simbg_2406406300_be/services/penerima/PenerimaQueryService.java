package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;

public interface PenerimaQueryService {
    public List<PenerimaManfaat> findAll();

    public PenerimaManfaat findPenerimaById(String id) throws PenerimaNotFoundException;

    public List<PenerimaManfaat> findPenerimaByRombelId(String rombelId);

    public int countAllActivePenerima();

    public int countAllPenerimaAwaitingVerification();

    public PenerimaResponse penerimaToResponse(PenerimaManfaat penerima);
}
