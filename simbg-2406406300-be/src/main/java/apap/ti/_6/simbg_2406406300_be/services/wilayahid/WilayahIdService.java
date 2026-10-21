package apap.ti._6.simbg_2406406300_be.services.wilayahid;

import java.util.List;

import apap.ti._6.simbg_2406406300_be.dto.responses.wilayahid.WilayahIdResponse;

public interface WilayahIdService {

    public List<WilayahIdResponse> getProvinces();
    public List<WilayahIdResponse> getRegencies(String provinceCode); // kota/kabupaten
    public List<WilayahIdResponse> getDistricts(String regencyCode); // kecamatan
    public List<WilayahIdResponse> getVillages(String districtCode); // kelurahan/desa

    public String getProvinceByName(String name);
    public String getRegencyByName(String name, String provinceCode);
    public String getDistrictByName(String name, String regencyCode);
    public String getVillageByName(String name, String districtCode);
}
