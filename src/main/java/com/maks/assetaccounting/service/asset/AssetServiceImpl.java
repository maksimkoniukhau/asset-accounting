package com.maks.assetaccounting.service.asset;

import com.maks.assetaccounting.converter.AssetConverter;
import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.entity.User;
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
import java.util.stream.Collectors;

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
    public AssetDto create(final AssetDto assetDto, final String username) {
        assetDto.setUsername(username);
        final Asset asset = assetRepository.save(assetConverter.convertToCreateEntity(assetDto));
        return assetConverter.convertToDto(asset);
    }

    @Override
    public AssetDto get(final Long id, final Long authUserId) {
        return assetConverter.convertToDto(assetRepository.findByUserIdAndId(authUserId, id));
    }

    @Override
    @Transactional
    public AssetDto update(final AssetDto assetDto, final Long id, final String username) {
        if (!assetDto.getUsername().equals(username)) {
            throw new IllegalArgumentException(assetDto + " isn't linked to " + username);
        }
        assureIdConsistent(assetDto, id);
        final Asset asset = assetRepository.save(assetConverter.convertToEntity(assetDto));
        return assetConverter.convertToDto(asset);
    }

    @Override
    @Transactional
    public AssetDto delete(final Long id, final Long authUserId) {
        final AssetDto assetDto = get(id, authUserId);
        assetRepository.deleteByUserIdAndId(authUserId, id);
        return assetDto;
    }

    @Override
    public List<AssetDto> getAll(final Long authUserId) {
        return assetConverter.convertListToDto(assetRepository.findAllByUserId(authUserId));
    }

    @Override
    @Transactional
    public void deleteAll(final List<AssetDto> userDtoList, final Long authUserId) {
        assetRepository.deleteAll(assetConverter.convertListToEntity(userDtoList)
                .stream()
                .filter(asset -> asset.getUser().getId().equals(authUserId))
                .collect(Collectors.toList()));
    }

    @Override
    public Page<AssetDto> getAllByCompanyName(final Optional<String> filter, final String companyName,
                                              final Pageable pageable, final Long authUserId) {
        final Company company = companyRepository.findByName(companyName);
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetConverter.convertPageToDto(assetRepository
                    .findByUserIdAndCompanyAndNameLikeIgnoreCase(authUserId,
                            checkNotFound(company, "name " + companyName),
                            repositoryFilter, pageable));
        } else {
            return assetConverter.convertPageToDto(assetRepository.findByUserIdAndCompany(authUserId, company, pageable));
        }
    }

    @Override
    public int countByCompanyName(final Optional<String> filter, final String companyName, final Long authUserId) {
        final Company company = companyRepository.findByName(companyName);
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetRepository.countByUserIdAndCompanyAndNameLikeIgnoreCase(authUserId,
                    checkNotFound(company, "name " + companyName), repositoryFilter);
        } else {
            return assetRepository.countByUserIdAndCompany(authUserId, company);
        }
    }

    @Override
    @Transactional
    public List<AssetDto> generation(final Long companyId, final User authUser) {
        final List<Asset> assets = new ArrayList<>();
        final Company toCompany = companyRepository.findById(companyId).orElse(null);
        checkNotFound(toCompany, "id " + companyId);
        for (int i = 1; i <= 100; i++) {
            final Asset asset = new Asset(String.format("Asset %d", i), ZonedDateTime.now(), i + 1000, toCompany);
            asset.setUser(authUser);
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
    public List<AssetDto> getMarketable(final Long authUserId) {
        return assetConverter.convertListToDto(assetRepository
                .findAllByUserIdOrderByNumberOfTransitionDesc(authUserId));
    }

    @Override
    public List<AssetDto> getExpensiveAndMarketable(final Long authUserId) {
        return assetConverter.convertListToDto(assetRepository
                .findAllByUserIdOrderByCostDescNumberOfTransitionDesc(authUserId));
    }

    @Override
    public Page<AssetDto> findAnyMatching(final Optional<String> filter, final Pageable pageable, final Long authUserId) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetConverter.convertPageToDto(assetRepository
                    .findByUserIdAndNameLikeIgnoreCase(authUserId, repositoryFilter, pageable));
        } else {
            return assetConverter.convertPageToDto(assetRepository.findByUserId(authUserId, pageable));
        }
    }

    @Override
    public long countAnyMatching(final Optional<String> filter, final Long authUserId) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return assetRepository.countByUserIdAndNameLikeIgnoreCase(authUserId, repositoryFilter);
        } else {
            return assetRepository.countByUserId(authUserId);
        }
    }
}
