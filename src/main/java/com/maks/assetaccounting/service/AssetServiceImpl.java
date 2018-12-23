package com.maks.assetaccounting.service;

import com.maks.assetaccounting.converter.AssetConverter;
import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.AssetRepository;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.maks.assetaccounting.util.ValidationUtil.assureIdConsistent;

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    private final CompanyRepository companyRepository;

    private final AssetConverter assetConverter;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, CompanyRepository companyRepository, AssetConverter assetConverter) {
        this.assetRepository = assetRepository;
        this.companyRepository = companyRepository;
        this.assetConverter = assetConverter;
    }

    @Override
    public AssetDto create(AssetDto assetDto) {
        Asset asset = assetRepository.save(assetConverter.convertToEntityForCreate(assetDto));
        return assetConverter.convertToDto(asset);
    }

    @Override
    public AssetDto get(Long id) {
        return assetConverter.convertToDto(assetRepository.findById(id).orElse(null));
    }

    @Override
    public AssetDto update(AssetDto assetDto, Long id) {
        assureIdConsistent(assetDto, id);
        Asset asset = assetRepository.save(assetConverter.convertToEntity(assetDto));
        return assetConverter.convertToDto(asset);
    }

    @Override
    public AssetDto delete(Long id) {
        AssetDto assetDto = get(id);
        assetRepository.deleteById(id);
        return assetDto;
    }

    @Override
    public List<AssetDto> getAll() {
        return assetConverter.convertListToDto(assetRepository.findAll());
    }

    @Override
    public List<AssetDto> generation(Long companyId) {
        List<Asset> assets = new ArrayList<>();
        Company toCompany = companyRepository.findById(companyId).orElse(null);
        for (int i = 1; i <= 100; i++) {
            Asset asset = new Asset(String.format("Asset number %d", i), new Date(), i + 1000, toCompany);
            assets.add(asset);
        }
        return assetConverter.convertListToDto(assetRepository.saveAll(assets));
    }

    @Override
    public List<AssetDto> transition(List<AssetDto> assetDtos, Long toId) {
        Company toCompany = companyRepository.findById(toId).orElse(null);
        List<Asset> assets = assetConverter.convertListToEntity(assetDtos);
        assets.forEach(asset -> {
            asset.setCompany(toCompany);
            asset.setTransferDate(new Date());
            asset.setNumberOfTransition(asset.getNumberOfTransition() + 1);
        });
        return assetConverter.convertListToDto(assetRepository.saveAll(assets));
    }

    @Override
    public List<AssetDto> getMarketable() {
        return assetConverter.convertListToDto(assetRepository.findFirst50ByOrderByNumberOfTransitionDesc());
    }

    @Override
    public List<AssetDto> getExpensiveAndMarketable() {
        return assetConverter.convertListToDto(assetRepository.findFirst50ByOrderByCostDescNumberOfTransitionDesc());
    }
}
