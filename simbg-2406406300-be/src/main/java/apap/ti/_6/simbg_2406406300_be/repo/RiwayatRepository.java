package apap.ti._6.simbg_2406406300_be.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti._6.simbg_2406406300_be.models.RiwayatStatusPenerima;

public interface RiwayatRepository extends JpaRepository<RiwayatStatusPenerima, String> {
    List<RiwayatStatusPenerima> findByPenerimaId(String penerimaId);

    RiwayatStatusPenerima findFirstByPenerimaIdAndStatusBaruOrderByTanggalPerubahanDesc(String penerimaId, String statusBaru);
}
