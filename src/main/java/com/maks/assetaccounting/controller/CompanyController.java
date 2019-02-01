package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.company.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.maks.assetaccounting.util.SecurityUtil.getAuthUserId;
import static com.maks.assetaccounting.util.SecurityUtil.getAuthUsername;

@RestController
@RequestMapping("company")
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public CompanyDto createCompany(@RequestBody final CompanyDto companyDto) {
        final String authUsername = getAuthUsername();
        log.info("create {} for user with username {}", companyDto, authUsername);
        return companyService.create(companyDto, authUsername);
    }

    @GetMapping
    public List<CompanyDto> getAllCompanies() {
        final Long authUserId = getAuthUserId();
        log.info("get all companies for user with id {}", authUserId);
        return companyService.getAll(authUserId);
    }

    @GetMapping("{id}")
    public CompanyDto getCompany(@PathVariable("id") final Long id) {
        final Long authUserId = getAuthUserId();
        log.info("get company with id {} for user with id {}", id, authUserId);
        return companyService.get(id, authUserId);
    }
}



