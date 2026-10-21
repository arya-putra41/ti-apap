package apap.ti._6.simbg_2406406300_be.repo;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;

public interface PenerimaRepository extends JpaRepository<PenerimaManfaat, String> {
    boolean existsByNisnAndStatusInIgnoreCase(String nisn, List<String> statuses);

    List<PenerimaManfaat> findByRombelId(String rombelId);

    int countByStatus(String status);

    List<PenerimaManfaat> findByStatusOrderByTanggalPengajuanAsc(String status);

    List<PenerimaManfaat> findByStatusAndUpdatedAtBefore(String status, ZonedDateTime updatedAt);
}
