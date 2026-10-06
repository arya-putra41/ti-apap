package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class RombelQuotaEmptyException extends ApiException {
    public RombelQuotaEmptyException(String rombelId) {
        super(String.format("Rombel dengan ID %s sudah penuh", rombelId), HttpStatus.CONFLICT);
    }
}
