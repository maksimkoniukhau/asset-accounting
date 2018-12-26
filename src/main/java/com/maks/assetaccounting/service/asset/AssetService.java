package com.maks.assetaccounting.service.asset;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.CrudService;

import java.util.List;

public interface AssetService extends CrudService<AssetDto, AssetDto> {

    List<AssetDto> generation(final Long companyId);

    List<AssetDto> transition(final List<AssetDto> assetDtos, final Long toId);

    List<AssetDto> getMarketable();

    List<AssetDto> getExpensiveAndMarketable();
}
