package apap.ti._6.simbg_2406406300_be.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;

public interface PetugasRepository extends JpaRepository<PetugasVerifikasi, String> {
    boolean existsByNip(String nip);
}
