package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.components.AppLayoutClass;
import com.maks.assetaccounting.vaadin.components.CancelButton;
import com.maks.assetaccounting.vaadin.dataproviders.AssetDataProvider;
import com.maks.assetaccounting.vaadin.dataproviders.CompanyDataProvider;
import com.maks.assetaccounting.vaadin.forms.AssetForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.maks.assetaccounting.vaadin.utils.FormatterUtil.DATE_TIME_FORMATTER;

@Route(value = "assets", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Assets")
@Data
public class AssetView extends AbstractView<AssetDto> {
    private final AssetService assetService;
    private final AssetDataProvider assetDataProvider;
    private final AssetForm assetForm;
    private final Dialog saveDialog;
    private final HorizontalLayout actionPanel;
    private final HorizontalLayout transitionGeneration;
    private final ComboBox<String> reportsComboBox;
    private final Button generationBtn;

    @Autowired
    public AssetView(final AssetService assetService, final CompanyService companyService,
                     final AssetDataProvider assetDataProvider, final CompanyDataProvider companyDataProvider,
                     final AssetForm assetForm) {
        super(assetDataProvider, assetService, new Grid<>());

        this.assetService = assetService;
        this.assetDataProvider = assetDataProvider;
        this.assetForm = assetForm;
        this.saveDialog = new Dialog();

        addBtn.setText("New asset");
        addBtn.addClickListener(e -> {
            assetForm.setAssetDto(new AssetDto());
            saveDialog.open();
        });

        final Button saveButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        saveButton.getElement().setAttribute("theme", "primary");
        saveButton.addClickListener(event -> {
            if (assetForm.isValid()) {
                save();
                saveDialog.close();
            }
        });

        saveDialog.setWidth("400px");
        saveDialog.add(assetForm, saveButton, new CancelButton(saveDialog, null));

        final ComboBox<CompanyDto> companyDtoComboBox = new ComboBox<>();

        this.generationBtn = new Button("Generation Assets");
        generationBtn.setEnabled(false);
        generationBtn.addClickListener(e -> {
            assetService.generation(companyDtoComboBox.getValue().getId());
            companyDtoComboBox.clear();
            assetDataProvider.refreshAll();
        });

        final List<AssetDto> assetDtos = new ArrayList<>();
        final Button transitionBtn = new Button("Transition Assets");
        transitionBtn.setEnabled(false);
        transitionBtn.addClickListener(e -> {
            assetService.transition(assetDtos, companyDtoComboBox.getValue().getId());
            companyDtoComboBox.clear();
            assetDataProvider.refreshAll();
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

        this.reportsComboBox = new ComboBox<>();
        reportsComboBox.setPlaceholder("Reports");
        reportsComboBox.setItems("Marketable", "Expensive And Marketable");
        reportsComboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                assetDataProvider.setSortOrder(null);
                assetDataProvider.refreshAll();
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

        this.transitionGeneration = new HorizontalLayout(
                companyDtoComboBox, generationBtn, transitionBtn);

        this.actionPanel = new HorizontalLayout(transitionGeneration, reportsComboBox);

        add(actionPanel, grid);
    }

    @Override
    protected void setupGrid(final Grid<AssetDto> grid) {
        grid.addColumn(AssetDto::getId).setHeader("Asset Id").setVisible(false);
        grid.addColumn(AssetDto::getName).setSortProperty("name").setHeader("Asset Name");
        grid.addColumn(assetDto -> assetDto.getCreationDate()
                .format(DATE_TIME_FORMATTER)).setSortProperty("creationDate").setHeader("Creation Date");
        grid.addColumn(assetDto -> assetDto.getTransferDate()
                .format(DATE_TIME_FORMATTER)).setSortProperty("transferDate").setHeader("Transfer Date");
        grid.addColumn(AssetDto::getCost).setSortProperty("cost").setHeader("Cost");
        grid.addColumn(AssetDto::getCompanyName).setSortProperty("company").setHeader("Company Name");
        grid.addComponentColumn(assetDto -> {
            final Button edit = new Button();
            edit.setIcon(VaadinIcon.EDIT.create());
            edit.addClickListener(event -> {
                assetForm.setAssetDto(assetDto);
                saveDialog.open();
            });
            return edit;
        }).setWidth("80px").setFlexGrow(0);

        grid.setWidth("1200px");
    }

    private void save() {
        final AssetDto assetDto = assetForm.getAssetDto();
        if (assetDto.getId() == null) {
            assetService.create(assetDto);
            assetDataProvider.refreshAll();
        } else {
            assetService.update(assetDto, assetDto.getId());
            assetDataProvider.refreshItem(assetDto);
        }
        assetForm.setAssetDto(null);
    }

    private void getMarketable() {
        assetDataProvider.setSortOrder(new QuerySortOrder("numberOfTransition", SortDirection.DESCENDING));
        assetDataProvider.refreshAll();
    }

    private void getExpensiveAndMarketable() {
        final List<QuerySortOrder> sortOrders = Arrays
                .asList(new QuerySortOrder("cost", SortDirection.DESCENDING),
                        new QuerySortOrder("numberOfTransition", SortDirection.DESCENDING));
        assetDataProvider.setSortOrders(sortOrders);
        assetDataProvider.refreshAll();
    }
}
