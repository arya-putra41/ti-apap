package apap.ti._6.simbg_2406406300_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.models.PetugasVerifikasi;
import apap.ti._6.simbg_2406406300_be.services.petugas.PetugasQueryService;

@RestController
@RequestMapping("/api/petugas")
public class PetugasController {
    
    private PetugasQueryService petugasQueryService;

    public PetugasController(PetugasQueryService petugasQueryService) {
        this.petugasQueryService = petugasQueryService;
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PetugasVerifikasi>>> findAll() {
        List<PetugasVerifikasi> result = petugasQueryService.findAll();

        BaseResponse<List<PetugasVerifikasi>> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }
}
