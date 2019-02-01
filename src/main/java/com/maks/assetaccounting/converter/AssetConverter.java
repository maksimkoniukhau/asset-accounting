package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.repository.AssetRepository;
import com.maks.assetaccounting.repository.CompanyRepository;
import com.maks.assetaccounting.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetConverter implements DtoEntityConverter<AssetDto, Asset> {

    private final AssetRepository assetRepository;

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Autowired
    public AssetConverter(final AssetRepository assetRepository, final CompanyRepository companyRepository, final UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AssetDto convertToDto(final Asset asset) {
        if (asset != null) {
            final AssetDto assetDto = new AssetDto();
            BeanUtils.copyProperties(asset, assetDto);
            assetDto.setCompanyName(asset.getCompany().getName());
            assetDto.setUsername(asset.getUser().getUsername());
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
            asset.setUser(userRepository.findByUsername(assetDto.getUsername()));
            return asset;
        }
        return null;
    }

    public Asset convertToCreateEntity(final AssetDto assetDto) {
        if (assetDto != null) {
            final Asset asset = new Asset();
            BeanUtils.copyProperties(assetDto, asset, "creationDate");
            asset.setCompany(companyRepository.findByName(assetDto.getCompanyName()));
            asset.setUser(userRepository.findByUsername(assetDto.getUsername()));
            return asset;
        }
        return null;
    }
}
