package apap.ti._6.simbg_2406406300_be.configs;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class PenerimaDaysConfig {
    private int autoRejectMaxDays = 14;
    private int verificationQueueMaxDays = 14;
}
