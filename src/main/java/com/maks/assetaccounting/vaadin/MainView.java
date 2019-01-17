package com.maks.assetaccounting.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(layout = AppLayoutClass.class)
@PageTitle("Asset Accounting")
public class MainView extends VerticalLayout {

    public MainView() {
        this.init();
    }

    private void init() {
        final Image img = new Image("https://i.imgur.com/LaWFLZf.jpg", "Oracle");
        img.setSizeFull();
        img.setHeight("230px");

        final Component content = new Span(new H3("Прототип Системы Учета активов предприятия и генерация отчетов"),
                new Paragraph("Система учета активов предприятия предназначена для:"),
                new Paragraph(" - Хранения информации об активах компании"),
                new Paragraph(" - Анализа эффективности использования активов с помощью отчетов."),
                new Paragraph("Активы (упрощенно) — это ресурсы, контролируемые компанией. " +
                        "Примеры: мобильные устройства, здания, автомобили и т.д. " +
                        "Активы могут переходить от одной компании к другой."),
                new Paragraph("Необходимо выявлять, то есть создавать отчеты для:"),
                new Paragraph(" - активов, которые переходят от одной компании к другой чаще всего, " +
                        "то есть самых востребованных."),
                new Paragraph(" - компаний, со списком активов в порядке возрастания цены."),
                new Paragraph(" - самых дорогих активов и самых востребованных одновременно"),
                new Paragraph(" - и другие"));

        add(img, content);
    }
}
