package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class NisnExistsException extends ApiException {
    public NisnExistsException(String nisn) {
        super(String.format("NISN %s masih memiliki pengajuan berjalan", nisn), HttpStatus.CONFLICT);
    }
}
