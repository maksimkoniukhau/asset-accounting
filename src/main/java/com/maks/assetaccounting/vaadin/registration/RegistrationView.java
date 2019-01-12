package com.maks.assetaccounting.vaadin.registration;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "sign-up", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Sign Up")
public class RegistrationView extends VerticalLayout {
    private final UserService userService;
    private final UserDto userDto;
    private final TextField username;
    private final PasswordField password;

    @Autowired
    public RegistrationView(final UserService userService) {
        this.userService = userService;

        this.userDto = new UserDto();

        this.username = new TextField();
        username.setPlaceholder("Username");
        username.setAutofocus(true);

        this.password = new PasswordField();
        password.setPlaceholder("Password");
        password.addKeyDownListener(Key.ENTER, event -> register());

        final Button registrationBtn = new Button("Sign up");
        registrationBtn.getElement().setAttribute("theme", "primary");
        registrationBtn.addClickListener(e -> register());

        final FormLayout registrationForm = new FormLayout();
        registrationForm.add(username, password, registrationBtn);

        final Binder<UserDto> binder = new Binder<>(UserDto.class);
        binder.bindInstanceFields(this);
        binder.setBean(userDto);

        setAlignItems(Alignment.CENTER);
        add(new H3("User registration"), registrationForm);
    }

    private void register() {
        userService.create(userDto);
        this.getUI().ifPresent(ui -> ui.getPage()
                .executeJavaScript("location.assign('sign-in')"));
    }
}
