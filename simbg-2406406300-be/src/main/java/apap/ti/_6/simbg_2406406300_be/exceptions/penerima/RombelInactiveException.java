package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class RombelInactiveException extends ApiException {
    public RombelInactiveException(String rombelId) {
        super(String.format("Rombel dengan ID %s tidak aktif", rombelId), HttpStatus.CONFLICT);
    }
}
