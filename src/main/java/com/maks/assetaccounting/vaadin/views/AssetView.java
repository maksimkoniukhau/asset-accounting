package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.components.AppLayoutClass;
import com.maks.assetaccounting.vaadin.dataproviders.AssetDataProvider;
import com.maks.assetaccounting.vaadin.dataproviders.CompanyDataProvider;
import com.maks.assetaccounting.vaadin.forms.AssetForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Route(value = "assets", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Assets")
@Data
public class AssetView extends AssetMain {

    private final HorizontalLayout transitionGeneration;
    private final Button generationBtn;

    public AssetView(final AssetService assetService, final CompanyService companyService,
                     final AssetDataProvider assetDataProvider, final CompanyDataProvider companyDataProvider,
                     final AssetForm assetForm) {
        super(assetService, companyService, assetDataProvider, assetForm);

        final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();

        this.generationBtn = new Button("Generation Assets");
        generationBtn.setEnabled(false);
        generationBtn.addClickListener(e -> {
            assetService.generation(companyDtoComboBox.getValue().getId());
            companyDtoComboBox.clear();
            dataProvider.refreshAll();
        });

        final List<AssetDto> assetDtos = new ArrayList<>();
        final Button transitionBtn = new Button("Transition Assets");
        transitionBtn.setEnabled(false);
        transitionBtn.addClickListener(e -> {
            assetService.transition(assetDtos, companyDtoComboBox.getValue().getId());
            companyDtoComboBox.clear();
            dataProvider.refreshAll();
        });

        companyDtoComboBox.setPlaceholder("Company");
        companyDtoComboBox.setDataProvider(companyDataProvider);
        companyDtoComboBox.setItemLabelGenerator(CompanyDto::getName);
        companyDtoComboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                generationBtn.setEnabled(false);
                transitionBtn.setEnabled(false);
            } else {
                generationBtn.setEnabled(true);
                if (!grid.getSelectedItems().isEmpty()) {
                    transitionBtn.setEnabled(true);
                }
            }
        });

        grid.asMultiSelect().addSelectionListener(event -> {
            if (!event.getValue().isEmpty()) {
                if (companyDtoComboBox.getValue() != null) {
                    transitionBtn.setEnabled(true);
                }
                assetDtos.addAll(event.getValue());
            } else {
                transitionBtn.setEnabled(false);
            }
        });

        this.transitionGeneration = new HorizontalLayout(
                companyDtoComboBox, generationBtn, transitionBtn);

        add(transitionGeneration, grid);
    }
}
