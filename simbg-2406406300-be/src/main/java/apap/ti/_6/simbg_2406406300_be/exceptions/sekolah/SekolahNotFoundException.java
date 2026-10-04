package apap.ti._6.simbg_2406406300_be.exceptions.sekolah;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class SekolahNotFoundException extends ApiException {
    public SekolahNotFoundException(String id) {
        super(String.format("Sekolah dengan id %s tidak ditemukan", id), HttpStatus.NOT_FOUND);
    }
}
