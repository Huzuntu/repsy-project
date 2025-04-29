package io.repsy.repsy_api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.repsy.repsy_api.repository.PackageRepository;
import io.repsy.storage.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.repsy.repsy_api.entity.Package;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class PackageService {
    private final PackageRepository packageRepository;
    private final StorageService storageService;

    public PackageService(PackageRepository packageRepository, StorageService storageService) {
        this.packageRepository = packageRepository;
        this.storageService = storageService;
    }

    public void upload(String packageName, String version, MultipartFile repFile, MultipartFile metaFile) throws IOException {
        validateFiles(repFile, metaFile);
        validateMetaJson(metaFile);

        String repPath = packageName + "/" + version + "/" + repFile.getOriginalFilename();
        String metaPath = packageName + "/" + version + "/" + metaFile.getOriginalFilename();
        storageService.save(repPath, repFile.getInputStream());
        storageService.save(metaPath, metaFile.getInputStream());

        packageRepository.findByNameAndVersion(packageName, version)
                .orElseGet(() -> {
                    Package entity = new Package();
                    entity.setName(packageName);
                    entity.setVersion(version);
                    entity.setUploadedAt(LocalDateTime.now());
                    entity.setRepFilePath(repPath);
                    entity.setMetaFilePath(metaPath);
                    return packageRepository.save(entity);
                });
    }

    private void validateFiles(MultipartFile repFile, MultipartFile metaFile) throws IOException {
        if (!repFile.getOriginalFilename().endsWith(".rep")) {
            throw new IllegalArgumentException(".rep file is missing or wrong");
        }
        if (!"meta.json".equals(metaFile.getOriginalFilename())) {
            throw new IllegalArgumentException("meta.json file is missing or wrong");
        }
    }

    private void validateMetaJson(MultipartFile metaFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(metaFile.getInputStream());

        if (!rootNode.hasNonNull("name") || !rootNode.get("name").isTextual()) {
            throw new IllegalArgumentException("meta.json must have a non-null 'name' field of type string");
        }
        if (!rootNode.hasNonNull("version") || !rootNode.get("version").isTextual()) {
            throw new IllegalArgumentException("meta.json must have a non-null 'version' field of type string");
        }
        if (!rootNode.hasNonNull("author") || !rootNode.get("author").isTextual()) {
            throw new IllegalArgumentException("meta.json must have a non-null 'author' field of type string");
        }
        if (!rootNode.has("dependencies") || !rootNode.get("dependencies").isArray()) {
            throw new IllegalArgumentException("meta.json must have a 'dependencies' array");
        }
    }


    public byte[] download(String packageName, String version, String fileName) throws IOException {
        String path = packageName + "/" + version + "/" + fileName;
        return storageService.load(path).readAllBytes();
    }
}
