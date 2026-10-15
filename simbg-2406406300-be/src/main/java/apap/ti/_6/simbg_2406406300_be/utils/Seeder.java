package apap.ti._6.simbg_2406406300_be.utils;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.CreateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.CreateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.repo.PetugasRepository;
import apap.ti._6.simbg_2406406300_be.services.rombel.RombelService;
import apap.ti._6.simbg_2406406300_be.services.sekolah.SekolahService;
import net.datafaker.Faker;

@Component
public class Seeder implements CommandLineRunner {

    private SekolahService sekolahService;
    private PetugasRepository petugasRepository;
    private RombelService rombelService;
    private IdGeneratorService idGeneratorService;

    public Seeder(SekolahService sekolahService, PetugasRepository petugasRepository, RombelService rombelService, IdGeneratorService idGeneratorService) {
        this.sekolahService = sekolahService;
        this.petugasRepository = petugasRepository;
        this.rombelService = rombelService;

        this.idGeneratorService = idGeneratorService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Ciptakan sekolah
        if (sekolahService.findAll().isEmpty()) {
            CreateSekolahRequest sd = CreateSekolahRequest.builder()
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
                .build();
            sekolahService.createSekolah(sd);

            // Sekolah 2: SMPN 21 Surabaya
            CreateSekolahRequest smp = CreateSekolahRequest.builder()
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
                .build();
            
            sekolahService.createSekolah(smp);

            // Sekolah 3: SMAN 2 Tangsel
            CreateSekolahRequest sma = CreateSekolahRequest.builder()
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
                .build();
            sekolahService.createSekolah(sma);
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
        List<Sekolah> schoolList = sekolahService.findAll();
        if (rombelService.findAll().size() == 0) {
            if (!schoolList.isEmpty()) {
                for (Sekolah currentSekolah : schoolList) {
                    for (int loopCount=1; loopCount<=3; loopCount++) {
                        CreateRombelRequest newRombel = CreateRombelRequest.builder()
                            .sekolahId(currentSekolah.getId())
                            .namaRombel("Kelas " + loopCount)
                            .tingkat(loopCount)
                            .tahunAjaran("2026/2027")
                            .kuotaPenerima(20)
                            .build();

                        rombelService.createRombel(newRombel);
                    }
                }
            }
        }

        // TODO: Ciptakan penerima
    }
}
