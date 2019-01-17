package com.maks.assetaccounting.vaadin.registration;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.maks.assetaccounting.vaadin.user.UserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "sign-up", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Sign Up")
public class RegistrationView extends VerticalLayout {

    private final UserService userService;
    private final UserForm registrationForm;

    @Autowired
    public RegistrationView(final UserService userService) {
        this.userService = userService;
        this.registrationForm = new UserForm(userService);

        final Button registrationBtn = new Button("Sign up");
        registrationBtn.getElement().setAttribute("theme", "primary");
        registrationBtn.addClickListener(e -> {
            if (registrationForm.getBinder().validate().isOk()) {
                register();
            }
        });

        registrationForm.remove(registrationForm.getActive(), registrationForm.getRoles());
        registrationForm.makeCreateValidation();
        registrationForm.add(registrationBtn);
        registrationForm.setUserDto(new UserDto());

        setAlignItems(Alignment.CENTER);
        setSizeFull();
        add(new H3("User registration"), registrationForm);
    }

    private void register() {
        userService.create(registrationForm.getUserDto());
        this.getUI().ifPresent(ui -> ui.getPage()
                .executeJavaScript("location.assign('sign-in')"));
        registrationForm.setUserDto(new UserDto());
    }
}
