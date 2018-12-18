package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}
