package apap.ti._6.simbg_2406406300_be.exceptions.sekolah;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class NpsnInexactLengthException extends ApiException {
    public NpsnInexactLengthException() {
        super("Panjang NPSN harus 8 karakter", HttpStatus.BAD_REQUEST);
    }
}
