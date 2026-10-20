package apap.ti._6.simbg_2406406300_be.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.CreatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.DeactivatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.RejectPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.ReviewPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.UpdatePenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.penerima.VerifyPenerimaRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.penerima.PenerimaStatistics;
import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaService;
import apap.ti._6.simbg_2406406300_be.services.penerima.PenerimaStatisticsService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/penerima")
public class PenerimaController {
    
    private PenerimaService penerimaService;
    private PenerimaStatisticsService penerimaStatisticsService;

    public PenerimaController(PenerimaService penerimaService, PenerimaStatisticsService penerimaStatisticsService) {
        this.penerimaService = penerimaService;
        this.penerimaStatisticsService = penerimaStatisticsService;
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<List<PenerimaBasicResponse>>> findAll() {
        List<PenerimaBasicResponse> result = penerimaService.findAll().stream()
            .map(penerima -> penerima.toBasicResponse())
            .toList();
        
        BaseResponse<List<PenerimaBasicResponse>> response = BaseResponse.success(result);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PenerimaResponse>> findPenerimaById(@PathVariable String id) {
        PenerimaResponse result = penerimaService.penerimaToResponse(penerimaService.findPenerimaById(id));

        BaseResponse<PenerimaResponse> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<PenerimaResponse>> createPenerima(@Valid @RequestBody CreatePenerimaRequest request) {
        PenerimaResponse created = penerimaService.penerimaToResponse(penerimaService.createPenerima(request));

        BaseResponse<PenerimaResponse> response = BaseResponse.created(created);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<PenerimaResponse>> updatePenerima(@Valid @RequestBody UpdatePenerimaRequest request) {
        PenerimaResponse updated = penerimaService.penerimaToResponse(penerimaService.updatePenerima(request));

        BaseResponse<PenerimaResponse> response = BaseResponse.success(updated);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/verifikasi")
    public ResponseEntity<BaseResponse<PenerimaResponse>> verifyPenerima(@Valid @RequestBody VerifyPenerimaRequest request) {
        PenerimaResponse updated = penerimaService.penerimaToResponse(penerimaService.verifyPenerima(request));

        BaseResponse<PenerimaResponse> response = BaseResponse.success(updated);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/aktivasi")
    public ResponseEntity<BaseResponse<PenerimaResponse>> activatePenerima(@PathVariable String id) {
        PenerimaResponse updated = penerimaService.penerimaToResponse(penerimaService.activatePenerima(id));

        BaseResponse<PenerimaResponse> response = BaseResponse.success(updated);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/tolak")
    public ResponseEntity<BaseResponse<PenerimaResponse>> rejectPenerima(@Valid @RequestBody RejectPenerimaRequest request) {
        PenerimaResponse updated = penerimaService.penerimaToResponse(penerimaService.rejectPenerima(request));

        BaseResponse<PenerimaResponse> response = BaseResponse.success(updated);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/nonaktifkan")
    public ResponseEntity<BaseResponse<PenerimaResponse>> deactivatePenerima(@Valid @RequestBody DeactivatePenerimaRequest request) {
        PenerimaResponse updated = penerimaService.penerimaToResponse(penerimaService.deactivatePenerima(request));

        BaseResponse<PenerimaResponse> response = BaseResponse.success(updated);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/tinjau-ulang")
    public ResponseEntity<BaseResponse<PenerimaResponse>> reviewPenerima(@Valid @RequestBody ReviewPenerimaRequest request) {
        PenerimaResponse updated = penerimaService.penerimaToResponse(penerimaService.reviewPenerima(request));

        BaseResponse<PenerimaResponse> response = BaseResponse.success(updated);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistik")
    public ResponseEntity<BaseResponse<PenerimaStatistics>> getPenerimaStatistics(@RequestParam(defaultValue = "BULANAN") String period, @RequestParam(defaultValue = "2026") int year) {
        PenerimaStatistics statistics = penerimaStatisticsService.getStatistics(period, year);

        BaseResponse<PenerimaStatistics> response = BaseResponse.success(statistics);

        return ResponseEntity.ok(response);
    }
}
