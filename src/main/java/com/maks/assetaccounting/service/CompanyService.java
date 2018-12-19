package com.maks.assetaccounting.service;

import com.maks.assetaccounting.model.Company;

import java.util.List;

public interface CompanyService {
    Company create(Company company);

    Company get(Company company);

    Company update(Company company);

    Company delete(Company company);

    List<Company> getAll();

    List<Company> getCompaniesWithAssetsInAscendingOrder();
}
