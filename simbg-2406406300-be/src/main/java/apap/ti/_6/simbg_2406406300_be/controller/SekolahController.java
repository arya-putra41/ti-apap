package apap.ti._6.simbg_2406406300_be.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.CreateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.sekolah.UpdateSekolahRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.SekolahBasicResponse;
import apap.ti._6.simbg_2406406300_be.dto.responses.SekolahResponse;
import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.models.Sekolah;
import apap.ti._6.simbg_2406406300_be.services.SekolahService;
import jakarta.validation.Valid;

@Controller
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
        SekolahResponse result = sekolahService.findSekolahById(id).toResponse();

        BaseResponse<SekolahResponse> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<SekolahResponse>> createSekolah(@Valid @RequestBody CreateSekolahRequest request) {
        Sekolah resultSchool = sekolahService.createSekolah(request);
        
        BaseResponse<SekolahResponse> response = BaseResponse.created(resultSchool.toResponse());

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<SekolahResponse>> updateSekolah(@Valid @RequestBody UpdateSekolahRequest request) {
        Sekolah updatedSchool = sekolahService.updateSekolah(request);

        BaseResponse<SekolahResponse> response = BaseResponse.success(updatedSchool.toResponse());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/nonaktifkan")
    public ResponseEntity<BaseResponse<SekolahResponse>> deactivateSekolah(@PathVariable String id) {
        Sekolah deactivatedSchool = sekolahService.deactivateSekolah(id);

        BaseResponse<SekolahResponse> response = BaseResponse.success(deactivatedSchool.toResponse());

        return ResponseEntity.ok(response);
    }
}
