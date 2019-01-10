package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.repository.AssetRepository;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetConverter implements DtoEntityConverter<AssetDto, Asset> {

    private final AssetRepository assetRepository;

    private final CompanyRepository companyRepository;

    @Autowired
    public AssetConverter(final AssetRepository assetRepository, final CompanyRepository companyRepository) {
        this.assetRepository = assetRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public AssetDto convertToDto(final Asset asset) {
        if (asset != null) {
            final AssetDto assetDto = new AssetDto();
            BeanUtils.copyProperties(asset, assetDto);
            assetDto.setCompanyName(asset.getCompany().getName());
            return assetDto;
        }
        return null;
    }

    @Override
    public Asset convertToEntity(final AssetDto assetDto) {
        if (assetDto != null) {
            final Asset asset = new Asset();
            BeanUtils.copyProperties(assetDto, asset);
            asset.setCompany(companyRepository.findByName(assetDto.getCompanyName()));
            asset.setNumberOfTransition(assetRepository.findById(assetDto.getId())
                    .orElse(new Asset()).getNumberOfTransition());
            return asset;
        }
        return null;
    }

    public Asset convertToEntityForCreate(final AssetDto assetDto) {
        if (assetDto != null) {
            final Asset asset = new Asset();
            BeanUtils.copyProperties(assetDto, asset, "creationDate");
            asset.setCompany(companyRepository.findByName(assetDto.getCompanyName()));
            return asset;
        }
        return null;
    }
}
