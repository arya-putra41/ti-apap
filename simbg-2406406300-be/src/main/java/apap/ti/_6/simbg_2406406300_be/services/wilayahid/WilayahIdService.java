package apap.ti._6.simbg_2406406300_be.services.wilayahid;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.responses.wilayahid.WilayahIdResponse;

public interface WilayahIdService {

    public List<WilayahIdResponse> getProvinces();
    public List<WilayahIdResponse> getRegencies(String provinceCode); // kota/kabupaten
    public List<WilayahIdResponse> getDistricts(String regencyCode); // kecamatan
    public List<WilayahIdResponse> getVillages(String districtCode); // kelurahan/desa

    public WilayahIdResponse getProvinceById(String provinceCode);
    public WilayahIdResponse getRegencyById(String regencyCode);
    public WilayahIdResponse getDistrictById(String districtCode);
    public WilayahIdResponse getVillageById(String villageCode);
}
