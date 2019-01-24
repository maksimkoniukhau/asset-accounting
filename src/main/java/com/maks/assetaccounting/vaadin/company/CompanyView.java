package com.maks.assetaccounting.vaadin.company;

import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.maks.assetaccounting.vaadin.dataprovider.AssetDataProvider;
import com.maks.assetaccounting.vaadin.dataprovider.CompanyDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "companies", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Companies")
public class CompanyView extends CompanyMain {

    public CompanyView(final CompanyService companyService, final CompanyDataProvider companyDataProvider,
                       final CompanyForm companyForm, final AssetService assetService,
                       final AssetDataProvider assetDataProvider) {
        super(companyService, companyDataProvider, companyForm, assetService, assetDataProvider);

        add(grid);
    }
}
