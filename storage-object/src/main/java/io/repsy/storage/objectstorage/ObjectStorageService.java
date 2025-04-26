package io.repsy.storage.objectstorage;

import io.repsy.storage.StorageService;

import java.io.InputStream;

public class ObjectStorageService implements StorageService {
    @Override
    public void save(String path, InputStream content) {

    }

    @Override
    public InputStream load(String path) {
        return null;
    }
}
