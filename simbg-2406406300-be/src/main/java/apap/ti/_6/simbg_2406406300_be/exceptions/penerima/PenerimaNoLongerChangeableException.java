package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class PenerimaNoLongerChangeableException extends ApiException {
    public PenerimaNoLongerChangeableException(String currentStatus) {
        super(String.format("Data penerima berstatus %s tidak dapat diubah", currentStatus), HttpStatus.CONFLICT);
    }
}
