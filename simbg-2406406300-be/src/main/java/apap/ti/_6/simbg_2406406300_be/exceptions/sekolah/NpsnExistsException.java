package apap.ti._6.simbg_2406406300_be.exceptions.sekolah;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class NpsnExistsException extends ApiException {
    public NpsnExistsException(String npsn) {
        super(String.format("NPSN %s sudah terdaftar", npsn), HttpStatus.CONFLICT);
    }
}
