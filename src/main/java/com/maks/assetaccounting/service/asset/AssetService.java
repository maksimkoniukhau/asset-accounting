package com.maks.assetaccounting.service.asset;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AssetService extends CrudService<AssetDto> {

    List<AssetDto> getAllByName(final String name);

    Page<AssetDto> getAllByCompanyName(final Optional<String> filter, final String companyName,
                                       final Pageable pageable);

    List<AssetDto> generation(final Long companyId);

    List<AssetDto> transition(final List<AssetDto> assetDtos, final Long toId);

    List<AssetDto> getMarketable();

    List<AssetDto> getExpensiveAndMarketable();

    int countByCompanyName(final Optional<String> filter, final String companyName);
}
