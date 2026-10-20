package apap.ti._6.simbg_2406406300_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/provinsi/{provinceCode}")
    public ResponseEntity<BaseResponse<WilayahIdResponse>> getProvinceById(@PathVariable String provinceCode) {
        WilayahIdResponse result = wilayahIdService.getProvinceById(provinceCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kota/{provinceCode}")
    public ResponseEntity<BaseResponse<List<WilayahIdResponse>>> getRegencies(@PathVariable String provinceCode) {
        List<WilayahIdResponse> result = wilayahIdService.getRegencies(provinceCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kota/detail/{regencyCode}")
    public ResponseEntity<BaseResponse<WilayahIdResponse>> getRegencyById(@PathVariable String regencyCode) {
        WilayahIdResponse result = wilayahIdService.getRegencyById(regencyCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kecamatan/{regencyCode}")
    public ResponseEntity<BaseResponse<List<WilayahIdResponse>>> getDistricts(@PathVariable String regencyCode) {
        List<WilayahIdResponse> result = wilayahIdService.getDistricts(regencyCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kecamatan/detail/{districtCode}")
    public ResponseEntity<BaseResponse<WilayahIdResponse>> getDistrictById(@PathVariable String districtCode) {
        WilayahIdResponse result = wilayahIdService.getDistrictById(districtCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kelurahan/{districtCode}")
    public ResponseEntity<BaseResponse<List<WilayahIdResponse>>> getVillages(@PathVariable String districtCode) {
        List<WilayahIdResponse> result = wilayahIdService.getVillages(districtCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }

    @GetMapping("/kelurahan/detail/{villageCode}")
    public ResponseEntity<BaseResponse<WilayahIdResponse>> getVillageById(@PathVariable String villageCode) {
        WilayahIdResponse result = wilayahIdService.getVillageById(villageCode);
        return ResponseEntity.ok(BaseResponse.success(result));
    }
}