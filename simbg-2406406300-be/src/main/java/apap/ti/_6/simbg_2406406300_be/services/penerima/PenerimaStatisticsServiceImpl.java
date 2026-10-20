package apap.ti._6.simbg_2406406300_be.services.penerima;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaStatistics;
import apap.ti._6.simbg_2406406300_be.models.RiwayatStatusPenerima;
import apap.ti._6.simbg_2406406300_be.repo.RiwayatRepository;

@Service
public class PenerimaStatisticsServiceImpl implements PenerimaStatisticsService {

    private RiwayatRepository riwayatRepository;

    public PenerimaStatisticsServiceImpl(RiwayatRepository riwayatRepository) {
        this.riwayatRepository = riwayatRepository;
    }

    @Override
    public PenerimaStatistics getStatistics(String period, Integer year) {
        // Validasi input period & tahun
        if (period == null || (!period.equalsIgnoreCase("BULANAN") && !period.equalsIgnoreCase("KUARTAL"))) {
            throw new IllegalArgumentException("Period harus BULANAN atau KUARTAL");
        }

        boolean isBulanan = period.equalsIgnoreCase("BULANAN");
        int size = isBulanan ? 12 : 4;

        List<String> labels = isBulanan
                ? List.of("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agt", "Sep", "Okt", "Nov", "Des")
                : List.of("Q1", "Q2", "Q3", "Q4");

        int[] diajukan = new int[size];
        int[] diverifikasi = new int[size];
        int[] ditolak = new int[size];

        // Fetch data riwayat di tahun tersebut
        List<RiwayatStatusPenerima> listRiwayat = riwayatRepository.findByYear(year);

        for (RiwayatStatusPenerima r : listRiwayat) {
            // Dapatkan bulan (1-12) dari tanggal riwayat
            int month = r.getCreatedAt().getMonthValue();

            // Hitung indeks array (0-11 untuk BULANAN, 0-3 untuk KUARTAL)
            int index = isBulanan ? (month - 1) : ((month - 1) / 3);

            String status = r.getStatusBaru().toUpperCase();
            if ("DIAJUKAN".equals(status)) {
                diajukan[index]++;
            } else if ("TERVERIFIKASI".equals(status)) {
                diverifikasi[index]++;
            } else if ("DITOLAK".equals(status)) {
                ditolak[index]++;
            }
        }

        return PenerimaStatistics.builder()
                .period(period.toUpperCase())
                .year(year)
                .labels(labels)
                .diajukan(Arrays.stream(diajukan).boxed().toList())
                .diverifikasi(Arrays.stream(diverifikasi).boxed().toList())
                .ditolak(Arrays.stream(ditolak).boxed().toList())
                .build();
    }
}
