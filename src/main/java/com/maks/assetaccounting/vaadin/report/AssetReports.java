package com.maks.assetaccounting.vaadin.report;

import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.maks.assetaccounting.vaadin.asset.AssetMain;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "reports/assets", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Reports/Assets")
public class AssetReports extends AssetMain {

    private final H4 assetH4 = new H4("Asset List");

    public AssetReports(final AssetService assetService, final CompanyService companyService) {
        super(assetService, companyService);

        final Button marketableBtn = new Button("Get Marketable Assets");
        marketableBtn.addClickListener(event -> getMarketableAssets());
        final Button expensiveBtn = new Button("Get Expensive And Marketable Assets");
        expensiveBtn.addClickListener(event -> getExpensiveAndMarketableAssets());

        add(marketableBtn, expensiveBtn, assetH4, getGrid());
        updateList();
    }

    private void getMarketableAssets() {
        clearFilters();
        getGrid().setItems(getAssetService().getMarketable());
        assetH4.setText("Marketable Assets");
    }

    private void getExpensiveAndMarketableAssets() {
        clearFilters();
        getGrid().setItems(getAssetService().getExpensiveAndMarketable());
        assetH4.setText("Expensive And Marketable Assets");
    }

    private void clearFilters() {
        getFilterByName().clear();
        getFilterByCompanyName().clear();
    }
}
