    package apap.ti._6.simbg_2406406300_be.dto.responses.wilayahid;

    import java.util.List;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class WilayahIdWrapper<T> {
        private List<T> data;
    }
