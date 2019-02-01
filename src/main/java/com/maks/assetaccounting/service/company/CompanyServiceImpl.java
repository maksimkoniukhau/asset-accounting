package com.maks.assetaccounting.service.company;

import com.maks.assetaccounting.converter.CompanyConverter;
import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.maks.assetaccounting.util.ValidationUtil.assureIdConsistent;

@Service
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    private final CompanyConverter companyConverter;

    @Autowired
    public CompanyServiceImpl(final CompanyRepository companyRepository, final CompanyConverter companyConverter) {
        this.companyRepository = companyRepository;
        this.companyConverter = companyConverter;
    }

    @Override
    @Transactional
    public CompanyDto create(final CompanyDto companyDto, final String username) {
        companyDto.setUsername(username);
        final Company company = companyRepository.save(companyConverter.convertToEntity(companyDto));
        return companyConverter.convertToDto(company);
    }

    @Override
    public CompanyDto get(final Long id, final Long authUserId) {
        return companyConverter.convertToDto(companyRepository.findByUserIdAndId(authUserId, id));
    }

    @Override
    @Transactional
    public CompanyDto update(final CompanyDto companyDto, final Long id, final String username) {
        if (!companyDto.getUsername().equals(username)) {
            throw new IllegalArgumentException(companyDto + " isn't linked to " + username);
        }
        assureIdConsistent(companyDto, id);
        return create(companyDto, username);
    }

    @Override
    @Transactional
    public CompanyDto delete(final Long id, final Long authUserId) {
        final CompanyDto companyDto = get(id, authUserId);
        companyRepository.deleteByUserIdAndId(authUserId, id);
        return companyDto;
    }

    @Override
    public List<CompanyDto> getAll(final Long authUserId) {
        return companyConverter.convertListToDto(companyRepository.findAllByUserId(authUserId));
    }

    @Override
    @Transactional
    public void deleteAll(final List<CompanyDto> companyDtoList, final Long authUserId) {
        companyRepository.deleteAll(companyConverter.convertListToEntity(companyDtoList)
                .stream()
                .filter(company -> company.getUser().getId().equals(authUserId))
                .collect(Collectors.toList()));
    }

    @Override
    public CompanyDto getByName(final String name) {
        return companyConverter.convertToDto(companyRepository.findByName(name));
    }

    @Override
    public Page<CompanyDto> findWithTheMostAssets(final Optional<String> filter, final Pageable pageable, final Long authUserId) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return companyConverter.convertPageToDto(companyRepository
                    .findWithTheMostAssetsAndNameLikeIgnoreCase(authUserId, repositoryFilter, pageable));
        } else {
            return companyConverter.convertPageToDto(companyRepository.findWithTheMostAssets(authUserId, pageable));
        }
    }

    @Override
    public Page<CompanyDto> findAnyMatching(final Optional<String> filter, final Pageable pageable, final Long authUserId) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return companyConverter.convertPageToDto(companyRepository
                    .findByUserIdAndNameLikeIgnoreCase(authUserId, repositoryFilter, pageable));
        } else {
            return companyConverter.convertPageToDto(companyRepository.findByUserId(authUserId, pageable));
        }
    }

    @Override
    public long countAnyMatching(final Optional<String> filter, final Long authUserId) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return companyRepository.countByUserIdAndNameLikeIgnoreCase(authUserId, repositoryFilter);
        } else {
            return companyRepository.countByUserId(authUserId);
        }
    }
}
