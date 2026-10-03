package apap.ti._6.simbg_2406406300_be.utils;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorService {
    private final JdbcTemplate jdbcTemplate;

    public IdGeneratorService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        createSequences();
    }

    public String generateSekolahId() {
        return generate("SKL", "sekolah_id_seq");
    }

    public String generatePetugasId() {
        return generate("PTG", "petugas_id_seq");
    }
 
    public String generateRombelId() {
        return generate("RBL", "rombel_id_seq");
    }
 
    public String generatePenerimaId() {
        return generate("PNR", "penerima_id_seq");
    }
 
    public String generateRiwayatId() {
        return generate("RWT", "riwayat_id_seq");
    }
 
    private void createSequences() {
        jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS sekolah_id_seq START 1");
        jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS petugas_id_seq START 1");
        jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS rombel_id_seq START 1");
        jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS penerima_id_seq START 1");
        jdbcTemplate.execute("CREATE SEQUENCE IF NOT EXISTS riwayat_id_seq START 1");
    }

    private String generate(String prefix, String seqName) {
        Long number = jdbcTemplate.queryForObject("SELECT nextval('" + seqName + "')", Long.class);
        return String.format("%s%04d", prefix, number);
    }
}
