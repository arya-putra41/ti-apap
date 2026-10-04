package apap.ti._6.simbg_2406406300_be.services.sekolah;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.CreateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.UpdateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.NpsnExistsException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.NpsnInexactLengthException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahIllegalChangeException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.SekolahRepository;
import apap.ti._6.simbg_2406406300_be.utils.IdGeneratorService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SekolahServiceImpl implements SekolahService {
    
    private SekolahRepository sekolahRepository;
    private IdGeneratorService idGenerator;

    public SekolahServiceImpl(SekolahRepository sekolahRepository, IdGeneratorService idGenerator) {
        this.sekolahRepository = sekolahRepository;
        this.idGenerator = idGenerator;
    }

    public List<Sekolah> findAll() {
        return sekolahRepository.findAll();
    }

    @Override
    public Sekolah findSekolahById(String id) throws SekolahNotFoundException {
        return sekolahRepository.findById(id)
                .orElseThrow(() -> new SekolahNotFoundException(id));
    }

    @Override
    public Sekolah createSekolah(CreateSekolahRequest request) throws NpsnExistsException, NpsnInexactLengthException {
        // Check NPSN: Apakah ada? Apakah 8 character?
        if (request.getNpsn().length() != 8) {
            throw new NpsnInexactLengthException();
        }
        boolean existsByNpsn = sekolahRepository.existsByNpsn(request.getNpsn());
        if (existsByNpsn) {
            throw new NpsnExistsException(request.getNpsn());
        }

        Sekolah newSekolah = Sekolah.builder()
            .id(idGenerator.generateSekolahId())
            
            .npsn(request.getNpsn())
            .namaSekolah(request.getNamaSekolah())
            .jenjang(request.getJenjang())
            .provinsi(request.getProvinsi())
            .kota(request.getKota())
            .kecamatan(request.getKecamatan())
            .desa(request.getDesa())
            .jalan(request.getJalan())
            .namaKepalaSekolah(request.getNamaKepalaSekolah())
            .teleponSekolah(request.getTeleponSekolah())
            .tanggalBergabung(request.getTanggalBergabung())

            .statusMitra("AKTIF")
            .build();

        return sekolahRepository.save(newSekolah);
    }

    @Override
    public Sekolah updateSekolah(UpdateSekolahRequest request) throws SekolahNotFoundException, SekolahIllegalChangeException {
        Sekolah updated = findSekolahById(request.getId());

        // check NPSN (field yg dilarang diubah)
        if (!updated.getNpsn().equals(request.getNpsn())) {
            throw new SekolahIllegalChangeException("NPSN");
        }

        // Set fields
        updated.updateFromRequest(request);
        return sekolahRepository.save(updated);
    }

    @Override
    public Sekolah deactivateSekolah(String id) {
        Sekolah deactivated = findSekolahById(id);

        // TODO: Setelah Rombel diimplementasikan, cek bahwa semua Rombel telah ditutup

        deactivated.deactivate();
        return sekolahRepository.save(deactivated);
    }
}
