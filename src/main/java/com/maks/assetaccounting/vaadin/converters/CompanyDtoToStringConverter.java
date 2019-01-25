package com.maks.assetaccounting.vaadin.converters;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.company.CompanyService;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class CompanyDtoToStringConverter implements Converter<CompanyDto, String> {

    private final CompanyService companyService;

    public CompanyDtoToStringConverter(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public Result<String> convertToModel(final CompanyDto value, final ValueContext context) {
        try {
            return Result.ok(value.getName());
        } catch (NumberFormatException e) {
            return Result.error("No exists such company");
        }
    }

    @Override
    public CompanyDto convertToPresentation(final String value, final ValueContext context) {
        return companyService.getByName(value);
    }
}
