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
    public CompanyDto create(final CompanyDto companyDto) {
        final Company company = companyRepository.save(companyConverter.convertToEntity(companyDto));
        return companyConverter.convertToDto(company);
    }

    @Override
    public CompanyDto get(final Long id) {
        return companyConverter.convertToDto(companyRepository.findById(id).orElse(null));
    }

    @Override
    @Transactional
    public CompanyDto update(final CompanyDto companyDto, final Long id) {
        assureIdConsistent(companyDto, id);
        return create(companyDto);
    }

    @Override
    @Transactional
    public CompanyDto delete(final Long id) {
        final CompanyDto companyDto = get(id);
        companyRepository.deleteById(id);
        return companyDto;
    }

    @Override
    public List<CompanyDto> getAll() {
        return companyConverter.convertListToDto(companyRepository.findAll());
    }

    @Override
    @Transactional
    public void deleteAll(final List<CompanyDto> userDtoList) {
        companyRepository.deleteAll(companyConverter.convertListToEntity(userDtoList));
    }

    @Override
    public CompanyDto getByName(final String name) {
        return companyConverter.convertToDto(companyRepository.findByName(name));
    }

    @Override
    public Page<CompanyDto> findWithTheMostAssets(final Optional<String> filter, final Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return companyConverter.convertPageToDto(companyRepository
                    .findWithTheMostAssetsAndNameLikeIgnoreCase(repositoryFilter, pageable));
        } else {
            return companyConverter.convertPageToDto(companyRepository.findWithTheMostAssets(pageable));
        }
    }

    @Override
    public Page<CompanyDto> findAnyMatching(final Optional<String> filter, final Pageable pageable) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return companyConverter.convertPageToDto(companyRepository
                    .findByNameLikeIgnoreCase(repositoryFilter, pageable));
        } else {
            return companyConverter.convertPageToDto(companyRepository.findBy(pageable));
        }
    }

    @Override
    public long countAnyMatching(final Optional<String> filter) {
        if (filter.isPresent()) {
            String repositoryFilter = "%" + filter.get() + "%";
            return companyRepository.countByNameLikeIgnoreCase(repositoryFilter);
        } else {
            return companyRepository.count();
        }
    }
}
