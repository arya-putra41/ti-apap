package apap.ti._6.simbg_2406406300_be.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "petugas")
public class PetugasVerifikasi extends BaseEntity {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String nip;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String wilayahTugas;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telepon;
}
