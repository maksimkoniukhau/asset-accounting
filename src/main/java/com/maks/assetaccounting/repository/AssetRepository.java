package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Asset findFirstByName(final String name);

    List<Asset> findAllByName(final String name);

    List<Asset> findAllByCompany(final Company company);

    List<Asset> findFirst50ByOrderByNumberOfTransitionDesc();

    List<Asset> findFirst50ByOrderByCostDescNumberOfTransitionDesc();
}
