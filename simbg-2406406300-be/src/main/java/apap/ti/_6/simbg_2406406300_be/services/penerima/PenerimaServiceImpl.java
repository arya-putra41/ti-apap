package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.CreatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.DeactivatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.RejectPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.ReviewPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.UpdatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.VerifyPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaResponse;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.KeteranganIllegalForKondisiException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.NisnExistsException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaIllegalChangeNisnException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaNoLongerChangeableException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaWrongStatusException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.RombelInactiveException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.RombelQuotaEmptyException;
import apap.ti._6.simbg_2406406300_be.exceptions.petugas.PetugasNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.rombel.RombelNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
import apap.ti._6.simbg_2406406300_be.repo.PenerimaRepository;
import apap.ti._6.simbg_2406406300_be.services.petugas.PetugasQueryService;
import apap.ti._6.simbg_2406406300_be.services.riwayat.RiwayatService;
import apap.ti._6.simbg_2406406300_be.services.rombel.RombelService;
import apap.ti._6.simbg_2406406300_be.utils.IdGeneratorService;
import jakarta.transaction.Transactional;


// TODO: Implement RiwayatStatusPenerima, and write to DB if change occurs

@Service
public class PenerimaServiceImpl implements PenerimaService {

    private PenerimaRepository penerimaRepository;
    private RombelService rombelService;
    private PetugasQueryService petugasQueryService;
    private RiwayatService riwayatService;
    private IdGeneratorService idGeneratorService;

