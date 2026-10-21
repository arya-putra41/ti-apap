package apap.ti._6.simbg_2406406300_be.services.wilayahid;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import apap.ti._6.simbg_2406406300_be.dto.responses.wilayahid.WilayahIdResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.wilayahid.WilayahIdWrapper;

@Service
public class WilayahIdServiceImpl implements WilayahIdService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://wilayah.id/api";

    // Helper method bertipe ParameterizedTypeReference untuk menangani deserialisasi generik
    private List<WilayahIdResponse> fetchWilayahList(String url) {
        try {
            ResponseEntity<WilayahIdWrapper<WilayahIdResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<WilayahIdWrapper<WilayahIdResponse>>() {}
            );

            WilayahIdWrapper<WilayahIdResponse> body = response.getBody();
            return body != null && body.getData() != null ? body.getData() : Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error fetching wilayah data from " + url + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<WilayahIdResponse> getProvinces() {
        return fetchWilayahList(BASE_URL + "/provinces.json");
    }

    @Override
    public List<WilayahIdResponse> getRegencies(String provinceCode) {
        return fetchWilayahList(BASE_URL + "/regencies/" + provinceCode + ".json");
    }

    @Override
    public List<WilayahIdResponse> getDistricts(String regencyCode) {
        return fetchWilayahList(BASE_URL + "/districts/" + regencyCode + ".json");
    }

    @Override
    public List<WilayahIdResponse> getVillages(String districtCode) {
        return fetchWilayahList(BASE_URL + "/villages/" + districtCode + ".json");
    }

    @Override
    public String getProvinceByName(String name) {
        if (name == null) return null;

        WilayahIdResponse province = getProvinces().stream()
                .filter(p -> p.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
        return province != null ? province.getCode() : null;
    }

    @Override
    public String getRegencyByName(String name, String provinceCode) {
        if (name == null || provinceCode == null) return null;
        WilayahIdResponse regency = getRegencies(provinceCode).stream()
                .filter(r -> r.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
        return regency != null ? regency.getCode() : null;
    }

    @Override
    public String getDistrictByName(String name, String regencyCode) {
        if (name == null || regencyCode == null) return null;
        WilayahIdResponse district = getDistricts(regencyCode).stream()
                .filter(d -> d.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
        return district != null ? district.getCode() : null;
    }

    @Override
    public String getVillageByName(String name, String districtCode) {
        if (name == null || districtCode == null) return null;
        WilayahIdResponse village = getVillages(districtCode).stream()
                .filter(v -> v.getName().equalsIgnoreCase(name.trim()))
                .findFirst()
                .orElse(null);
        return village != null ? village.getCode() : null;
    }
}