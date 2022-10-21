package reactdemoapp.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactdemoapp.backend.annotations.Subdomain;
import reactdemoapp.backend.payload.response.NewContentResponse;
import reactdemoapp.backend.services.CdnService;

import java.io.IOException;

@RestController
@Subdomain("cdn")
@CrossOrigin(origins = "*")
public class CdnController {
    @Autowired
    CdnService cdnService;

    @GetMapping("/{contentId}")
    public ResponseEntity<byte[]> get(@PathVariable long contentId) throws IOException {
        return ResponseEntity.ok(cdnService.get(contentId).getContent());
    }

    @PostMapping
    public NewContentResponse post(@RequestBody byte[] content) throws IOException {
        return cdnService.save(content).toResponse();
    }
}
