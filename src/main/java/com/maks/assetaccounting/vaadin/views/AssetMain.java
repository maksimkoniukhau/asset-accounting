package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.components.CancelButton;
import com.maks.assetaccounting.vaadin.dataproviders.AssetDataProvider;
import com.maks.assetaccounting.vaadin.forms.AssetForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import static com.maks.assetaccounting.vaadin.utils.FormatterUtil.DATE_TIME_FORMATTER;

@Route
@Data
public class AssetMain extends AbstractView<AssetDto> {
    private final AssetService assetService;
    private final AssetForm assetForm;
    private final Dialog saveDialog;

    @Autowired
    public AssetMain(final AssetService assetService, final CompanyService companyService,
                     final AssetDataProvider assetDataProvider, final AssetForm assetForm) {
        super(assetDataProvider, assetService, new Grid<>());

        this.assetService = assetService;
        this.assetForm = assetForm;
        this.saveDialog = new Dialog();

        addBtn.setText("New asset");
        addBtn.addClickListener(e -> {
            assetForm.setAssetDto(new AssetDto());
            getSaveDialog().open();
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
        System.out.println("wqrwer");
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
        }).setWidth("1px");

        grid.setWidth("1200px");
    }

    public void save() {
        final AssetDto assetDto = assetForm.getAssetDto();
        if (assetDto.getId() == null) {
            assetService.create(assetDto);
            dataProvider.refreshAll();
        } else {
            assetService.update(assetDto, assetDto.getId());
            dataProvider.refreshItem(assetDto);
        }
        assetForm.setAssetDto(null);
    }
}
