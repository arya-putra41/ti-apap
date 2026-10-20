package apap.ti._6.simbg_2406406300_be.services.wilayahid;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import apap.ti._6.simbg_2406406300_be.dto.responses.wilayahid.WilayahIdResponse;

@Service
public class WilayahIdServiceImpl implements WilayahIdService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://wilayah.id/api";

    @Override
    public List<WilayahIdResponse> getProvinces() {
        String url = BASE_URL + "/provinces.json";
        WilayahIdResponse[] response = restTemplate.getForObject(url, WilayahIdResponse[].class);
        return response != null ? Arrays.asList(response) : Collections.emptyList();
    }

    @Override
    public List<WilayahIdResponse> getRegencies(String provinceCode) {
        String url = BASE_URL + "/regencies/" + provinceCode + ".json";
        WilayahIdResponse[] response = restTemplate.getForObject(url, WilayahIdResponse[].class);
        return response != null ? Arrays.asList(response) : Collections.emptyList();
    }

    @Override
    public List<WilayahIdResponse> getDistricts(String regencyCode) {
        String url = BASE_URL + "/districts/" + regencyCode + ".json";
        WilayahIdResponse[] response = restTemplate.getForObject(url, WilayahIdResponse[].class);
        return response != null ? Arrays.asList(response) : Collections.emptyList();
    }

    @Override
    public List<WilayahIdResponse> getVillages(String districtCode) {
        String url = BASE_URL + "/villages/" + districtCode + ".json";
        WilayahIdResponse[] response = restTemplate.getForObject(url, WilayahIdResponse[].class);
        return response != null ? Arrays.asList(response) : Collections.emptyList();
    }

    @Override
    public WilayahIdResponse getProvinceById(String provinceCode) {
        return getProvinces().stream()
                .filter(p -> p.getCode().equalsIgnoreCase(provinceCode))
                .findFirst()
                .orElse(null);
    }

    @Override
    public WilayahIdResponse getRegencyById(String regencyCode) {
        // Kode kabupaten/kota memiliki format 'XX.YY' (di mana XX adalah kode provinsi)
        if (regencyCode == null || !regencyCode.contains(".")) {
            return null;
        }
        String provinceCode = regencyCode.split("\\.")[0];
        return getRegencies(provinceCode).stream()
                .filter(r -> r.getCode().equalsIgnoreCase(regencyCode))
                .findFirst()
                .orElse(null);
    }

    @Override
    public WilayahIdResponse getDistrictById(String districtCode) {
        // Kode kecamatan memiliki format 'XX.YY.ZZ' (di mana XX.YY adalah kode kabupaten/kota)
        if (districtCode == null || districtCode.indexOf('.') == -1) {
            return null;
        }
        String regencyCode = districtCode.substring(0, districtCode.lastIndexOf('.'));
        return getDistricts(regencyCode).stream()
                .filter(d -> d.getCode().equalsIgnoreCase(districtCode))
                .findFirst()
                .orElse(null);
    }

    @Override
    public WilayahIdResponse getVillageById(String villageCode) {
        // Kode kelurahan/desa memiliki format 'XX.YY.ZZ.WW' (di mana XX.YY.ZZ adalah kode kecamatan)
        if (villageCode == null || villageCode.indexOf('.') == -1) {
            return null;
        }
        String districtCode = villageCode.substring(0, villageCode.lastIndexOf('.'));
        return getVillages(districtCode).stream()
                .filter(v -> v.getCode().equalsIgnoreCase(villageCode))
                .findFirst()
                .orElse(null);
    }
}