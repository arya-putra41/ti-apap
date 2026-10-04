package apap.ti._6.simbg_2406406300_be.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class HomeController {
    @GetMapping
    public ResponseEntity<String> showHomepage() {
        return ResponseEntity.ok("Hello World");
    }
}
