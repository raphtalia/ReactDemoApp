package reactdemoapp.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactdemoapp.backend.annotations.Subdomain;

@RestController
@Subdomain("cdn")
@CrossOrigin(origins = "*")
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Hello from test");
    }
}
