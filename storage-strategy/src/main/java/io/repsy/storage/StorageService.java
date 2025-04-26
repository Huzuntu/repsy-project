package io.repsy.storage;

import java.io.InputStream;

public interface StorageService {
    void save(String path, InputStream content);
    InputStream load(String path);
}