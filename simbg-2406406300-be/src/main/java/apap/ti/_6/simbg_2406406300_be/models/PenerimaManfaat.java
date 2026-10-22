package apap.ti._6.simbg_2406406300_be.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.UpdatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaQueueResponse;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "rombel_id")
    private RombonganBelajar rombel;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false, check = @CheckConstraint(name = "nisn_harus_10_karakter", constraint = "char_length(nisn) = 10"))
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

    @ManyToOne
    @JoinColumn(name = "petugas_verifikasi_id")
    private PetugasVerifikasi petugasVerifikasi;

    @Column(nullable = false)
    private ZonedDateTime tanggalPengajuan;

    private ZonedDateTime tanggalVerifikasi;

    public void updateFromRequest(UpdatePenerimaRequest request) {
        this.setNama(request.getNama());
        this.setJenisKelamin(request.getJenisKelamin());
        this.setTanggalLahir(request.getTanggalLahir());
        this.setKondisiKhusus(request.getKondisiKhusus());
        this.setKeterangan(request.getKeterangan());

        if ("AKTIF".equalsIgnoreCase(status)) {
            this.setStatus("DIAJUKAN");
        }
    }

    public void verify(PetugasVerifikasi petugas) {
        this.setPetugasVerifikasi(petugas);
        this.setTanggalVerifikasi(ZonedDateTime.now(ZoneId.of("UTC+07:00")));
        this.setStatus("TERVERIFIKASI");
    }

    public void review() {
        this.setTanggalPengajuan(ZonedDateTime.now(ZoneId.of("UTC+07:00")));
        this.setStatus("DIAJUKAN");
    }

    public void activate() {
        this.setStatus("AKTIF");
    }

    public void reject() {
        this.setStatus("DITOLAK");
    }

    public void deactivate() {
        this.setStatus("NONAKTIF");
    }

    public PenerimaBasicResponse toBasicResponse() {
        PenerimaBasicResponse response = PenerimaBasicResponse.builder()
            .id(id)
            .nama(nama)
            .nisn(nisn)
            .rombelId(rombel.getId())
            .namaRombel(rombel.getNamaRombel())
            .namaSekolah(rombel.getSekolah().getNamaSekolah())
            .jenisKelamin(jenisKelamin)
            .kondisiKhusus(kondisiKhusus)
            .status(status)
            .tanggalPengajuan(tanggalPengajuan)
            .build();
        return response;
    }

    public PenerimaQueueResponse toQueueResponse() {
        PenerimaQueueResponse response = PenerimaQueueResponse.builder()
            .id(id)
            .nama(nama)
            .nisn(nisn)
            .rombelId(rombel.getId())
            .namaRombel(rombel.getNamaRombel())
            .namaSekolah(rombel.getSekolah().getNamaSekolah())
            .jenisKelamin(jenisKelamin)
            .kondisiKhusus(kondisiKhusus)
            .status(status)
            .tanggalPengajuan(tanggalPengajuan)
            .jumlahHariMenunggu((int) ChronoUnit.DAYS.between(tanggalPengajuan, ZonedDateTime.now(ZoneId.of("UTC+07:00"))))
            .build();
        return response;
    }
}
