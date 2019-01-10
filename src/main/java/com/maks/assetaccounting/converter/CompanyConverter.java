package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.entity.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyConverter implements DtoEntityConverter<CompanyDto, Company> {

    private final AssetConverter assetConverter;

    @Autowired
    public CompanyConverter(final AssetConverter assetConverter) {
        this.assetConverter = assetConverter;
    }

    @Override
    public CompanyDto convertToDto(final Company company) {
        if (company != null) {
            final CompanyDto companyDto = new CompanyDto();
            BeanUtils.copyProperties(company, companyDto);
            companyDto.setAssetDtos(assetConverter.convertListToDto(company.getAssets()));
            return companyDto;
        }
        return null;
    }

    @Override
    public Company convertToEntity(final CompanyDto companyDto) {
        if (companyDto != null) {
            final Company company = new Company();
            BeanUtils.copyProperties(companyDto, company);
            company.setAssets(assetConverter.convertListToEntity(companyDto.getAssetDtos()));
            return company;
        }
        return null;
    }
}
