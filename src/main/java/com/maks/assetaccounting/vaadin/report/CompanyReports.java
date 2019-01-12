package com.maks.assetaccounting.vaadin.report;

import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.maks.assetaccounting.vaadin.company.CompanyMain;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "reports/companies", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Reports/Companies")
public class CompanyReports extends CompanyMain {

    private final H4 companyH4 = new H4("Company List");

    public CompanyReports(final CompanyService companyService) {
        super(companyService);

        final Button companiesOrderBtn =
                new Button("Get Companies With Assets In Ascending Order Of Cost");
        companiesOrderBtn.addClickListener(event -> getCompaniesWithAssetsInAscendingOrder());
        final Button companiesMostAssetsBtn = new Button("Get Companies With The Most Assets");
        companiesMostAssetsBtn.addClickListener(event -> getCompaniesWithTheMostAssets());

        add(companiesOrderBtn, companiesMostAssetsBtn, companyH4, getGrid());
        updateList();
    }

    private void getCompaniesWithAssetsInAscendingOrder() {
        clearFilters();
        getGrid().setItems(getCompanyService().findCompaniesWithAssetsInAscendingOrder());
        companyH4.setText("Companies With Assets In Ascending Order Of Cost");
    }

    private void getCompaniesWithTheMostAssets() {
        clearFilters();
        getGrid().setItems(getCompanyService().getCompaniesWithTheMostAssets());
        companyH4.setText("Companies With The Most Assets");
    }

    private void clearFilters() {
        getFilterByName().clear();
    }
}
