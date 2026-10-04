package apap.ti._6.simbg_2406406300_be.models;

import java.time.LocalDate;

import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.UpdateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.SekolahBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.SekolahResponse;
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

    @Column(nullable = false, unique = true, check = @CheckConstraint(name = "npsn_harus_8_karakter", constraint = "char_length(npsn) = 255"))
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

    public void updateFromRequest(UpdateSekolahRequest request) {
        this.setNamaSekolah(request.getNamaSekolah());
        this.setJenjang(request.getJenjang());
        this.setProvinsi(request.getProvinsi());
        this.setKota(request.getKota());
        this.setKecamatan(request.getKecamatan());
        this.setDesa(request.getDesa());
        this.setJalan(request.getJalan());
        this.setNamaKepalaSekolah(request.getNamaKepalaSekolah());
        this.setTeleponSekolah(request.getTeleponSekolah());
        this.setTanggalBergabung(request.getTanggalBergabung());
    }

    public SekolahResponse toResponse() {
        SekolahResponse response = SekolahResponse.builder()
            .id(id)
            .npsn(npsn)
            .namaSekolah(namaSekolah)
            .jenjang(jenjang)
            .provinsi(provinsi)
            .kota(kota)
            .kecamatan(kecamatan)
            .desa(desa)
            .jalan(jalan)
            .namaKepalaSekolah(namaKepalaSekolah)
            .teleponSekolah(teleponSekolah)
            .tanggalBergabung(tanggalBergabung)
            .statusMitra(statusMitra)
            .build();

        return response;
    }

    public SekolahBasicResponse toBasicResponse() {
        SekolahBasicResponse basicResponse = SekolahBasicResponse.builder()
            .id(id)
            .npsn(npsn)
            .namaSekolah(namaSekolah)
            .jenjang(jenjang)
            .statusMitra(statusMitra)
            .build();

        return basicResponse;
    }

    public void deactivate() {
        this.statusMitra = "NONAKTIF";
    }
}
