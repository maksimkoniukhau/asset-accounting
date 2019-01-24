package com.maks.assetaccounting.service.company;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyService extends CrudService<CompanyDto, CompanyDto> {

    CompanyDto getByName(final String name);

    Page<CompanyDto> findWithTheMostAssets(final Optional<String> filter, final Pageable pageable);
}
