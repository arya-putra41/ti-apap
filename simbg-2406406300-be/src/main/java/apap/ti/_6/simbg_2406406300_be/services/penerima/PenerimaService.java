package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.CreatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.DeactivatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.RejectPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.ReviewPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.UpdatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.VerifyPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.KeteranganIllegalForKondisiException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.NisnExistsException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaIllegalChangeNisnException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaNoLongerChangeableException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaNotFoundException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.PenerimaWrongStatusException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.RombelInactiveException;
import apap.ti._6.simbg_2406406300_be.exceptions.penerima.RombelQuotaEmptyException;
import apap.ti._6.simbg_2406406300_be.exceptions.petugas.PetugasNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;

public interface PenerimaService {
    public List<PenerimaManfaat> findAll();

    public PenerimaManfaat findById(String id) throws PenerimaNotFoundException;

    public PenerimaManfaat createPenerima(CreatePenerimaRequest request) throws NisnExistsException, KeteranganIllegalForKondisiException, RombelQuotaEmptyException, RombelInactiveException;

    public PenerimaManfaat updatePenerima(UpdatePenerimaRequest request) throws PenerimaNotFoundException, PenerimaIllegalChangeNisnException, KeteranganIllegalForKondisiException, PenerimaNoLongerChangeableException;

    public PenerimaManfaat verifyPenerima(VerifyPenerimaRequest request) throws PenerimaNotFoundException, PetugasNotFoundException, PenerimaWrongStatusException;

    public PenerimaManfaat activatePenerima(String id) throws PenerimaNotFoundException, PenerimaWrongStatusException;

    public PenerimaManfaat rejectPenerima(RejectPenerimaRequest request) throws PenerimaNotFoundException, PenerimaWrongStatusException;

    public PenerimaManfaat deactivatePenerima(DeactivatePenerimaRequest request) throws PenerimaNotFoundException, PenerimaWrongStatusException;

    public PenerimaManfaat reviewPenerima(ReviewPenerimaRequest request) throws PenerimaNotFoundException, PenerimaWrongStatusException;

    // TODO: verification queue, graphs
}
