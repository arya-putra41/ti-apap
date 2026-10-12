package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.repo.PenerimaRepository;

@Service
public class PenerimaQueryServiceImpl implements PenerimaQueryService {

    private PenerimaRepository penerimaRepository;

    public PenerimaQueryServiceImpl(PenerimaRepository penerimaRepository) {
        this.penerimaRepository = penerimaRepository;
    }
    
    @Override
    public List<PenerimaManfaat> findAll() {
        return penerimaRepository.findAll();
    }

    @Override
    public PenerimaManfaat findPenerimaById(String id) throws PenerimaNotFoundException {
        return penerimaRepository.findById(id)
                .orElseThrow(() -> new PenerimaNotFoundException(id));
    }
}
