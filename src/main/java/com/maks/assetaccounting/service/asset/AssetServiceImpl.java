package com.maks.assetaccounting.service.asset;

import com.maks.assetaccounting.converter.AssetConverter;
import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.AssetRepository;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public void deleteAll(final List<AssetDto> userDtoList) {
        assetRepository.deleteAll(assetConverter.convertListToEntity(userDtoList));
    }

    @Override
    public List<AssetDto> getAllByName(final String name) {
        return assetConverter.convertListToDto(assetRepository.findAllByName(name));
    }

    @Override
    public Page<AssetDto> getAllByCompanyName(final Optional<String> filter, final String companyName,
                                              final Pageable pageable) {
        final Company company = companyRepository.findByName(companyName);
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetConverter.convertPageToDto(assetRepository
                    .findByCompanyAndNameLikeIgnoreCase(checkNotFound(company, "name " + companyName),
                            repositoryFilter, pageable));
        } else {
            return assetConverter.convertPageToDto(assetRepository.findByCompany(company, pageable));
        }
    }

    @Override
    public int countByCompanyName(final Optional<String> filter, final String companyName) {
        final Company company = companyRepository.findByName(companyName);
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetRepository.countByCompanyAndNameLikeIgnoreCase(
                    checkNotFound(company, "name " + companyName), repositoryFilter);
        } else {
            return assetRepository.countByCompany(company);
        }
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

    @Override
    public Page<AssetDto> findAnyMatching(final Optional<String> filter, final Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetConverter.convertPageToDto(assetRepository.findByNameLikeIgnoreCase(repositoryFilter, pageable));
        } else {
            return assetConverter.convertPageToDto(assetRepository.findBy(pageable));
        }
    }

    @Override
    public long countAnyMatching(final Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetRepository.countByNameLikeIgnoreCase(repositoryFilter);
        } else {
            return assetRepository.count();
        }
    }
}
