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
        binder.forField(companyName)
                .withValidator(name -> assetMain.getCompanyService()
                        .getByName(name) != null, "No exists such company")
                .bind(AssetDto::getCompanyName, AssetDto::setCompanyName);
        binder.forField(transferDate)
                .asRequired("Enter a date")
                .withConverter(new LocalDateZonedDateTimeConverter())
                .bind(AssetDto::getTransferDate, AssetDto::setTransferDate);
        binder.forField(cost)
                .asRequired("Enter a cost")
                .withConverter(new StringToDoubleConverter("Must enter a number"))
                .bind(AssetDto::getCost, AssetDto::setCost);
        binder.bindInstanceFields(this);
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
}
