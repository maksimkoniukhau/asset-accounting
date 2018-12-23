package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findByName(String  name);
}
