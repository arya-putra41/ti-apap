package apap.ti._6.simbg_2406406300_be.exceptions.penerima;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class KeteranganIllegalForKondisiException extends ApiException {
    public KeteranganIllegalForKondisiException() {
        super("Keterangan harus diisi apabila siswa memiliki kondisi khusus", HttpStatus.BAD_REQUEST);
    }
}
