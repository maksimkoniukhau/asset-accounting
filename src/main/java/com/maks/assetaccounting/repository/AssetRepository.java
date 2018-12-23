package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findFirst50ByOrderByNumberOfTransitionDesc();

    List<Asset> findFirst50ByOrderByCostDescNumberOfTransitionDesc();
}
