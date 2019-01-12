package com.maks.assetaccounting.vaadin.asset;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

import static com.maks.assetaccounting.util.ValidationUtil.checkNotFound;

@Route(value = "assets", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Assets")
public class AssetView extends AssetMain {

    private final TextField generationCompanyName;
    private final TextField transitionCompanyName;

    public AssetView(final AssetService assetService, final CompanyService companyService) {
        super(assetService, companyService);

        final Button addAssetBtn = new Button("Add new asset");
        addAssetBtn.addClickListener(e -> {
            getAssetForm().setAssetDto(new AssetDto());
            getSaveDialog().open();
        });

        generationCompanyName = new TextField();
        generationCompanyName.setPlaceholder("Generation for company...");
        final Button generationBtn = new Button("Generation Assets");
        generationBtn.addClickListener(e -> {
            String companyName = generationCompanyName.getValue();
            getAssetService().generation(checkNotFound(getCompanyService()
                    .getByName(companyName), "name = " + companyName).getId());
            generationCompanyName.clear();
            updateList();
        });

        List<AssetDto> assetDtos = new ArrayList<>();
        transitionCompanyName = new TextField();
        transitionCompanyName.setPlaceholder("Transition to company...");
        final Button transitionBtn = new Button("Transition Assets");
        transitionBtn.addClickListener(e -> {
            String companyName = transitionCompanyName.getValue();
            getAssetService().transition(assetDtos, checkNotFound(getCompanyService()
                    .getByName(companyName), "name = " + companyName).getId());
            transitionCompanyName.clear();
            updateList();
        });

        getGrid().setSelectionMode(Grid.SelectionMode.MULTI);
        getGrid().asMultiSelect()
                .addSelectionListener(event -> assetDtos.addAll(event.getValue()));

        final HorizontalLayout generation = new HorizontalLayout(
                generationCompanyName, generationBtn);
        final HorizontalLayout transition = new HorizontalLayout(
                transitionCompanyName, transitionBtn);

        add(generation, transition, addAssetBtn, getGrid());
        updateList();
    }
}
