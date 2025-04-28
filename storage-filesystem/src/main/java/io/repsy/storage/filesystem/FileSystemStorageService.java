package io.repsy.storage.filesystem;

import io.repsy.storage.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class FileSystemStorageService implements StorageService {
    private final Path rootPath;

    public FileSystemStorageService(String rootPath) {
        this.rootPath = Paths.get(rootPath);
    }

    @Override
    public void save(String path, InputStream content) {
        try {
            Path fullPath = rootPath.resolve(path);
            Files.createDirectories(fullPath.getParent());
            Files.copy(content, fullPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("FileSystemStorageService.save failed for path: " + path, e);
        }
    }

    @Override
    public InputStream load(String path) {
        try {
            Path fullPath = rootPath.resolve(path);
            if (!Files.exists(fullPath)) {
                throw new RuntimeException("File not found on filesystem: " + fullPath);
            }
            return Files.newInputStream(fullPath, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException("FileSystemStorageService.load failed for path: " + path, e);
        }
    }
}
