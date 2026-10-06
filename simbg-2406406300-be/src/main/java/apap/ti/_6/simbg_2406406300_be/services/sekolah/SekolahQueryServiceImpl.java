package apap.ti._6.simbg_2406406300_be.services.sekolah;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.SekolahRepository;

@Service
public class SekolahQueryServiceImpl implements SekolahQueryService {
        private SekolahRepository sekolahRepository;

    public SekolahQueryServiceImpl(SekolahRepository sekolahRepository) {
        this.sekolahRepository = sekolahRepository;
    }

    public List<Sekolah> findAll() {
        return sekolahRepository.findAll();
    }

    @Override
    public Sekolah findSekolahById(String id) throws SekolahNotFoundException {
        return sekolahRepository.findById(id)
                .orElseThrow(() -> new SekolahNotFoundException(id));
    }
}
