package apap.ti._6.simbg_2406406300_be.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.CreateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.UpdateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.sekolah.SekolahBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.sekolah.SekolahResponse;
import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.services.sekolah.SekolahService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sekolah")
public class SekolahController {

    private SekolahService sekolahService;

    public SekolahController(SekolahService sekolahService) {
        this.sekolahService = sekolahService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<SekolahBasicResponse>>> findAllSekolah() {
        List<SekolahBasicResponse> result = sekolahService.findAll().stream()
                .map(s -> s.toBasicResponse())
                .toList();

        BaseResponse<List<SekolahBasicResponse>> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<SekolahResponse>> findSekolahById(@PathVariable String id) {
        Sekolah foundSekolah = sekolahService.findSekolahById(id);
        SekolahResponse result = sekolahService.sekolahToResponse(foundSekolah);

        BaseResponse<SekolahResponse> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<SekolahResponse>> createSekolah(
            @Valid @RequestBody CreateSekolahRequest request) {
        Sekolah createdSekolah = sekolahService.createSekolah(request);
        SekolahResponse result = sekolahService.sekolahToResponse(createdSekolah);

        BaseResponse<SekolahResponse> response = BaseResponse.created(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<SekolahResponse>> updateSekolah(
            @Valid @RequestBody UpdateSekolahRequest request) {
        Sekolah updatedSekolah = sekolahService.updateSekolah(request);
        SekolahResponse result = sekolahService.sekolahToResponse(updatedSekolah);

        BaseResponse<SekolahResponse> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/nonaktifkan")
    public ResponseEntity<BaseResponse<SekolahResponse>> deactivateSekolah(@PathVariable String id) {
        Sekolah deactivatedSekolah = sekolahService.deactivateSekolah(id);
        SekolahResponse result = sekolahService.sekolahToResponse(deactivatedSekolah);

        BaseResponse<SekolahResponse> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }
}
