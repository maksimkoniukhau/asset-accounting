package com.maks.assetaccounting.service;

import com.maks.assetaccounting.model.Asset;
import com.maks.assetaccounting.model.Company;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CompanyServiceImpl implements CrudService<Company>, CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company create(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company get(Company company) {
        return company;
    }

    @Override
    public Company update(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company delete(Company company) {
        companyRepository.delete(company);
        return company;
    }

    @Override
    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> getCompaniesWithAssetsInAscendingOrder() {
        List<Company> companies = companyRepository.findAll();
        companies.forEach(company -> company.getAssets().sort(Comparator.comparing(Asset::getCost)));
        return companies;
    }
}
