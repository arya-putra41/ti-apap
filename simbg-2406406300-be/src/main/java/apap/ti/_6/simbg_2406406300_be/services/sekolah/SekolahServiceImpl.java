package apap.ti._6.simbg_2406406300_be.services.sekolah;

import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.CreateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.UpdateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.sekolah.SekolahResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.NpsnExistsException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.RombelStillActiveException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahIllegalChangeException;
import apap.ti._6.simbg_2406406300_be.exceptions.sekolah.SekolahNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.SekolahRepository;
import apap.ti._6.simbg_2406406300_be.services.rombel.RombelQueryService;
import apap.ti._6.simbg_2406406300_be.utils.IdGeneratorService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class SekolahServiceImpl implements SekolahService {
    
    private SekolahRepository sekolahRepository;
    private RombelQueryService rombelQueryService;
    private IdGeneratorService idGenerator;

    public SekolahServiceImpl(SekolahRepository sekolahRepository, RombelQueryService rombelQueryService, IdGeneratorService idGenerator) {
        this.sekolahRepository = sekolahRepository;
        this.rombelQueryService = rombelQueryService;
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
    public Sekolah createSekolah(CreateSekolahRequest request) throws NpsnExistsException {
        // Check NPSN: Apakah ada?
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
    public Sekolah addRombelToSekolah(String id, RombonganBelajar rombel) throws SekolahNotFoundException {
        Sekolah findSekolah = findSekolahById(id);

        return sekolahRepository.save(findSekolah);
    }

    @Override
    public Sekolah deactivateSekolah(String id) throws SekolahNotFoundException, RombelStillActiveException {
        Sekolah deactivated = findSekolahById(id);

        // Cek bahwa semua rombel di sekolah tersebut sudah ditutup
        boolean adaRombelStillActive = rombelQueryService.rombelAktifExistsBySekolahId(id);
        if (adaRombelStillActive == true) {
            throw new RombelStillActiveException();
        }

        deactivated.deactivate();
        return sekolahRepository.save(deactivated);
    }

    @Override
    public SekolahResponse sekolahToResponse(Sekolah sekolah) {
        List<RombelResponse> listOfRombel = rombelQueryService.findBySekolahId(sekolah.getId()).stream()
            .map(rbl -> rombelQueryService.rombelToResponse(rbl))
            .toList();

        SekolahResponse response = SekolahResponse.builder()
            .id(sekolah.getId())
            .npsn(sekolah.getNpsn())
            .namaSekolah(sekolah.getNamaSekolah())
            .jenjang(sekolah.getJenjang())
            .provinsi(sekolah.getProvinsi())
            .kota(sekolah.getKota())
            .kecamatan(sekolah.getKecamatan())
            .desa(sekolah.getDesa())
            .jalan(sekolah.getJalan())
            .namaKepalaSekolah(sekolah.getNamaKepalaSekolah())
            .teleponSekolah(sekolah.getTeleponSekolah())
            .tanggalBergabung(sekolah.getTanggalBergabung())
            .statusMitra(sekolah.getStatusMitra())
            .rombel(listOfRombel)
            .build();

        return response;
    }
}
