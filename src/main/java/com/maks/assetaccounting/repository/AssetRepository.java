package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Asset findFirstByName(final String name);

    List<Asset> findAllByName(final String name);

    Page<Asset> findByCompanyAndNameLikeIgnoreCase(final Company company, final String name, final Pageable pageable);

    Page<Asset> findByCompany(final Company company, final Pageable pageable);

    List<Asset> findFirst50ByOrderByNumberOfTransitionDesc();

    List<Asset> findFirst50ByOrderByCostDescNumberOfTransitionDesc();

    Page<Asset> findBy(final Pageable page);

    Page<Asset> findByNameLikeIgnoreCase(final String name, final Pageable pageable);

    int countByNameLikeIgnoreCase(final String name);

    int countByCompanyAndNameLikeIgnoreCase(final Company company, final String name);

    int countByCompany(final Company company);
}
