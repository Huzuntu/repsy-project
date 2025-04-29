package io.repsy.storage.objectstorage;

import io.minio.*;
import io.repsy.storage.StorageService;
import jakarta.annotation.PostConstruct;

import java.io.InputStream;

public class ObjectStorageService implements StorageService {
    private final MinioClient minioClient;
    private final String bucketName;

    public ObjectStorageService(String endpoint, String accessKey,String secretKey, String bucketName) {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        this.bucketName = bucketName;
    }

    @PostConstruct
    public void initialize() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                System.out.println("Bucket created: " + bucketName);
            } else {
                System.out.println("Bucket already exists: " + bucketName);
            }
        } catch (Exception e) {
            System.err.println("Could not initialize bucket: " + e.getMessage());
            throw new RuntimeException("Failed to initialize MinIO bucket", e);
        }
    }

    @Override
    public void save(String path, InputStream content) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(content, -1, 10 * 1024 * 1024)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("ObjectStorageService.save failed for path: " + path, e);
        }
    }

    @Override
    public InputStream load(String path) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("ObjectStorageService.load failed for path: " + path, e);
        }
    }
}
