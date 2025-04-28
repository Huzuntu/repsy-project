package io.repsy.repsy_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.repsy.repsy_api.entity.Package;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {
    Optional<Package> findByNameAndVersion(String name, String version);
}

