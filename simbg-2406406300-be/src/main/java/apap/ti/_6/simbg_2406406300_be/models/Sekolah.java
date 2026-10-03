package apap.ti._6.simbg_2406406300_be.models;

import java.time.LocalDate;

import jakarta.persistence.CheckConstraint;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "sekolah")
public class Sekolah extends BaseEntity {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String npsn;

    @Column(nullable = false)
    private String namaSekolah;

    @Column(nullable = false)
    private String jenjang;

    @Column(nullable = false)
    private String provinsi;

    @Column(nullable = false)
    private String kota;

    @Column(nullable = false)
    private String kecamatan;

    @Column(nullable = false)
    private String desa;

    @Column(nullable = false)
    private String jalan;

    @Column(nullable = false)
    private String namaKepalaSekolah;

    @Column(nullable = false)
    private String teleponSekolah;

    @Column(nullable = false, check = @CheckConstraint(name = "date_cannot_be_after_now", constraint = "tanggal_bergabung <= NOW()"))
    private LocalDate tanggalBergabung;

    @Column(nullable = false)
    private String statusMitra;
}
