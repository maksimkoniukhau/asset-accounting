package com.maks.assetaccounting.service;

import com.maks.assetaccounting.model.Asset;

import java.util.List;

public interface AssetService {

    List<Asset> generation(Long companyId);

    List<Asset> transition(List<Asset> assets, Long toId);

    List<Asset> getMarketable();

    List<Asset> getExpensiveAndMarketable();
}
