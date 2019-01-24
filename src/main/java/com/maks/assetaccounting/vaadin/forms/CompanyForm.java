package com.maks.assetaccounting.vaadin.forms;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.company.CompanyService;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@Data
public class CompanyForm extends FormLayout {
    private CompanyDto companyDto;
    private final TextField name;
    private final Binder<CompanyDto> binder;

    @Autowired
    public CompanyForm(final CompanyService companyService) {
        this.name = new TextField("Company Name");
        name.setPlaceholder("Company name");

        this.binder = new Binder<>(CompanyDto.class);
        binder.forField(name)
                .asRequired("Company Name field is required")
                .withValidator(name -> name.matches("^.*\\S.*$"),
                        "Company Name must contain at least one non-whitespace character")
                .withValidator(name -> companyService.getByName(name) == null,
                        "Company already exists")
                .bind("name");

        setCompanyDto(null);

        add(name);
    }

    public void setCompanyDto(final CompanyDto companyDto) {
        this.companyDto = companyDto;
        binder.setBean(companyDto);
        name.focus();
    }

    public boolean isValid() {
        return this.binder.validate().isOk();
    }
}
