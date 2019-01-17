package com.maks.assetaccounting.vaadin.asset;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.vaadin.converter.LocalDateZonedDateTimeConverter;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;

public class AssetForm extends FormLayout {
    private final AssetMain assetMain;
    private AssetDto assetDto;
    private final TextField name = new TextField("Asset Name");
    private final DatePicker transferDate = new DatePicker("Transfer Date");
    private final TextField cost = new TextField("Asset Cost");
    private final TextField companyName = new TextField("Company Name");
    private final Binder<AssetDto> binder = new Binder<>(AssetDto.class);

    public AssetForm(final AssetMain assetMain) {
        this.assetMain = assetMain;
        this.init();
    }

    private void init() {
        binder.forField(name)
                .asRequired("Asset Name field is required")
                .withValidator(name -> name.matches("^.*\\S.*$"),
                        "Asset Name must contain at least one non-whitespace character")
                .bind("name");
        binder.forField(transferDate)
                .asRequired("Transfer Date field is required")
                .withConverter(new LocalDateZonedDateTimeConverter())
                .bind("transferDate");
        binder.forField(cost)
                .asRequired("Asset Cost field is required")
                .withConverter(new StringToDoubleConverter("Must enter a number"))
                .bind("cost");
        binder.forField(companyName)
                .asRequired("Company Name field is required")
                .withValidator(name -> assetMain.getCompanyService()
                        .getByName(name) != null, "No exists such company")
                .bind("companyName");
        add(name, transferDate, cost, companyName);
        setAssetDto(null);
    }

    public void setAssetDto(final AssetDto assetDto) {
        this.assetDto = assetDto;
        binder.setBean(assetDto);
        name.focus();
    }

    public void delete() {
        assetMain.getAssetService().delete(assetDto.getId());
        assetMain.updateList();
        setAssetDto(null);
    }

    public void save() {
        if (assetDto.getId() == null) {
            assetMain.getAssetService().create(assetDto);
        } else {
            assetMain.getAssetService().update(assetDto, assetDto.getId());
        }
        assetMain.updateList();
        setAssetDto(null);
    }

    public Binder<AssetDto> getBinder() {
        return binder;
    }
}
