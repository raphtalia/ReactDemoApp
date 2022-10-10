package reactdemoapp.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactdemoapp.backend.annotations.Subdomain;

@RestController
@Subdomain("cdn")
@CrossOrigin(origins = "*")
public class CdnController {
    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hello from CDN 1");
    }

    @PostMapping ResponseEntity<String> post(@RequestBody byte[] body) {
        System.out.println("Received " + body.length + " bytes");
        return ResponseEntity.ok("Hello from CDN 2");
    }
}
