package com.maks.assetaccounting.vaadin.forms;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.company.CompanyService;
import com.maks.assetaccounting.vaadin.converters.LocalDateZonedDateTimeConverter;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToDoubleConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;

@SpringComponent
@UIScope
@Data
public class AssetForm extends FormLayout {
    private AssetDto assetDto;
    private final TextField name;
    private final DatePicker transferDate;
    private final TextField cost;
    private final TextField companyName;
    private final Binder<AssetDto> binder;

    public AssetForm(final CompanyService companyService) {
        this.name = new TextField("Asset Name");
        name.setPlaceholder("Asset name");
        this.transferDate = new DatePicker("Transfer Date");
        transferDate.setPlaceholder("Transfer date");
        this.cost = new TextField("Asset Cost");
        cost.setPlaceholder("Cost");
        this.companyName = new TextField("Company Name");
        companyName.setPlaceholder("Company name");

        this.binder = new Binder<>(AssetDto.class);
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
                .withValidator(name -> companyService
                        .getByName(name) != null, "No exists such company")
                .bind("companyName");

        setAssetDto(null);

        add(name, transferDate, cost, companyName);
    }

    public void setAssetDto(final AssetDto assetDto) {
        this.assetDto = assetDto;
        binder.setBean(assetDto);
        name.focus();
    }

    public boolean isValid() {
        return this.binder.validate().isOk();
    }
}
