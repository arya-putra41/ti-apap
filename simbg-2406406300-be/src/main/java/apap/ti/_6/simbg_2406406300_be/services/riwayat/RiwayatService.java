package apap.ti._6.simbg_2406406300_be.services.riwayat;

import java.time.ZonedDateTime;
import java.util.List;

import apap.ti._6.simbg_2406406300_be.exceptions.riwayat.RiwayatNotFoundException;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.models.RiwayatStatusPenerima;

public interface RiwayatService {
    public List<RiwayatStatusPenerima> findAll();

    public RiwayatStatusPenerima findRiwayatById(String id) throws RiwayatNotFoundException;

    public List<RiwayatStatusPenerima> findRiwayatByPenerimaId(String penerimaId);

    public RiwayatStatusPenerima createRiwayat(PenerimaManfaat penerima, String statusSebelumnya, String statusBaru, String keterangan);

    public ZonedDateTime findAktivasiOfPenerima(PenerimaManfaat penerima);
}
