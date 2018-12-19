package com.maks.assetaccounting.controller;

import com.maks.assetaccounting.model.Asset;
import com.maks.assetaccounting.model.Company;
import com.maks.assetaccounting.service.AssetService;
import com.maks.assetaccounting.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("company")
@Slf4j
public class CompanyController {
    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("create")
    public Company createCompany(@RequestBody Company company) {
        log.info("create {}", company);
        return companyService.create(company);
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        log.info("get all companies");
        return companyService.getAll();
    }
}



