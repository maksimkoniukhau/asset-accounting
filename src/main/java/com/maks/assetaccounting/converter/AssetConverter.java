package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetConverter implements DtoEntityConverter<AssetDto, Asset> {

    private final CompanyRepository companyRepository;

    @Autowired
    public AssetConverter(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public AssetDto convertToDto(final Asset asset) {
        final AssetDto assetDto = new AssetDto();
        BeanUtils.copyProperties(asset, assetDto);
        assetDto.setCompanyName(asset.getCompany().getName());
        return assetDto;
    }

    @Override
    public Asset convertToEntity(final AssetDto assetDto) {
        Asset asset = convertToEntityForCreate(assetDto);
        asset.setCreationDate(assetDto.getCreationDate());
        return asset;
    }

    public Asset convertToEntityForCreate(final AssetDto assetDto) {
        final Asset asset = new Asset();
        BeanUtils.copyProperties(assetDto, asset);
        asset.setCompany(companyRepository.findByName(assetDto.getCompanyName()));
        return asset;
    }
}
