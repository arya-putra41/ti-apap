package apap.ti._6.simbg_2406406300_be.exceptions.rombel;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class RombelNotFoundException extends ApiException {
     public RombelNotFoundException(String id) {
        super(String.format("Rombel dengan ID %s tidak ditemukan", id), HttpStatus.NOT_FOUND);
    }
}
