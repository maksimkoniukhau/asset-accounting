package com.maks.assetaccounting.vaadin.company;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.dto.CompanyDto;
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
public class CompanyMain extends VerticalLayout implements RouterLayout {
    private final CompanyService companyService;

    private final CompanyForm companyForm = new CompanyForm(this);
    private final Grid<CompanyDto> grid = new Grid<>();
    private final TextField filterByName = new TextField();
    private final Dialog saveDialog = new Dialog();

    @Autowired
    public CompanyMain(final CompanyService companyService) {
        this.companyService = companyService;
        this.init();
    }

    private void init() {
        final Dialog deleteDialog = new Dialog(new H4("Are you sure?"));

        final Button saveButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        saveButton.getElement().setAttribute("theme", "primary");
        saveButton.addClickListener(event -> {
            companyForm.save();
            saveDialog.close();
        });

        final Button deleteButton = new Button("Delete", new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(event -> {
            companyForm.delete();
            deleteDialog.close();
        });

        final Button cancelSaveBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelSaveBtn.addClickListener(event -> saveDialog.close());

        final Button cancelDelBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelDelBtn.getElement().setAttribute("theme", "primary");
        cancelDelBtn.addClickListener(event -> deleteDialog.close());

        saveDialog.setWidth("400px");
        saveDialog.add(companyForm);
        saveDialog.add(saveButton, cancelSaveBtn);

        deleteDialog.setWidth("400px");
        deleteDialog.add(deleteButton, cancelDelBtn);

        filterByName.setPlaceholder("Filter by name...");
        filterByName.addValueChangeListener(e -> updateList());
        final Button clearFilterTextBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterTextBtn.addClickListener(e -> filterByName.clear());
        final HorizontalLayout filtering = new HorizontalLayout(filterByName, clearFilterTextBtn);

        grid.setSizeFull();
        grid.addColumn(CompanyDto::getId).setHeader("Company Id").setVisible(false);
        grid.addColumn(CompanyDto::getName).setHeader("Company Name");
        grid.addComponentColumn(companyDto -> {
            final Dialog assetListDialog = new Dialog();
            final Grid<AssetDto> assetGrid = new Grid<>();
            assetGrid.addColumn(AssetDto::getName).setHeader("Asset Name");
            assetGrid.addColumn(assetDto -> assetDto.getCreationDate()
                    .format(DATE_TIME_FORMATTER)).setHeader("Creation Date");
            assetGrid.addColumn(assetDto -> assetDto.getTransferDate()
                    .format(DATE_TIME_FORMATTER)).setHeader("Transfer Date");
            assetGrid.addColumn(AssetDto::getCost).setHeader("Cost");
            assetGrid.addColumn(AssetDto::getCompanyName).setHeader("Company Name");
            assetGrid.setSizeFull();
            assetGrid.setItems(companyDto.getAssetDtos());
            assetListDialog.add(assetGrid);
            assetListDialog.setWidth("800px");
            assetListDialog.setHeight("600px");
            final Button assetsBtn = new Button("Asset List");
            assetsBtn.addClickListener(event -> assetListDialog.open());
            assetsBtn.getStyle().set("background-color", "white");
            return assetsBtn;
        }).setHeader("Company Assets");
        grid.addComponentColumn(companyDto -> {
            final Button edit = new Button("Edit");
            edit.addClickListener(event -> {
                companyForm.setCompanyDto(companyDto);
                saveDialog.open();
            });
            return edit;
        });
        grid.addComponentColumn(companyDto -> {
            final Button delete = new Button("Delete");
            delete.addClickListener(event -> {
                companyForm.setCompanyDto(companyDto);
                deleteDialog.open();
            });
            return delete;
        });

        add(filtering);
        setHeight("90vh");
    }

    public void updateList() {
        final String filterString = filterByName.getValue();
        if (filterString != null && !filterString.isEmpty()) {
            final CompanyDto companyDto = companyService.getByName(filterString);
            if (companyDto != null) grid.setItems(companyDto);
        } else {
            final List<CompanyDto> companyDtos = companyService.getAll();
            if (companyDtos != null)
                grid.setItems(companyDtos);
        }
    }
}
