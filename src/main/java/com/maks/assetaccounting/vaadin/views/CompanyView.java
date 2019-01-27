package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.components.AppLayoutClass;
import com.maks.assetaccounting.vaadin.components.CancelButton;
import com.maks.assetaccounting.vaadin.dataproviders.AssetDataProvider;
import com.maks.assetaccounting.vaadin.dataproviders.CompanyDataProvider;
import com.maks.assetaccounting.vaadin.forms.AssetForm;
import com.maks.assetaccounting.vaadin.forms.CompanyForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "companies", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Companies")
@Data
public class CompanyView extends AbstractView<CompanyDto> {
    private final CompanyService companyService;
    private final CompanyForm companyForm;
    private final Dialog saveDialog;

    private final AssetService assetService;
    private final CompanyDataProvider companyDataProvider;
    private final AssetDataProvider assetDataProvider;

    @Autowired
    public CompanyView(final CompanyService companyService, final CompanyDataProvider companyDataProvider,
                       final CompanyForm companyForm, final AssetService assetService,
                       final AssetDataProvider assetDataProvider) {
        super(companyDataProvider, companyService, new Grid<>());

        this.companyService = companyService;
        this.companyForm = companyForm;
        this.saveDialog = new Dialog();

        this.assetService = assetService;
        this.companyDataProvider = companyDataProvider;
        this.assetDataProvider = assetDataProvider;

        addBtn.setText("New company");
        addBtn.addClickListener(e -> {
            companyForm.setCompanyDto(new CompanyDto());
            saveDialog.open();
        });

        final Button saveButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        saveButton.getElement().setAttribute("theme", "primary");
        saveButton.addClickListener(event -> {
            if (companyForm.isValid()) {
                save();
                saveDialog.close();
            }
        });

        saveDialog.setWidth("400px");
        saveDialog.add(companyForm, saveButton, new CancelButton(saveDialog, null));

        final ComboBox<String> reportsComboBox = new ComboBox<>();
        reportsComboBox.setPlaceholder("Reports");
        reportsComboBox.setItems("Most Assets");
        reportsComboBox.addValueChangeListener(event -> {
            if (event.getSource().isEmpty()) {
                companyDataProvider.setSortOrder(null);
                companyDataProvider.setReportFilter(null);
                companyDataProvider.refreshAll();
            } else {
                switch (event.getValue()) {
                    case "Most Assets":
                        getWithTheMostAssets();
                        break;
                }
            }
        });

        add(reportsComboBox, grid);
    }

    @Override
    protected void setupGrid(final Grid<CompanyDto> grid) {
        grid.addColumn(CompanyDto::getId).setHeader("Company Id").setVisible(false);
        grid.addColumn(CompanyDto::getName).setSortProperty("name").setHeader("Company Name");
        grid.addComponentColumn(companyDto -> {
            final Dialog assetListDialog = new Dialog();
            final AssetView assetView = new AssetView(assetService, companyService, assetDataProvider,
                    companyDataProvider, new AssetForm(companyService, companyDataProvider));
            assetView.getActionPanel().remove(assetView.getReportsComboBox());
            assetView.getTransitionGeneration().remove(assetView.getGenerationBtn());
            assetListDialog.add(assetView);
            assetListDialog.setWidth("1200px");
            assetListDialog.setHeight("600px");
            assetListDialog.addDialogCloseActionListener(event -> {
                assetDataProvider.setCompanyName(null);
                assetListDialog.close();
            });
            final Button assetsBtn = new Button("Asset List");
            assetsBtn.addClickListener(event -> {
                assetDataProvider.setCompanyName(companyDto.getName());
                assetListDialog.open();
            });
            assetsBtn.getStyle().set("background-color", "transparent");
            return assetsBtn;
        }).setHeader("Company Assets");
        grid.addComponentColumn(companyDto -> {
            final Button edit = new Button();
            edit.setIcon(VaadinIcon.EDIT.create());
            edit.addClickListener(event -> {
                companyForm.setCompanyDto(companyDto);
                saveDialog.open();
            });
            return edit;
        }).setWidth("80px").setFlexGrow(0);

        grid.setWidth("1100px");
    }

    private void save() {
        final CompanyDto companyDto = companyForm.getCompanyDto();
        if (companyDto.getId() == null) {
            companyService.create(companyDto);
        } else {
            companyService.update(companyDto, companyDto.getId());
        }
        companyDataProvider.refreshAll();
        companyForm.setCompanyDto(null);
    }

    private void getWithTheMostAssets() {
        companyDataProvider.setReportFilter("Most Assets");
        companyDataProvider.refreshAll();
    }
}

