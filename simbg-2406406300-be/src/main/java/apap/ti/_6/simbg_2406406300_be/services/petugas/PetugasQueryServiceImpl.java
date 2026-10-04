package apap.ti._6.simbg_2406406300_be.services.petugas;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.exceptions.petugas.PetugasNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import apap.ti._6.simbg_2406406300_be.repo.PetugasRepository;

@Service
public class PetugasQueryServiceImpl {
    
    private PetugasRepository petugasRepository;

    public PetugasQueryServiceImpl(PetugasRepository petugasRepository) {
        this.petugasRepository = petugasRepository;
    }

    public List<PetugasVerifikasi> findAll() {
        return petugasRepository.findAll();
    }

    public PetugasVerifikasi findById(String id) throws PetugasNotFoundException {
        return petugasRepository.findById(id)
                .orElseThrow(() -> new PetugasNotFoundException(id));
    }
}
