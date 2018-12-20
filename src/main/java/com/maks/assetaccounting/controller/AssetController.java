package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.model.Asset;
import com.maks.assetaccounting.service.AssetService;
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
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public Asset createAsset(@RequestBody Asset asset) {
        log.info("create {}", asset);
        return assetService.create(asset);
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        log.info("get all assets");
        return assetService.getAll();
    }

    @GetMapping("generation")
    public List<Asset> generationAssets(@RequestParam(value = "to") Long to) {
        log.info("generation assets for company with id {}", to);
        return assetService.generation(to);
    }

    @PutMapping("transition")
    public List<Asset> transitionAssets(@RequestBody List<Asset> assets, @RequestParam(value = "to") Long to) {
        log.info("transition assets to company with id {}", to);
        return assetService.transition(assets, to);
    }
}
