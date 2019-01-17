package com.maks.assetaccounting.vaadin.report;

import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "reports", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Reports")
public class ReportView extends VerticalLayout {

    public ReportView() {
        final Image img = new Image("https://anbl-2.azureedge.net/medias/Reports.jpg?fv=5BB0648D75F3A37F947A6634999E6330-393237", "Reports");
        img.setSizeFull();
        img.setHeight("300px");

        final String companiesRoute = UI.getCurrent().getRouter().getUrl(CompanyReports.class);
        final Anchor companies = new Anchor(companiesRoute, "Company Reports");
        companies.getStyle().set("text-decoration", "none");

        final String assetsRoute = UI.getCurrent().getRouter().getUrl(AssetReports.class);
        final Anchor assets = new Anchor(assetsRoute, "Asset Reports");
        assets.getStyle().set("text-decoration", "none");

        setSizeFull();
        add(img, new H4(companies), new H4(assets));
    }
}
