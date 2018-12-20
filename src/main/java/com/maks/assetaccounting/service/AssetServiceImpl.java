package com.maks.assetaccounting.service;

import com.maks.assetaccounting.model.Asset;
import com.maks.assetaccounting.model.Company;
import com.maks.assetaccounting.repository.AssetRepository;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    private final CompanyRepository companyRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, CompanyRepository companyRepository) {
        this.assetRepository = assetRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Asset create(Asset asset) {
        return assetRepository.save(asset);
    }

    @Override
    public Asset get(Asset asset) {
        return asset;
    }

    @Override
    public Asset update(Asset asset) {
        return assetRepository.save(asset);
    }

    @Override
    public Asset delete(Asset asset) {
        assetRepository.delete(asset);
        return asset;
    }

    @Override
    public List<Asset> getAll() {
        return assetRepository.findAll();
    }

    @Override
    public List<Asset> generation(Long companyId) {
        List<Asset> assets = new ArrayList<>();
        Company toCompany = companyRepository.findById(companyId).orElse(null);
        for (int i = 1; i <= 100; i++) {
            Asset asset = new Asset(String.format("Asset number %d", i), new Date(), i + 1000, toCompany);
            assets.add(asset);
        }
        return assetRepository.saveAll(assets);
    }

    @Override
    public List<Asset> transition(List<Asset> assets, Long toId) {
        Company toCompany = companyRepository.findById(toId).orElse(null);
        assets.forEach(asset -> {
            asset.setCompany(toCompany);
            asset.setTransferDate(new Date());
            asset.setNumberOfTransition(asset.getNumberOfTransition() + 1);
        });
        return assetRepository.saveAll(assets);
    }

    @Override
    public List<Asset> getMarketable() {
        return assetRepository.findFirst50ByOrderByNumberOfTransitionDesc();
    }

    @Override
    public List<Asset> getExpensiveAndMarketable() {
        return assetRepository.findFirst50ByOrderByCostDescNumberOfTransitionDesc();
    }
}
