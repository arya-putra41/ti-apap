package apap.ti._6.simbg_2406406300_be.services.petugas;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.exceptions.petugas.PetugasNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;

public interface PetugasQueryService {
    public List<PetugasVerifikasi> findAll();

    public PetugasVerifikasi findById(String id) throws PetugasNotFoundException;
}
