package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Asset findByUserIdAndId(final Long authUserId, final Long id);

    void deleteByUserIdAndId(final Long authUserId, final Long id);

    List<Asset> findAllByUserId(final Long authUserId);

    Page<Asset> findByUserIdAndCompanyAndNameLikeIgnoreCase(final Long authUserId, final Company company,
                                                            final String name, final Pageable pageable);

    Page<Asset> findByUserIdAndCompany(final Long authUserId, final Company company, final Pageable pageable);

    List<Asset> findAllByUserIdOrderByNumberOfTransitionDesc(final Long authUserId);

    List<Asset> findAllByUserIdOrderByCostDescNumberOfTransitionDesc(final Long authUserId);

    Page<Asset> findByUserId(final Long authUserId, final Pageable page);

    Page<Asset> findByUserIdAndNameLikeIgnoreCase(final Long authUserId, final String name, final Pageable pageable);

    int countByUserIdAndNameLikeIgnoreCase(final Long authUserId, final String name);

    int countByUserIdAndCompanyAndNameLikeIgnoreCase(final Long authUserId, final Company company, final String name);

    int countByUserIdAndCompany(final Long authUserId, final Company company);

    int countByUserId(final Long authUserId);
}
