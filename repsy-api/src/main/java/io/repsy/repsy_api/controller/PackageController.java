package io.repsy.repsy_api.controller;

import io.repsy.repsy_api.service.PackageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/")
public class PackageController {
    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }


    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<String> deploy(@PathVariable String packageName, @PathVariable String version, @RequestParam("file") MultipartFile file
    ) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename == null ||
                (!filename.endsWith(".rep") && !filename.endsWith(".json"))) {
            return ResponseEntity.badRequest()
                    .body("Only .rep and .json files are accepted");
        }
        packageService.upload(packageName, version, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Uploaded " + filename + " successfully");
    }

    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<ByteArrayResource> download(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName
    ) throws IOException {
        byte[] data = packageService.download(packageName, version, fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .contentLength(data.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
