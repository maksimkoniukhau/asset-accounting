package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.service.asset.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.maks.assetaccounting.util.SecurityUtil.*;

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
        final String authUsername = getAuthUsername();
        log.info("create {} for user with username {}", assetDto, authUsername);
        return assetService.create(assetDto, authUsername);
    }

    @GetMapping
    public List<AssetDto> getAllAssets() {
        final Long authUserId = getAuthUserId();
        log.info("get all assets for user with id {}", authUserId);
        return assetService.getAll(authUserId);
    }

    @GetMapping("generation")
    public List<AssetDto> generationAssets(@RequestParam(value = "to") final Long to) {
        final User authUser = getAuthUser();
        log.info("generation assets for company with id {} for user {}", to, authUser);
        return assetService.generation(to, authUser);
    }

    @PutMapping("transition")
    public List<AssetDto> transitionAssets(@RequestBody final List<AssetDto> assetDtos, @RequestParam(value = "to") final Long to) {
        log.info("transition assets to company with id {}", to);
        return assetService.transition(assetDtos, to);
    }
}
