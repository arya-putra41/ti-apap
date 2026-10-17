package apap.ti._6.simbg_2406406300_be.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;

public interface RombelRepository extends JpaRepository<RombonganBelajar, String> {
    public List<RombonganBelajar> findBySekolahId(String sekolahId);

    public boolean existsBySekolahIdAndStatus(String sekolahId, String status); 

    int countByStatus(String status);

    @Query("SELECT CASE WHEN SUM(r.kuotaPenerima) > 0 THEN (CAST(SUM(r.jumlahPenerimaTerdaftar) AS double) / SUM(r.kuotaPenerima)) * 100.0 ELSE 0.0 END FROM RombonganBelajar r")
    double getKuotaTerpakai();
}
