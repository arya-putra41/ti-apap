package apap.ti._6.simbg_2406406300_be.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti._6.simbg_2406406300_be.models.Sekolah;

public interface SekolahRepository extends JpaRepository<Sekolah, String> {
    boolean existsByNpsn(String npsn);

    int countByStatusMitra(String status);

    List<Sekolah> findByStatusMitra(String status);
}
