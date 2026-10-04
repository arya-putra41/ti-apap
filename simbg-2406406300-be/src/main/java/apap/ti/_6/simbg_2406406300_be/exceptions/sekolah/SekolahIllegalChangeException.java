package apap.ti._6.simbg_2406406300_be.exceptions.sekolah;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class SekolahIllegalChangeException extends ApiException {
    public SekolahIllegalChangeException(String field) {
        super(String.format("Field %s tidak boleh diubah", field), HttpStatus.BAD_REQUEST);
    }
}
