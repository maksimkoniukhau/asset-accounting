package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.AssetService;
import com.maks.assetaccounting.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("report")
@Slf4j
public class ReportController {
    private final CompanyService companyService;

    private final AssetService assetService;

    @Autowired
    public ReportController(CompanyService companyService, AssetService assetService) {
        this.companyService = companyService;
        this.assetService = assetService;
    }

    @GetMapping("company")
    public List<CompanyDto> getCompaniesWithAssetsInAscendingOrder() {
        log.info("get companies with assets in ascending order");
        return companyService.findCompaniesWithAssetsInAscendingOrder();
    }

    @GetMapping("asset/marketable")
    public List<AssetDto> getMarketableAssets() {
        log.info("get marketable assets");
        return assetService.getMarketable();
    }

    @GetMapping("asset/expensive-and-marketable")
    public List<AssetDto> getExpensiveAndMarketableAssets() {
        log.info("get expensive and marketable assets");
        return assetService.getExpensiveAndMarketable();
    }
}
