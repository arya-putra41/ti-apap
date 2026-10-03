package apap.ti._6.simbg_2406406300_be.models;

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
@Table(name = "rombel")
public class RombonganBelajar extends BaseEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String sekolahId;

    @Column(nullable = false)
    private String namaRombel;

    @Column(nullable = false)
    private int tingkat;

    @Column(nullable = false)
    private String tahunAjaran;

    @Column(nullable = false, check = @CheckConstraint(name = "kuota_cannot_be_less_than_one", constraint = "kuota_penerima > 0"))
    private Integer kuotaPenerima;

    @Column(nullable = false)
    private Integer jumlahPenerimaTerdaftar;

    @Column(nullable = false)
    private String status;
}
