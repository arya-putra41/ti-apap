package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class PenerimaWrongStatusException extends ApiException {
    public PenerimaWrongStatusException(String operation, String statusNeeded) {
        super(String.format("%s hanya dapat dilakukan pada penerima berstatus %s", operation, statusNeeded), HttpStatus.CONFLICT);
    }
}
