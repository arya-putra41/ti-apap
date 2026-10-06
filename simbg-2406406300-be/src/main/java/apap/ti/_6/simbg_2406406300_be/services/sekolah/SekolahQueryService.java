package apap.ti._6.simbg_2406406300_be.services.sekolah;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;

public interface SekolahQueryService {
    public List<Sekolah> findAll();

    public Sekolah findSekolahById(String id) throws SekolahNotFoundException;
}
