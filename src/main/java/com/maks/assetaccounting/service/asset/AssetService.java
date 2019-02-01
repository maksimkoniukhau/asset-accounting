package com.maks.assetaccounting.service.asset;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AssetService extends CrudService<AssetDto> {

    Page<AssetDto> getAllByCompanyName(final Optional<String> filter, final String companyName,
                                       final Pageable pageable, final Long authUserId);

    List<AssetDto> generation(final Long companyId, final User authUser);

    List<AssetDto> transition(final List<AssetDto> assetDtos, final Long toId);

    List<AssetDto> getMarketable(final Long authUserId);

    List<AssetDto> getExpensiveAndMarketable(final Long authUserId);

    int countByCompanyName(final Optional<String> filter, final String companyName, final Long authUserId);
}
