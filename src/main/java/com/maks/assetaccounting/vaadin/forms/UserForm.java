package com.maks.assetaccounting.vaadin.forms;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.entity.Role;
import com.maks.assetaccounting.service.user.UserService;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;

import java.util.Arrays;

@SpringComponent
@UIScope
@Data
public class UserForm extends FormLayout {
    private final UserService userService;
    private final Binder<UserDto> binder;
    private UserDto userDto;
    private final TextField username;
    private final PasswordField password;
    private final TextField email;
    private final TextField firstName;
    private final TextField lastName;
    private final Checkbox active;
    private final CheckboxGroup<Role> roles;

    public UserForm(final UserService userService) {
        this.userService = userService;

        this.userDto = new UserDto();

        this.username = new TextField();
        username.setPlaceholder("Username");

        this.password = new PasswordField();
        password.setPlaceholder("Password");

        this.email = new TextField();
        email.setPlaceholder("Email");

        this.firstName = new TextField();
        firstName.setPlaceholder("First Name");

        this.lastName = new TextField();
        lastName.setPlaceholder("Last Name");

        this.active = new Checkbox("Enable/disable user");

        this.roles = new CheckboxGroup<>();
        final ListDataProvider<Role> dataProvider = new ListDataProvider<>(Arrays.asList(Role.values()));
        roles.setDataProvider(dataProvider);

        binder = new Binder<>(UserDto.class);
        binder.forField(email)
                .asRequired("Email field is required")
                .withValidator(new EmailValidator("This doesn't look like a valid email address"))
                .bind("email");
        binder.bindInstanceFields(this);

        setUserDto(null);

        setWidth("400px");
        add(username, password, email, firstName, lastName, active, roles);
    }

    public void setUserDto(final UserDto userDto) {
        this.userDto = userDto;
        this.binder.setBean(userDto);
        this.username.focus();
    }

    public void makeCreateValidation() {
        binder.forField(username)
                .asRequired("Username field is required")
                .withValidator(name -> name.matches("^.*\\S.*$"),
                        "Username must contain at least one non-whitespace character")
                .withValidator(name -> userService.getByName(name) == null,
                        "User already exists")
                .bind("username");
        binder.forField(password)
                .asRequired("Password field is required")
                .withValidator(pass -> pass.matches("^.*\\S.*$"),
                        "Password must contain at least one non-whitespace character")
                .bind("password");
    }

    public void makeEditValidation() {
        binder.forField(username)
                .asRequired("Username field is required")
                .withValidator(name -> name.matches("^.*\\S.*$"),
                        "Username must contain at least one non-whitespace character")
                .bind("username");
    }

    public void makeChangePassValidation() {
        binder.forField(password)
                .asRequired("Password field is required")
                .withValidator(pass -> pass.matches("^.*\\S.*$"),
                        "Password must contain at least one non-whitespace character")
                .bind("password");
    }

    public boolean isValid() {
        return this.binder.validate().isOk();
    }
}
