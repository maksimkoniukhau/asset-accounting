package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.maks.assetaccounting.util.SecurityUtil.getAuthUserId;

@RestController
@RequestMapping("report")
@Slf4j
public class ReportController {
    private final CompanyService companyService;

    private final AssetService assetService;

    @Autowired
    public ReportController(final CompanyService companyService, final AssetService assetService) {
        this.companyService = companyService;
        this.assetService = assetService;
    }

    @GetMapping("asset/marketable")
    public List<AssetDto> getMarketableAssets() {
        final Long authUserId = getAuthUserId();
        log.info("get marketable assets for user with id {}", authUserId);
        return assetService.getMarketable(authUserId);
    }

    @GetMapping("asset/expensive-and-marketable")
    public List<AssetDto> getExpensiveAndMarketableAssets() {
        final Long authUserId = getAuthUserId();
        log.info("get expensive and marketable assets for user with id {}", authUserId);
        return assetService.getExpensiveAndMarketable(authUserId);
    }
}
