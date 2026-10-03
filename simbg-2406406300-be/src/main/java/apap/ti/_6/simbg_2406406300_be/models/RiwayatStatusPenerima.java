package apap.ti._6.simbg_2406406300_be.models;

import java.time.LocalDateTime;

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
@Table(name = "riwayatstatus")
public class RiwayatStatusPenerima extends ReadOnlyEntity {
    @Id
    private String id;

    @Column(nullable = false)
    private String penerimaId;

    private String statusSebelumnya;

    @Column(nullable = false)
    private String statusBaru;

    @Column(nullable = false)
    private LocalDateTime tanggalPerubahan;

    @Column(nullable = false)
    private String keterangan;
}
