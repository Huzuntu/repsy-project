package io.repsy.repsy_api.service;

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

    public void upload(String packageName, String version, MultipartFile file) throws IOException {
        String path = packageName + "/" + version + "/" + file.getOriginalFilename();
        storageService.save(path, file.getInputStream());

        packageRepository.findByNameAndVersion(packageName, version)
                .orElseGet(() -> {
                    Package entity = new Package();
                    entity.setName(packageName);
                    entity.setVersion(version);
                    entity.setUploadedAt(LocalDateTime.now());
                    return packageRepository.save(entity);
                });
    }

    public byte[] download(String packageName,
                           String version,
                           String fileName) throws IOException {
        String path = packageName + "/" + version + "/" + fileName;
        return storageService.load(path).readAllBytes();
    }
}
