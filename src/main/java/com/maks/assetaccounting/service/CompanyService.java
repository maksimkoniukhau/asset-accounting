package com.maks.assetaccounting.service;

import com.maks.assetaccounting.model.Company;

import java.util.List;

public interface CompanyService {
    Company create(Company asset);

    Company get(Company asset);

    Company update(Company asset);

    Company delete(Company asset);

    List<Company> getAll();

    List<Company> getCompaniesWithAssetsInAscendingOrder();
}
