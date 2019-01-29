package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.dto.AbstractDto;
import com.maks.assetaccounting.service.CrudService;
import com.maks.assetaccounting.vaadin.components.CancelButton;
import com.maks.assetaccounting.vaadin.dataproviders.AbstractDataProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractView<T extends AbstractDto> extends VerticalLayout {

    final AbstractDataProvider<T> dataProvider;
    private final TextField filterByName;
    final Button addBtn;
    private final Button deleteBtn;
    final Grid<T> grid;
    private final CrudService<T> service;

    public AbstractView(final AbstractDataProvider<T> dataProvider,
                        final CrudService<T> service, final Grid<T> grid) {
        this.dataProvider = dataProvider;
        this.grid = grid;
        this.service = service;

        this.filterByName = new TextField();
        filterByName.setPlaceholder("Filter by name...");
        filterByName.setValueChangeMode(ValueChangeMode.EAGER);

        filterByName.addValueChangeListener(e -> dataProvider.setFilter(e.getValue()));

        final Button clearFilterByNameBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterByNameBtn.addClickListener(e -> filterByName.clear());

        this.addBtn = new Button();
        addBtn.setIcon(VaadinIcon.PLUS.create());

        final Dialog deleteDialog = new Dialog(new H4("Are you sure?"));

        final Button deleteButton = new Button("Delete", new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(event -> {
            delete();
            deleteDialog.close();
        });

        deleteDialog.setWidth("400px");
        deleteDialog.add(deleteButton, new CancelButton(deleteDialog, "primary"));

        this.deleteBtn = new Button("Delete");
        deleteBtn.setEnabled(false);
        deleteBtn.addClickListener(event -> deleteDialog.open());

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.asMultiSelect().addSelectionListener(event -> {
            if (!event.getValue().isEmpty()) {
                deleteBtn.setEnabled(true);
            } else {
                deleteBtn.setEnabled(false);
            }
        });
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setMultiSort(true);
        grid.setDataProvider(dataProvider);
        grid.setSizeFull();
        grid.getStyle().set("margin", "auto");
        setupGrid(grid);
        grid.appendFooterRow().getCell(grid.getColumns().get(1)).setComponent(dataProvider.getFooterLabel());

        final HorizontalLayout panel = new HorizontalLayout(filterByName, clearFilterByNameBtn, addBtn, deleteBtn);

        add(panel);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    protected abstract void setupGrid(final Grid<T> grid);

    private void delete() {
        final List<T> dtoList = new ArrayList<>(grid.getSelectedItems());
        service.deleteAll(dtoList);
        dataProvider.refreshAll();
        grid.deselectAll();
    }
}
