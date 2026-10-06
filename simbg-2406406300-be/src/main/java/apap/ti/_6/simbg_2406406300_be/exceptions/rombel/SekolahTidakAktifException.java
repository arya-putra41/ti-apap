package apap.ti._6.simbg_2406406300_be.exceptions.rombel;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class SekolahTidakAktifException extends ApiException {
    public SekolahTidakAktifException(String sekolahId) {
        super(String.format("Sekolah dengan ID %s tidak aktif", sekolahId), HttpStatus.CONFLICT);
    }
}
