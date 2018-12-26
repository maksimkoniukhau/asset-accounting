package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("asset")
@Slf4j
public class AssetController {
    private final AssetService assetService;

    @Autowired
    public AssetController(final AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public AssetDto createAsset(@RequestBody final AssetDto assetDto) {
        log.info("create {}", assetDto);
        return assetService.create(assetDto);
    }

    @GetMapping
    public List<AssetDto> getAllAssets() {
        log.info("get all assets");
        return assetService.getAll();
    }

    @GetMapping("generation")
    public List<AssetDto> generationAssets(@RequestParam(value = "to") final Long to) {
        log.info("generation assets for company with id {}", to);
        return assetService.generation(to);
    }

    @PutMapping("transition")
    public List<AssetDto> transitionAssets(@RequestBody final List<AssetDto> assetDtos, @RequestParam(value = "to") final Long to) {
        log.info("transition assets to company with id {}", to);
        return assetService.transition(assetDtos, to);
    }
}
