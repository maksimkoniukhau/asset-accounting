package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByName(final String name);

    Page<Company> findBy(final Pageable page);

    Page<Company> findByNameLikeIgnoreCase(final String name, final Pageable page);

    @Query(value = "SELECT * FROM company c ORDER BY (select count(a.company_id) from asset a where a.company_id = c.id) DESC", nativeQuery = true)
    Page<Company> findWithTheMostAssets(final Pageable page);

    @Query(value = "SELECT * FROM company c WHERE UPPER(c.name) LIKE UPPER(?1) ORDER BY (select count(a.company_id) from asset a where a.company_id = c.id) DESC", nativeQuery = true)
    Page<Company> findWithTheMostAssetsAndNameLikeIgnoreCase(final String name, final Pageable page);

    int countByNameLikeIgnoreCase(final String name);
}
