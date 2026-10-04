package apap.ti._6.simbg_2406406300_be.exceptions.petugas;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class PetugasNotFoundException extends ApiException {
    public PetugasNotFoundException(String id) {
        super(String.format("Petugas dengan ID %s tidak ditemukan", id), HttpStatus.NOT_FOUND);
    }
}
