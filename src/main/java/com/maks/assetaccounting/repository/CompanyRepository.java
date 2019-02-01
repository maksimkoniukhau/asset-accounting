package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByUserIdAndId(final Long authUserId, final Long id);

    void deleteByUserIdAndId(final Long authUserId, final Long id);

    List<Company> findAllByUserId(final Long authUserId);

    Company findByName(final String name);

    Page<Company> findByUserId(final Long authUserId, final Pageable page);

    Page<Company> findByUserIdAndNameLikeIgnoreCase(final Long authUserId, final String name, final Pageable page);

    @Query(value = "SELECT * FROM company c WHERE c.user_id = (?1) " +
            "ORDER BY (select count(a.company_id) from asset a where a.company_id = c.id) DESC", nativeQuery = true)
    Page<Company> findWithTheMostAssets(final Long authUserId, final Pageable page);

    @Query(value = "SELECT * FROM company c WHERE c.user_id = (?1) AND UPPER(c.name) LIKE UPPER(?2) " +
            "ORDER BY (select count(a.company_id) from asset a where a.company_id = c.id) DESC", nativeQuery = true)
    Page<Company> findWithTheMostAssetsAndNameLikeIgnoreCase(final Long authUserId, final String name, final Pageable page);

    int countByUserIdAndNameLikeIgnoreCase(final Long authUserId, final String name);

    int countByUserId(final Long authUserId);
}
