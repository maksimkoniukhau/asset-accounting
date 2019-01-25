package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.components.CancelButton;
import com.maks.assetaccounting.vaadin.dataproviders.AssetDataProvider;
import com.maks.assetaccounting.vaadin.dataproviders.CompanyDataProvider;
import com.maks.assetaccounting.vaadin.forms.AssetForm;
import com.maks.assetaccounting.vaadin.forms.CompanyForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Route
@Data
public class CompanyMain extends AbstractView<CompanyDto> {
    private final CompanyService companyService;
    private final CompanyForm companyForm;
    private final Dialog saveDialog;

    private final AssetService assetService;
    private final CompanyDataProvider companyDataProvider;
    private final AssetDataProvider assetDataProvider;

    @Autowired
    public CompanyMain(final CompanyService companyService, final CompanyDataProvider companyDataProvider,
                       final CompanyForm companyForm, final AssetService assetService,
                       final AssetDataProvider assetDataProvider) {
        super(companyDataProvider.withConfigurableFilter(), companyService, new Grid<>());

        this.companyService = companyService;
        this.companyForm = companyForm;
        this.saveDialog = new Dialog();

        this.assetService = assetService;
        this.companyDataProvider = companyDataProvider;
        this.assetDataProvider = assetDataProvider;

        addBtn.setText("New company");
        addBtn.addClickListener(e -> {
            companyForm.setCompanyDto(new CompanyDto());
            getSaveDialog().open();
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
    }

    @Override
    protected void setupGrid(final Grid<CompanyDto> grid) {
        grid.addColumn(CompanyDto::getId).setHeader("Company Id").setVisible(false);
        grid.addColumn(CompanyDto::getName).setSortProperty("name").setHeader("Company Name");
        grid.addComponentColumn(companyDto -> {
            final Dialog assetListDialog = new Dialog();
            final AssetView assetView = new AssetView(assetService, companyService, assetDataProvider,
                    companyDataProvider, new AssetForm(companyService, companyDataProvider));
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
        }).setWidth("1px");

        grid.setWidth("1100px");
    }

    public void save() {
        final CompanyDto companyDto = companyForm.getCompanyDto();
        if (companyDto.getId() == null) {
            companyService.create(companyDto);
            wrapper.refreshAll();
        } else {
            companyService.update(companyDto, companyDto.getId());
            wrapper.refreshItem(companyDto);
        }
        companyForm.setCompanyDto(null);
    }
}