    public PenerimaServiceImpl(PenerimaRepository penerimaRepository, RombelService rombelService, PetugasQueryService petugasQueryService, RiwayatService riwayatService, IdGeneratorService idGeneratorService) {
        this.penerimaRepository = penerimaRepository;
        this.rombelService = rombelService;
        this.petugasQueryService = petugasQueryService;
        this.riwayatService = riwayatService;
        this.idGeneratorService = idGeneratorService;
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

    @Override
    @Transactional
    public PenerimaManfaat createPenerima(CreatePenerimaRequest request) throws NisnExistsException,
            KeteranganIllegalForKondisiException, RombelNotFoundException, RombelQuotaEmptyException, RombelInactiveException {
        // check if NISN is already in database
        List<String> activeStatuses = Arrays.asList("DIAJUKAN", "TERVERIFIKASI", "AKTIF");
        boolean nisnExists = penerimaRepository.existsByNisnAndStatusInIgnoreCase(request.getNisn(), activeStatuses);
        if (nisnExists) {
            throw new NisnExistsException(request.getNisn());
        }

        // check if keterangan exists for condition
        if (request.getKeterangan() == null || request.getKeterangan().isBlank()) {
            if (!request.getKondisiKhusus().equalsIgnoreCase("TIDAK_ADA")) {
                throw new KeteranganIllegalForKondisiException();
            }
        }

        // check if rombel exists and can accept students
        RombonganBelajar ownerRombel = rombelService.findRombelById(request.getRombelId());
        if ("DITUTUP".equalsIgnoreCase(ownerRombel.getStatus())) {
            throw new RombelInactiveException(request.getRombelId());
        } else if (ownerRombel.getKuotaPenerima() <= 0 || ownerRombel.getKuotaPenerima() - ownerRombel.getJumlahPenerimaTerdaftar() <= 0) {
            throw new RombelQuotaEmptyException(request.getRombelId());
        } 

        // create the object
        PenerimaManfaat created = PenerimaManfaat.builder()
            .id(idGeneratorService.generatePenerimaId())

            .nama(request.getNama())
            .nisn(request.getNisn())
            .jenisKelamin(request.getJenisKelamin())
            .tanggalLahir(request.getTanggalLahir())
            .kondisiKhusus(request.getKondisiKhusus())
            .keterangan(request.getKeterangan())

            .rombel(ownerRombel)
            .status("DIAJUKAN")
            .tanggalPengajuan(ZonedDateTime.now(ZoneId.of("UTC+07:00")))
            .build();


        rombelService.addPenerima(request.getRombelId());
        
        PenerimaManfaat saved = penerimaRepository.save(created);
        riwayatService.createRiwayat(saved, null, "DIAJUKAN", "Pengajuan dibuat");

        return saved;
    }

    @Override
    public PenerimaManfaat updatePenerima(UpdatePenerimaRequest request)
            throws PenerimaNotFoundException, PenerimaIllegalChangeNisnException, KeteranganIllegalForKondisiException,
            PenerimaNoLongerChangeableException {
        // find penerima, service throws exception if not found
        PenerimaManfaat updated = findPenerimaById(request.getId());
        String beforeStatus = updated.getStatus();

        // check status, if DITOLAK or NONAKTIF then throw exception (no longer changeable)
        List<String> invalidStatuses = Arrays.asList("DITOLAK", "NONAKTIF");
        for (String invalidStatus : invalidStatuses) {
            if (beforeStatus.equalsIgnoreCase(invalidStatus)) {
                throw new PenerimaNoLongerChangeableException(invalidStatus);
            }
        }

        // check keterangan, if empty when kondisi khusus is not empty then throw exception (illegal keterangan)
        if (request.getKeterangan() == null || request.getKeterangan().isBlank()) {
            if (!request.getKondisiKhusus().equalsIgnoreCase("TIDAK_ADA")) {
                throw new KeteranganIllegalForKondisiException();
            }
        }

        // update the object
        updated.updateFromRequest(request);

        // check status again: if from "AKTIF" then review
        if (beforeStatus.equalsIgnoreCase("AKTIF")) {
            updated.review();
        }
        
        PenerimaManfaat saved = penerimaRepository.save(updated);
        if (beforeStatus.equalsIgnoreCase("AKTIF")) {
            riwayatService.createRiwayat(saved, "AKTIF", "DIAJUKAN", "Data diubah saat aktif, perlu verifikasi ulang");
        } 

        return saved;
    }

    @Override
    public PenerimaManfaat verifyPenerima(VerifyPenerimaRequest request)
            throws PenerimaNotFoundException, PetugasNotFoundException, PenerimaWrongStatusException {
        // check penerima and petugas exist, throw exception if absent
        PenerimaManfaat penerima = findPenerimaById(request.getId());
        PetugasVerifikasi petugas = petugasQueryService.findPetugasById(request.getPetugasVerifikasiId());

        // check status of penerima, throw exception if incorrect
        if (!penerima.getStatus().equalsIgnoreCase("DIAJUKAN")) {
            throw new PenerimaWrongStatusException("Verifikasi", "DIAJUKAN");
        }

        penerima.verify(petugas);
        PenerimaManfaat saved = penerimaRepository.save(penerima);
        riwayatService.createRiwayat(saved, "DIAJUKAN", "TERVERIFIKASI", request.getCatatan());
        return saved;
    }

    @Override
    public PenerimaManfaat activatePenerima(String id) throws PenerimaNotFoundException, PenerimaWrongStatusException {
        PenerimaManfaat penerima = findPenerimaById(id);

        // check status of penerima, throw exception if incorrect
        if (!penerima.getStatus().equalsIgnoreCase("TERVERIFIKASI")) {
            throw new PenerimaWrongStatusException("Aktivasi", "TERVERIFIKASI");
        }

        penerima.activate();
        PenerimaManfaat saved = penerimaRepository.save(penerima);
        riwayatService.createRiwayat(saved, "TERVERIFIKASI", "AKTIF", "Penerima Diaktivasi");
        return saved;
    }

    @Override
    public PenerimaManfaat rejectPenerima(RejectPenerimaRequest request)
            throws PenerimaNotFoundException, PenerimaWrongStatusException {
        PenerimaManfaat penerima = findPenerimaById(request.getId());
        String beforeStatus = penerima.getStatus();

        // check status of penerima, throw exception if incorrect
        if (!beforeStatus.equalsIgnoreCase("DIAJUKAN") && !beforeStatus.equalsIgnoreCase("TERVERIFIKASI")) {
            throw new PenerimaWrongStatusException("Penolakan", "DIAJUKAN atau TERVERIFIKASI");
        }

        rombelService.removePenerima(penerima.getRombel().getId());
        penerima.reject();

        PenerimaManfaat saved = penerimaRepository.save(penerima);
        riwayatService.createRiwayat(saved, beforeStatus, "DITOLAK", request.getAlasan());
        return saved;
    }

    @Override
    public PenerimaManfaat deactivatePenerima(DeactivatePenerimaRequest request)
            throws PenerimaNotFoundException, PenerimaWrongStatusException {
        PenerimaManfaat penerima = findPenerimaById(request.getId());

        // check status of penerima, throw exception if incorrect
        if (!penerima.getStatus().equalsIgnoreCase("AKTIF")) {
            throw new PenerimaWrongStatusException("Penonaktifan", "AKTIF");
        }

        rombelService.removePenerima(penerima.getRombel().getId());
        penerima.deactivate();

        PenerimaManfaat saved = penerimaRepository.save(penerima);
        riwayatService.createRiwayat(saved, "AKTIF", "NONAKTIF", request.getAlasan());
        return saved;
    }

    @Override
    public PenerimaManfaat reviewPenerima(ReviewPenerimaRequest request)
            throws PenerimaNotFoundException, PenerimaWrongStatusException {
        PenerimaManfaat penerima = findPenerimaById(request.getId());

        // check status of penerima, throw exception if incorrect
        if (!penerima.getStatus().equalsIgnoreCase("TERVERIFIKASI")) {
            throw new PenerimaWrongStatusException("Tinjau ulang", "TERVERIFIKASI");
        }

        rombelService.removePenerima(penerima.getRombel().getId());
        penerima.review();

        PenerimaManfaat saved = penerimaRepository.save(penerima);
        riwayatService.createRiwayat(saved, "TERVERIFIKASI", "DIAJUKAN", request.getAlasan());
        return saved;
    }
    
    @Override
    public PenerimaResponse penerimaToResponse(PenerimaManfaat penerima) {
        PenerimaResponse response = PenerimaResponse.builder()
            .id(penerima.getId())
            .nama(penerima.getNama())
            .nisn(penerima.getNisn())
            .namaRombel(penerima.getRombel().getNamaRombel())
            .namaSekolah(penerima.getRombel().getSekolah().getNamaSekolah())
            .jenisKelamin(penerima.getJenisKelamin())
            .kondisiKhusus(penerima.getKondisiKhusus())
            .keterangan(penerima.getKeterangan())
            .status(penerima.getStatus())
            .tanggalPengajuan(penerima.getTanggalPengajuan())
            .tanggalVerifikasi(penerima.getTanggalVerifikasi())
            .petugasVerifikasi(penerima.getPetugasVerifikasi())

            .riwayat(riwayatService.findRiwayatByPenerimaId(penerima.getId()))
            .build();

        return response;
    }
}
