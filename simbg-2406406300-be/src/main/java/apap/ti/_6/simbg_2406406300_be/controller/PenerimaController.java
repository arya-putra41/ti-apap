package apap.ti._6.simbg_2406406300_be.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaBasicResponse;
import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.models.PenerimaManfaat;
import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaService;

@RestController
@RequestMapping("/api/penerima")
public class PenerimaController {
    
    private PenerimaService penerimaService;

    public PenerimaController(PenerimaService penerimaService) {
        this.penerimaService = penerimaService;
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PenerimaBasicResponse>>> findAll() {
        List<PenerimaBasicResponse> result = penerimaService.findAll().stream()
            .map(penerima -> penerima.toBasicResponse())
            .toList();
        
        BaseResponse<List<PenerimaBasicResponse>> response = BaseResponse.success(result);
        
        return ResponseEntity.ok(response);
    }
}
