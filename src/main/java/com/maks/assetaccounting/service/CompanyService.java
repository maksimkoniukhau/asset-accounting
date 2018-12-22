package com.maks.assetaccounting.service;

import com.maks.assetaccounting.model.Company;

import java.util.List;

public interface CompanyService {

    List<Company> getCompaniesWithAssetsInAscendingOrder();
}
