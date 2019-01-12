package com.maks.assetaccounting.vaadin.login;

import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Route(value = "sign-in", layout = AppLayoutClass.class)
public class LoginView extends VerticalLayout {

    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final Label label;
    private final TextField usernameTextField;
    private final PasswordField passwordField;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private HttpServletRequest req;

    LoginView() {
        this.label = new Label("Invalid username or password. Please try again");
        label.getStyle().set("color", "red");
        label.setVisible(false);

        this.usernameTextField = new TextField();
        usernameTextField.setPlaceholder("Username");
        usernameTextField.setAutofocus(true);

        this.passwordField = new PasswordField();
        passwordField.setPlaceholder("Password");
        passwordField.addKeyDownListener(Key.ENTER, event -> authenticateAndNavigate());

        final Button signInBtn = new Button("Sign in");
        signInBtn.getElement().setAttribute("theme", "primary");
        signInBtn.addClickListener(event -> authenticateAndNavigate());

        final FormLayout formLayout = new FormLayout();
        formLayout.add(usernameTextField, passwordField, signInBtn);

        setAlignItems(Alignment.CENTER);
        add(new H3("Please sign in"), formLayout, label);
    }

    private void authenticateAndNavigate() {
        final UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
                usernameTextField.getValue(), passwordField.getValue());
        try {
            final Authentication auth = authManager.authenticate(authReq);
            final SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);

            /*Navigate to the requested page:
            This is to redirect a user back to the originally requested URL*/
            final HttpSession session = req.getSession(false);
            final DefaultSavedRequest savedRequest = (DefaultSavedRequest) session
                    .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
            final String requestedURI = savedRequest != null ? savedRequest.getRequestURI() : "";
            this.getUI().ifPresent(ui -> ui.getPage()
                    .executeJavaScript("location.assign('" + StringUtils
                            .removeStart(requestedURI, contextPath + "/") + "')"));
        } catch (AuthenticationException e) {
            label.setVisible(true);
        }
    }
}
