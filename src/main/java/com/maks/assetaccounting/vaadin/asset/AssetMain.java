package com.maks.assetaccounting.vaadin.asset;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.maks.assetaccounting.service.company.CompanyService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.maks.assetaccounting.vaadin.util.FormatterUtil.DATE_TIME_FORMATTER;

@Route
@Data
public class AssetMain extends VerticalLayout implements RouterLayout {
    private final AssetService assetService;
    private final CompanyService companyService;

    private final AssetForm assetForm = new AssetForm(this);
    private final Grid<AssetDto> grid = new Grid<>();
    private final Dialog saveDialog = new Dialog();
    private final TextField filterByName = new TextField();
    private final TextField filterByCompanyName = new TextField();

    @Autowired
    public AssetMain(final AssetService assetService, final CompanyService companyService) {
        this.assetService = assetService;
        this.companyService = companyService;
        this.init();
    }

    private void init() {
        final Dialog deleteDialog = new Dialog(new H4("Are you sure?"));

        final Button saveButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        saveButton.getElement().setAttribute("theme", "primary");
        saveButton.addClickListener(event -> {
            assetForm.save();
            saveDialog.close();
        });

        final Button deleteButton = new Button("Delete", new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(event -> {
            assetForm.delete();
            deleteDialog.close();
        });

        final Button cancelSaveBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelSaveBtn.addClickListener(event -> saveDialog.close());

        final Button cancelDelBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelDelBtn.getElement().setAttribute("theme", "primary");
        cancelDelBtn.addClickListener(event -> deleteDialog.close());

        saveDialog.setWidth("400px");
        saveDialog.add(assetForm);
        saveDialog.add(saveButton, cancelSaveBtn);

        deleteDialog.setWidth("400px");
        deleteDialog.add(deleteButton, cancelDelBtn);

        filterByName.setPlaceholder("Filter by name...");
        filterByName.addValueChangeListener(e -> updateList());
        final Button clearFilterNameBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterNameBtn.addClickListener(e -> filterByName.clear());
        filterByCompanyName.setPlaceholder("Filter by company name...");
        filterByCompanyName.addValueChangeListener(e -> updateList());
        final Button clearFilterCompanyNameBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterCompanyNameBtn.addClickListener(e -> filterByCompanyName.clear());
        final HorizontalLayout filtering = new HorizontalLayout(filterByName
                , clearFilterNameBtn, filterByCompanyName, clearFilterCompanyNameBtn);

        grid.setSizeFull();
        grid.addColumn(AssetDto::getId).setHeader("Asset Id").setVisible(false);
        grid.addColumn(AssetDto::getName).setHeader("Asset Name");
        grid.addColumn(assetDto -> assetDto.getCreationDate()
                .format(DATE_TIME_FORMATTER)).setHeader("Creation Date");
        grid.addColumn(assetDto -> assetDto.getTransferDate()
                .format(DATE_TIME_FORMATTER)).setHeader("Transfer Date");
        grid.addColumn(AssetDto::getCost).setHeader("Cost");
        grid.addColumn(AssetDto::getCompanyName).setHeader("Company Name");
        grid.addComponentColumn(assetDto -> {
            final Button edit = new Button("Edit");
            edit.addClickListener(event -> {
                assetForm.setAssetDto(assetDto);
                saveDialog.open();
            });
            return edit;
        });
        grid.addComponentColumn(assetDto -> {
            final Button delete = new Button("Delete");
            delete.addClickListener(event -> {
                assetForm.setAssetDto(assetDto);
                deleteDialog.open();
            });
            return delete;
        });

        add(filtering);
        setHeight("90vh");
    }

    public void updateList() {
        final String filterName = filterByName.getValue();
        final String filterCompanyName = filterByCompanyName.getValue();
        if (filterName != null && !filterName.isEmpty()) {
            final List<AssetDto> filterNameDto = assetService.getAllByName(filterName);
            if (filterNameDto != null) grid.setItems(filterNameDto);
        } else {
            if (filterCompanyName != null && !filterCompanyName.isEmpty()) {
                if (companyService.getByName(filterCompanyName) != null) {
                    final List<AssetDto> filterCompanyNameDto = assetService
                            .getAllByCompanyName(filterCompanyName);
                    if (filterCompanyNameDto != null) grid.setItems(filterCompanyNameDto);
                }
            } else {
                final List<AssetDto> assetDtos = assetService.getAll();
                if (assetDtos != null)
                    grid.setItems(assetDtos);
            }
        }
    }
}
