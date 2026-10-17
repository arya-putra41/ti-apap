package apap.ti._6.simbg_2406406300_be.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti._6.simbg_2406406300_be.dto.responses.home.HomeResponse;
import apap.ti._6.simbg_2406406300_be.httpresponses.BaseResponse;
import apap.ti._6.simbg_2406406300_be.services.home.HomeService;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<HomeResponse>> showHomepage() {
        HomeResponse result = homeService.buildResponse();
        BaseResponse<HomeResponse> response = BaseResponse.success(result);

        return ResponseEntity.ok(response);
    }
}
