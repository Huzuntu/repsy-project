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
@RequestMapping("/")
public class PackageController {
    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }


    @PostMapping("/{packageName}/{version}")
    public ResponseEntity<String> deploy(@PathVariable String packageName, @PathVariable String version,
                                         @RequestParam("repFile") MultipartFile repFile, @RequestParam("metaFile") MultipartFile metaFile) throws IOException {
        packageService.upload(packageName, version, repFile, metaFile);
        return ResponseEntity.status(HttpStatus.OK).body("Package uploaded successfully");

    }

    @GetMapping("/{packageName}/{version}/{fileName}")
    public ResponseEntity<byte[]> download(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName) throws IOException {

        byte[] fileBytes = packageService.download(packageName, version, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileBytes);
    }
}
