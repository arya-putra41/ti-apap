package apap.ti._6.simbg_2406406300_be.services.petugas;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.exceptions.petugas.PetugasNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;

public interface PetugasQueryService {
    List<PetugasVerifikasi> findAll();

    PetugasVerifikasi findPetugasById(String id) throws PetugasNotFoundException;
}
