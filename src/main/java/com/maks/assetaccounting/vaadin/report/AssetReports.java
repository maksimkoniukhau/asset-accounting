package com.maks.assetaccounting.vaadin.report;

import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.maks.assetaccounting.vaadin.asset.AssetForm;
import com.maks.assetaccounting.vaadin.asset.AssetMain;
import com.maks.assetaccounting.vaadin.dataprovider.AssetDataProvider;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@Route(value = "reports/assets", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Reports/Assets")
public class AssetReports extends AssetMain {

    private final AssetDataProvider assetDataProvider;
    private final H4 assetH4 = new H4("Asset List");

    public AssetReports(final AssetService assetService, final CompanyService companyService,
                        final AssetDataProvider assetDataProvider, final AssetForm assetForm) {
        super(assetService, companyService, assetDataProvider, assetForm);

        this.assetDataProvider = assetDataProvider;

        final ComboBox<String> reportsComboBox = new ComboBox<>("Reports");
        reportsComboBox.setPlaceholder("Reports");
        reportsComboBox.setItems("Marketable", "Expensive And Marketable");
        reportsComboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                assetDataProvider.setSortOrder(null);
                assetH4.setText("Asset List");
                wrapper.refreshAll();
            } else {
                switch (event.getValue()) {
                    case "Marketable":
                        getMarketable();
                        break;
                    case "Expensive And Marketable":
                        getExpensiveAndMarketable();
                        break;
                }
            }
        });

        add(reportsComboBox, assetH4, grid);
    }

    private void getMarketable() {
        assetDataProvider.setSortOrder(new QuerySortOrder("numberOfTransition", SortDirection.DESCENDING));
        assetH4.setText("Marketable Assets");
        wrapper.refreshAll();
    }

    private void getExpensiveAndMarketable() {
        final List<QuerySortOrder> sortOrders = Arrays
                .asList(new QuerySortOrder("cost", SortDirection.DESCENDING),
                        new QuerySortOrder("numberOfTransition", SortDirection.DESCENDING));
        assetDataProvider.setSortOrders(sortOrders);
        assetH4.setText("Expensive And Marketable Assets");
        wrapper.refreshAll();
    }
}
