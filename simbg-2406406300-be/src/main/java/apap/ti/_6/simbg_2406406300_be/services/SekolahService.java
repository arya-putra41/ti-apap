package apap.ti._6.simbg_2406406300_be.services;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.CreateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.UpdateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.NpsnExistsException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.NpsnInexactLengthException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahIllegalChangeException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;

public interface SekolahService {
    public List<Sekolah> findAll();

    public Sekolah findSekolahById(String id) throws SekolahNotFoundException;

    public Sekolah createSekolah(CreateSekolahRequest request) throws NpsnExistsException, NpsnInexactLengthException;

    public Sekolah updateSekolah(UpdateSekolahRequest request) throws SekolahNotFoundException, SekolahIllegalChangeException;

    public Sekolah deactivateSekolah(String id);
}
