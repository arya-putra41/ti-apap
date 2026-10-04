package apap.ti._6.simbg_2406406300_be.utils;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.PenerimaRepository;
import apap.ti._6.simbg_2406406300_be.repo.PetugasRepository;
import apap.ti._6.simbg_2406406300_be.repo.RombelRepository;
import apap.ti._6.simbg_2406406300_be.repo.SekolahRepository;
import net.datafaker.Faker;

@Component
public class Seeder implements CommandLineRunner {
    
    private SekolahRepository sekolahRepository;
    private PetugasRepository petugasRepository;
    private RombelRepository rombelRepository;
    private PenerimaRepository penerimaRepository;
    private IdGeneratorService idGeneratorService;

    public Seeder(SekolahRepository sekolahRepository, PetugasRepository petugasRepository, RombelRepository rombelRepository, PenerimaRepository penerimaRepository, IdGeneratorService idGeneratorService) {
        this.sekolahRepository = sekolahRepository;
        this.petugasRepository = petugasRepository;
        this.rombelRepository = rombelRepository;
        this.penerimaRepository = penerimaRepository;

        this.idGeneratorService = idGeneratorService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Ciptakan sekolah
        if (sekolahRepository.findAll().size() == 0) {
            // Sekolah 1: SDN 1 Depok
            sekolahRepository.save(
                Sekolah.builder()
                    .id(idGeneratorService.generateSekolahId())
                    .npsn("20210123")
                    .namaSekolah("SDN 1 Depok")
                    .jenjang("SD")
                    .provinsi("Jawa Barat")
                    .kota("Depok")
                    .kecamatan("Beji")
                    .desa("Kemiri Muka")
                    .jalan("Jl. Margonda Raya No. 1")
                    .namaKepalaSekolah("Siti Aminah")
                    .teleponSekolah("0217775678")
                    .tanggalBergabung(LocalDate.of(2026, 7, 1))
                    .statusMitra("AKTIF")
                .build()
            );

            // Sekolah 2: SMPN 21 Surabaya
            sekolahRepository.save(
                Sekolah.builder()
                    .id(idGeneratorService.generateSekolahId())
                    .npsn("20210124")
                    .namaSekolah("SMPN 21 Surabaya")
                    .jenjang("SMP")
                    .provinsi("Jawa Timur")
                    .kota("Surabaya")
                    .kecamatan("Rungkut")
                    .desa("Wonorejo")
                    .jalan("Jl. Apel No. 15")
                    .namaKepalaSekolah("Hasan Mukti")
                    .teleponSekolah("0211234567")
                    .tanggalBergabung(LocalDate.of(2026, 6, 2))
                    .statusMitra("AKTIF")
                .build()
            );

            // Sekolah 3: SMAN 2 Tangsel
            sekolahRepository.save(
                Sekolah.builder()
                    .id(idGeneratorService.generateSekolahId())
                    .npsn("20210125")
                    .namaSekolah("SMAN 2 Tangsel")
                    .jenjang("SMA/SMK")
                    .provinsi("Banten")
                    .kota("Tangerang Selatan")
                    .kecamatan("Serpong")
                    .desa("Rawa Buntu")
                    .jalan("Jl. Dinova Si Pintar No. 10")
                    .namaKepalaSekolah("Ayane Sakura")
                    .teleponSekolah("0214567890")
                    .tanggalBergabung(LocalDate.of(1991, 11, 20))
                    .statusMitra("AKTIF")
                .build()
            );
        }

        // Ciptakan petugas
        String[] daerahTugas = {"Depok", "Surabaya", "Tangerang Selatan"};
        if (petugasRepository.findAll().size() == 0) {
            for (int index = 0; index < 10; index++) {
                petugasRepository.save(
                    PetugasVerifikasi.builder()
                    .id(idGeneratorService.generatePetugasId())
                    .nip(String.valueOf(index))
                    .nama(faker.name().fullName())
                    .wilayahTugas(daerahTugas[index % daerahTugas.length])
                    .email(faker.name().firstName() + "@mail.com")
                    .telepon("021" + index)
                    .build()
                );
            }
        }

        // TODO: Ciptakan rombel dan penerima
    }
}
