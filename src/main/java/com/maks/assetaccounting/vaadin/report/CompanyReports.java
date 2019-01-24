package com.maks.assetaccounting.vaadin.report;

import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.maks.assetaccounting.vaadin.company.CompanyForm;
import com.maks.assetaccounting.vaadin.company.CompanyMain;
import com.maks.assetaccounting.vaadin.dataprovider.AssetDataProvider;
import com.maks.assetaccounting.vaadin.dataprovider.CompanyDataProvider;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "reports/companies", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Reports/Companies")
public class CompanyReports extends CompanyMain {
    private final H4 companyH4 = new H4("Company List");

    public CompanyReports(final CompanyService companyService, final CompanyDataProvider companyDataProvider,
                          final CompanyForm companyForm, final AssetService assetService,
                          final AssetDataProvider assetDataProvider) {
        super(companyService, companyDataProvider, companyForm, assetService, assetDataProvider);

        final ComboBox<String> reportsComboBox = new ComboBox<>("Reports");
        reportsComboBox.setPlaceholder("Reports");
        reportsComboBox.setItems("Most Assets");
        reportsComboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                companyDataProvider.setSortOrder(null);
                companyH4.setText("Company List");
                companyDataProvider.setReportFilter(null);
                wrapper.refreshAll();
            } else {
                switch (event.getValue()) {
                    case "Most Assets":
                        getWithTheMostAssets();
                        break;
                }
            }
        });

        add(reportsComboBox, companyH4, grid);
    }

    private void getWithTheMostAssets() {
        getCompanyDataProvider().setReportFilter("Most Assets");
        companyH4.setText("Companies With The Most Assets");
        wrapper.refreshAll();
    }
}
