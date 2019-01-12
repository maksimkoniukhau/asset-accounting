package com.maks.assetaccounting.vaadin.company;

import com.maks.assetaccounting.dto.CompanyDto;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class CompanyForm extends FormLayout {
    private final CompanyMain companyMain;
    private CompanyDto companyDto;
    private final TextField name = new TextField("Company Name");
    private final Binder<CompanyDto> binder = new Binder<>(CompanyDto.class);

    public CompanyForm(final CompanyMain companyMain) {
        this.companyMain = companyMain;
        this.init();
    }

    private void init() {
        binder.bindInstanceFields(this);
        add(name);
        setCompanyDto(null);
    }

    public void setCompanyDto(final CompanyDto companyDto) {
        this.companyDto = companyDto;
        binder.setBean(companyDto);
        name.focus();
    }

    public void delete() {
        companyMain.getCompanyService().delete(companyDto.getId());
        companyMain.updateList();
        setCompanyDto(null);
    }

    public void save() {
        if (companyDto.getId() == null) {
            companyMain.getCompanyService().create(companyDto);
        } else {
            companyMain.getCompanyService().update(companyDto, companyDto.getId());
        }
        companyMain.updateList();
        setCompanyDto(null);
    }
}
