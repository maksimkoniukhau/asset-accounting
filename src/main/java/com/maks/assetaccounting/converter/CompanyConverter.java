package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyConverter implements DtoEntityConverter<CompanyDto, Company> {

    private final AssetConverter assetConverter;
    private final UserRepository userRepository;

    @Autowired
    public CompanyConverter(final AssetConverter assetConverter, final UserRepository userRepository) {
        this.assetConverter = assetConverter;
        this.userRepository = userRepository;
    }

    @Override
    public CompanyDto convertToDto(final Company company) {
        if (company != null) {
            final CompanyDto companyDto = new CompanyDto();
            BeanUtils.copyProperties(company, companyDto);
            companyDto.setAssetDtos(assetConverter.convertListToDto(company.getAssets()));
            companyDto.setUsername(company.getUser().getUsername());
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
            company.setUser(userRepository.findByUsername(companyDto.getUsername()));
            return company;
        }
        return null;
    }
}
