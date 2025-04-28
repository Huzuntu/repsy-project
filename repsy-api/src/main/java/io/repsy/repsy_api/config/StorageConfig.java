package io.repsy.repsy_api.config;

import io.repsy.storage.StorageService;
import io.repsy.storage.filesystem.FileSystemStorageService;
import io.repsy.storage.objectstorage.ObjectStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Value("${storage.strategy}")
    private String strategy;

    @Value("${storage.root-path:}")
    private String rootPath;

    @Value("${storage.endpoint:}")
    private String endpoint;

    @Value("${storage.access-key:}")
    private String accessKey;

    @Value("${storage.secret-key:}")
    private String secretKey;

    @Value("${storage.bucket-name:}")
    private String bucketName;

    @Bean
    public StorageService storageService() {
        switch (strategy.toLowerCase()) {
            case "file-system":
                return new FileSystemStorageService(rootPath);
            case "object-storage":
                return new ObjectStorageService(endpoint, accessKey, secretKey, bucketName);
            default:
                throw new IllegalArgumentException("Unknown storage.strategy: " + strategy);
        }
    }
}
