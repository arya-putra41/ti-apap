package apap.ti._6.simbg_2406406300_be.exceptions.sekolah;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class RombelStillActiveException extends ApiException {
    public RombelStillActiveException() {
        super("Sekolah masih memiliki rombongan belajar berstatus Aktif", HttpStatus.CONFLICT);
    }
}
