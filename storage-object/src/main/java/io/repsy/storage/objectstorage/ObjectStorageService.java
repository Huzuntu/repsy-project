package io.repsy.storage.objectstorage;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.repsy.storage.StorageService;

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

    @Override
    public void save(String path, InputStream content) {
        try {
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
