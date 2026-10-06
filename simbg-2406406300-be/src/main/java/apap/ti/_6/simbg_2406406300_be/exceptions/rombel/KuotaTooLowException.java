package apap.ti._6.simbg_2406406300_be.exceptions.rombel;

import org.springframework.http.HttpStatus;

import apap.ti._6.simbg_2406406300_be.exceptions.ApiException;

public class KuotaTooLowException extends ApiException {
    public KuotaTooLowException(int newKuota, int jumlahTerdaftar) {
        super(String.format("Kuota baru (%s) tidak boleh lebih kecil dari jumlah penerima terdaftar (%s)", newKuota, jumlahTerdaftar), HttpStatus.CONFLICT);
    }
}
