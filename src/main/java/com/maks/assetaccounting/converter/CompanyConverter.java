package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyConverter implements DtoEntityConverter<CompanyDto, Company> {

    private final AssetConverter assetConverter;

    @Autowired
    public CompanyConverter(AssetConverter assetConverter) {
        this.assetConverter = assetConverter;
    }

    @Override
    public CompanyDto convertToDto(final Company company) {
        final CompanyDto companyDto = new CompanyDto();
        BeanUtils.copyProperties(company, companyDto);
        final List<Asset> assets = company.getAssets();
        if (assets != null && !assets.isEmpty()) {
            final List<AssetDto> assetDtos = assets.stream()
                    .map(assetConverter::convertToDto)
                    .collect(Collectors.toList());
            companyDto.setAssetDtos(assetDtos);
        }
        return companyDto;
    }

    @Override
    public Company convertToEntity(final CompanyDto companyDto) {
        final Company company = new Company();
        BeanUtils.copyProperties(companyDto, company);
        final List<AssetDto> assetDtos = companyDto.getAssetDtos();
        if (assetDtos != null && !assetDtos.isEmpty()) {
            final List<Asset> assets = assetDtos.stream()
                    .map(assetConverter::convertToEntity)
                    .collect(Collectors.toList());
            company.setAssets(assets);
        }
        return company;
    }
}
