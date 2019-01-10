package com.maks.assetaccounting.service.asset;

import com.maks.assetaccounting.converter.AssetConverter;
import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.AssetRepository;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.maks.assetaccounting.util.ValidationUtil.assureIdConsistent;
import static com.maks.assetaccounting.util.ValidationUtil.checkNotFound;

@Service
@Transactional(readOnly = true)
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    private final CompanyRepository companyRepository;

    private final AssetConverter assetConverter;

    @Autowired
    public AssetServiceImpl(final AssetRepository assetRepository, final CompanyRepository companyRepository
            , final AssetConverter assetConverter) {
        this.assetRepository = assetRepository;
        this.companyRepository = companyRepository;
        this.assetConverter = assetConverter;
    }

    @Override
    @Transactional
    public AssetDto create(final AssetDto assetDto) {
        final Asset asset = assetRepository.save(assetConverter.convertToEntityForCreate(assetDto));
        return assetConverter.convertToDto(asset);
    }

    @Override
    public AssetDto get(final Long id) {
        return assetConverter.convertToDto(assetRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public AssetDto update(final AssetDto assetDto, final Long id) {
        assureIdConsistent(assetDto, id);
        final Asset asset = assetRepository.save(assetConverter.convertToEntity(assetDto));
        return assetConverter.convertToDto(asset);
    }

    @Override
    @Transactional
    public AssetDto delete(final Long id) {
        final AssetDto assetDto = get(id);
        assetRepository.deleteById(id);
        return assetDto;
    }

    @Override
    public List<AssetDto> getAll() {
        return assetConverter.convertListToDto(assetRepository.findAll());
    }

    @Override
    public List<AssetDto> getAllByName(final String name) {
        return assetConverter.convertListToDto(assetRepository.findAllByName(name));
    }

    @Override
    public List<AssetDto> getAllByCompanyName(final String companyName) {
        final Company company = companyRepository.findByName(companyName);
        return assetConverter.convertListToDto(assetRepository
                .findAllByCompany(checkNotFound(company, "name " + companyName)));
    }

    @Override
    @Transactional
    public List<AssetDto> generation(final Long companyId) {
        final List<Asset> assets = new ArrayList<>();
        final Company toCompany = companyRepository.findById(companyId).orElse(null);
        checkNotFound(toCompany, "id " + companyId);
        for (int i = 1; i <= 100; i++) {
            final Asset asset = new Asset(String.format("Asset %d", i), ZonedDateTime.now(), i + 1000, toCompany);
            assets.add(asset);
        }
        return assetConverter.convertListToDto(assetRepository.saveAll(assets));
    }

    @Override
    @Transactional
    public List<AssetDto> transition(final List<AssetDto> assetDtos, final Long toId) {
        final Company toCompany = companyRepository.findById(toId).orElse(null);
        checkNotFound(toCompany, "id " + toId);
        final List<Asset> assets = assetConverter.convertListToEntity(assetDtos);
        if (assets != null) {
            assets.forEach(asset -> {
                asset.setCompany(toCompany);
                asset.setTransferDate(ZonedDateTime.now());
                asset.setNumberOfTransition(asset.getNumberOfTransition() + 1);
            });
            return assetConverter.convertListToDto(assetRepository.saveAll(assets));
        }
        return null;
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
