package apap.ti._6.simbg_2406406300_be.models;

import java.time.LocalDate;
import java.time.ZonedDateTime;

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
@Table(name = "penerima")
public class PenerimaManfaat extends BaseEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String rombelId;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String nisn;

    @Column(nullable = false)
    private String jenisKelamin;

    @Column(nullable = false)
    private LocalDate tanggalLahir;

    @Column(nullable = false)
    private String kondisiKhusus;

    private String keterangan;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String petugasVerifikasiId;

    @Column(nullable = false)
    private ZonedDateTime tanggalPengajuan;

    private ZonedDateTime tanggalVerifikasi;
}
