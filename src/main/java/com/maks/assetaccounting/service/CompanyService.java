package com.maks.assetaccounting.service;

import com.maks.assetaccounting.dto.CompanyDto;

import java.util.List;

public interface CompanyService extends CrudService<CompanyDto, CompanyDto> {

    List<CompanyDto> findCompaniesWithAssetsInAscendingOrder();
}
