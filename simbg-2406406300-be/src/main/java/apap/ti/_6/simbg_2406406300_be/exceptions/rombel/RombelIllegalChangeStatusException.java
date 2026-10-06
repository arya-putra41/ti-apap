package apap.ti._6.simbg_2406406300_be.exceptions.rombel;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class RombelIllegalChangeStatusException extends ApiException {
    public RombelIllegalChangeStatusException(String rombelId) {
        super(String.format("Rombel dengan ID %s sudah ditutup. Rombel yang ditutup tidak dapat diaktifkan kembali", rombelId), HttpStatus.CONFLICT);
    }
}
