package io.repsy.storage.filesystem;

import io.repsy.storage.StorageService;

import java.io.InputStream;

public class FileSystemStorageService implements StorageService {
    @Override
    public void save(String path, InputStream content) {

    }

    @Override
    public InputStream load(String path) {
        return null;
    }
}
