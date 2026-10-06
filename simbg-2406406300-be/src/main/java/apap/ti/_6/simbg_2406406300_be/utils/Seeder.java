package apap.ti._6.simbg_2406406300_be.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
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

    public Seeder(SekolahRepository sekolahRepository, PetugasRepository petugasRepository,
            RombelRepository rombelRepository, PenerimaRepository penerimaRepository,
            IdGeneratorService idGeneratorService) {
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
        if (sekolahRepository.count() == 0) {
            // Sekolah 1: SDN 1 Depok
            Sekolah sd = Sekolah.builder()
                    .id(idGeneratorService.generateSekolahId())
                    .npsn("20210123")
                    .namaSekolah("SDN 1 Depok")
                    .jenjang("SD")
                    .provinsi("Jawa Barat")
                    .kota("Depok")
                    .kecamatan("Beji")
                    .desa("Kemiri Muka")
                    .jalan("Jl. Margonda Raya No. 1")
                    .namaKepalaSekolah("Nao Toyama")
                    .teleponSekolah("0217775678")
                    .tanggalBergabung(LocalDate.of(2026, 7, 1))
                    .rombel(new ArrayList<>())
                    .statusMitra("AKTIF")
                .build();
            sekolahRepository.save(sd);

            // Sekolah 2: SMPN 21 Surabaya
            Sekolah smp = Sekolah.builder()
                    .id(idGeneratorService.generateSekolahId())
                    .npsn("20210124")
                    .namaSekolah("SMPN 21 Surabaya")
                    .jenjang("SMP")
                    .provinsi("Jawa Timur")
                    .kota("Surabaya")
                    .kecamatan("Rungkut")
                    .desa("Wonorejo")
                    .jalan("Jl. Apel No. 15")
                    .namaKepalaSekolah("Saori Hayami")
                    .teleponSekolah("0211234567")
                    .tanggalBergabung(LocalDate.of(2026, 6, 2))
                    .rombel(new ArrayList<>())
                    .statusMitra("AKTIF")
                .build();
            sekolahRepository.save(smp);

            // Sekolah 3: SMAN 2 Tangsel
            Sekolah sma = Sekolah.builder()
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
                    .rombel(new ArrayList<>())
                    .statusMitra("AKTIF")
                .build();
            sekolahRepository.save(sma);
        }

        // Ciptakan petugas
        String[] daerahTugas = {"Depok", "Surabaya", "Tangerang Selatan"};
        if (petugasRepository.count() == 0) {
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

        // Ciptakan rombel
        List<Sekolah> schoolList = sekolahRepository.findAll();
        if (rombelRepository.count() == 0) {
            if (!schoolList.isEmpty()) {
                List<RombonganBelajar> listRombel = new ArrayList<>();
                for (Sekolah currentSekolah : schoolList) {
                    for (int loopCount=1; loopCount<=3; loopCount++) {
                        RombonganBelajar newRombel = RombonganBelajar.builder()
                        .id(idGeneratorService.generateRombelId())
                        .sekolahId(currentSekolah.getId())
                        .namaRombel("Kelas " + loopCount)
                        .tingkat(loopCount)
                        .tahunAjaran("2026/2027")
                        .kuotaPenerima(20)
                        .jumlahPenerimaTerdaftar(0)
                        .status("AKTIF")
                        .build();

                        listRombel.add(newRombel);
                        currentSekolah.addRombel(newRombel);
                    }
                }

                rombelRepository.saveAll(listRombel);
                sekolahRepository.saveAll(schoolList);
            }
        }

        // Buat sekolah tidak aktif untuk kepentingan testing rombel
        Sekolah sekolahInactive = Sekolah.builder()
                    .id(idGeneratorService.generateSekolahId())
                    .npsn("20210126")
                    .namaSekolah("SMAN 69 Tidak Aktif")
                    .jenjang("SMA/SMK")
                    .provinsi("Jawa Barat")
                    .kota("Depok")
                    .kecamatan("Beji")
                    .desa("Kemiri Muka")
                    .jalan("Jl. Margonda Raya No. 1")
                    .namaKepalaSekolah("Rie Takahashi")
                    .teleponSekolah("0219999999")
                    .tanggalBergabung(LocalDate.of(2026, 7, 1))
                    .rombel(new ArrayList<>())
                    .statusMitra("NONAKTIF")
                .build();
        sekolahRepository.save(sekolahInactive);

        // TODO: Ciptakan penerima
    }
}
