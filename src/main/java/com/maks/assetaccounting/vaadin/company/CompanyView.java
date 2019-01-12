package com.maks.assetaccounting.vaadin.company;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "companies", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Companies")
public class CompanyView extends CompanyMain {

    public CompanyView(final CompanyService companyService) {
        super(companyService);

        final Button addCompanyBtn = new Button("Add new company");
        addCompanyBtn.addClickListener(e -> {
            getCompanyForm().setCompanyDto(new CompanyDto());
            getSaveDialog().open();
        });

        add(addCompanyBtn, getGrid());
        updateList();
    }
}
