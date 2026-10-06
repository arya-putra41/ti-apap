package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class PenerimaNotFoundException extends ApiException {
    public PenerimaNotFoundException(String id) {
        super(String.format("Penerima manfaat dengan ID %s tidak ditemukan", id), HttpStatus.NOT_FOUND);
    }
}
