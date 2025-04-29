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

    @Value("${storage.filesystem.root-path:}")
    private String rootPath;

    @Value("${storage.object.endpoint:}")
    private String endpoint;

    @Value("${storage.object.access-key:}")
    private String accessKey;

    @Value("${storage.object.secret-key:}")
    private String secretKey;

    @Value("${storage.object.bucket-name:}")
    private String bucketName;

    @Bean
    public StorageService storageService() {
        if ("file-system".equalsIgnoreCase(strategy)) {
            return new FileSystemStorageService(rootPath);
        } else if ("object-storage".equalsIgnoreCase(strategy)) {
            return new ObjectStorageService(endpoint, accessKey, secretKey, bucketName);
        } else {
            throw new IllegalArgumentException("Unknown storage.strategy: " + strategy);
        }
    }
}
