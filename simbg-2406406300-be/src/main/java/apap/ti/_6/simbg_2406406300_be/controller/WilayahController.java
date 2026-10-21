package apap.ti._6.simbg_2406406300_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406406300_be.dto.responses.wilayahid.WilayahIdResponse;
import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.services.wilayahid.WilayahIdService;

@RestController
@RequestMapping("api/wilayah")
public class WilayahController {

    private final WilayahIdService wilayahIdService;

    public WilayahController(WilayahIdService wilayahIdService) {
        this.wilayahIdService = wilayahIdService;
    }
    
    @GetMapping("/provinsi")
    public ResponseEntity<BaseResponse<List<WilayahIdResponse>>> getProvinces() {
        List<WilayahIdResponse> result = wilayahIdService.getProvinces();
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kota/{provinceCode}")
    public ResponseEntity<BaseResponse<List<WilayahIdResponse>>> getRegencies(@PathVariable String provinceCode) {
        List<WilayahIdResponse> result = wilayahIdService.getRegencies(provinceCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kecamatan/{regencyCode}")
    public ResponseEntity<BaseResponse<List<WilayahIdResponse>>> getDistricts(@PathVariable String regencyCode) {
        List<WilayahIdResponse> result = wilayahIdService.getDistricts(regencyCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kelurahan/{districtCode}")
    public ResponseEntity<BaseResponse<List<WilayahIdResponse>>> getVillages(@PathVariable String districtCode) {
        List<WilayahIdResponse> result = wilayahIdService.getVillages(districtCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    // GET /api/wilayah/provinsi/code?name=Aceh
    @GetMapping("/provinsi/code")
    public ResponseEntity<BaseResponse<String>> getProvinceCodeByName(@RequestParam String name) {
        String code = wilayahIdService.getProvinceByName(name);
        return ResponseEntity.ok(BaseResponse.success(code));
    }

    // GET /api/wilayah/kota/code?name=Kota%20Banda%20Aceh&provinceCode=11
    @GetMapping("/kota/code")
    public ResponseEntity<BaseResponse<String>> getRegencyCodeByName(
            @RequestParam String name, 
            @RequestParam String provinceCode) {
        String code = wilayahIdService.getRegencyByName(name, provinceCode);
        return ResponseEntity.ok(BaseResponse.success(code));
    }

    // GET /api/wilayah/kecamatan/code?name=Kuta%20Alam&regencyCode=11.71
    @GetMapping("/kecamatan/code")
    public ResponseEntity<BaseResponse<String>> getDistrictCodeByName(
            @RequestParam String name, 
            @RequestParam String regencyCode) {
        String code = wilayahIdService.getDistrictByName(name, regencyCode);
        return ResponseEntity.ok(BaseResponse.success(code));
    }

    // GET /api/wilayah/kelurahan/code?name=Peunayong&districtCode=11.71.01
    @GetMapping("/kelurahan/code")
    public ResponseEntity<BaseResponse<String>> getVillageCodeByName(
            @RequestParam String name, 
            @RequestParam String districtCode) {
        String code = wilayahIdService.getVillageByName(name, districtCode);
        return ResponseEntity.ok(BaseResponse.success(code));
    }
}