package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.components.AppLayoutClass;
import com.maks.assetaccounting.vaadin.dataproviders.AssetDataProvider;
import com.maks.assetaccounting.vaadin.dataproviders.CompanyDataProvider;
import com.maks.assetaccounting.vaadin.forms.CompanyForm;
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
