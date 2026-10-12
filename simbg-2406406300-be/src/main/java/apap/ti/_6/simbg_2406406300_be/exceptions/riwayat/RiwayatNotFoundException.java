package apap.ti._6.simbg_2406406300_be.exceptions.riwayat;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class RiwayatNotFoundException extends ApiException {
    public RiwayatNotFoundException(String id) {
        super(String.format("Riwayat dengan ID %s tidak ditemukan", id), HttpStatus.NOT_FOUND);
    }
}
