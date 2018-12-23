package com.maks.assetaccounting.service;

import com.maks.assetaccounting.dto.AssetDto;

import java.util.List;

public interface AssetService extends CrudService<AssetDto, AssetDto> {

    List<AssetDto> generation(Long companyId);

    List<AssetDto> transition(List<AssetDto> assetDtos, Long toId);

    List<AssetDto> getMarketable();

    List<AssetDto> getExpensiveAndMarketable();
}
