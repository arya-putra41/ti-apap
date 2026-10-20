package apap.ti._6.simbg_2406406300_be.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import apap.ti._6.simbg_2406406300_be.models.RiwayatStatusPenerima;

public interface RiwayatRepository extends JpaRepository<RiwayatStatusPenerima, String> {
    List<RiwayatStatusPenerima> findByPenerimaId(String penerimaId);

    RiwayatStatusPenerima findFirstByPenerimaIdAndStatusBaruOrderByTanggalPerubahanDesc(String penerimaId, String statusBaru);

    @Query("select r from RiwayatStatusPenerima r where extract(year from r.tanggalPerubahan) = :year")
    public List<RiwayatStatusPenerima> findByYear(@Param("year") int year);
}
