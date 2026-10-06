package apap.ti._6.simbg_2406406300_be.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.CreateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.requests.rombel.UpdateRombelRequest;
import apap.ti._6.simbg_2406406300_be.dto.responses.rombel.RombelResponse;
import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.models.RombonganBelajar;
import apap.ti._6.simbg_2406406300_be.services.rombel.RombelService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/rombel")
public class RombelController {
    
    private RombelService rombelService;

    public RombelController(RombelService rombelService) {
        this.rombelService = rombelService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<RombelResponse>>> findAll() {
        List<RombelResponse> result = rombelService.findAll().stream()
            .map(rombel -> rombelService.rombelToResponse(rombel))
            .toList();

        BaseResponse<List<RombelResponse>> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<RombelResponse>> findRombelById(@PathVariable String id) {
        RombelResponse result = rombelService.rombelToResponse(rombelService.findRombelById(id));

        BaseResponse<RombelResponse> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<RombelResponse>> createRombel(@Valid @RequestBody CreateRombelRequest request) {
        RombonganBelajar resultRombel = rombelService.createRombel(request);
        RombelResponse resultDto = rombelService.rombelToResponse(resultRombel);

        BaseResponse<RombelResponse> response = BaseResponse.created(resultDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<BaseResponse<RombelResponse>> updateRombel(@Valid @RequestBody UpdateRombelRequest request) {
        RombonganBelajar updatedRombel = rombelService.updateRombel(request);
        RombelResponse updatedDto = rombelService.rombelToResponse(updatedRombel);

        BaseResponse<RombelResponse> response = BaseResponse.success(updatedDto);

        return ResponseEntity.ok(response);
    }
}
