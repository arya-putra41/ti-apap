package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class PenerimaIllegalChangeNisnException extends ApiException {
    public PenerimaIllegalChangeNisnException() {
        super("NISN peserta yang sudah terdaftar tidak dapat diubah", HttpStatus.BAD_REQUEST);
    }
}
