package apap.ti._6.simbg_2406406300_be.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;

public interface RombelRepository extends JpaRepository<RombonganBelajar, String> {
    public List<RombonganBelajar> findBySekolahId(String sekolahId);

    public boolean existsBySekolahIdAndStatus(String sekolahId, String status); 
}
