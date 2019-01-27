package com.maks.assetaccounting.vaadin.views;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import com.maks.assetaccounting.vaadin.components.AppLayoutClass;
import com.maks.assetaccounting.vaadin.components.CancelButton;
import com.maks.assetaccounting.vaadin.dataproviders.UserDataProvider;
import com.maks.assetaccounting.vaadin.forms.UserForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Route(value = "users", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Users")
@Secured("ROLE_ADMIN")
public class UserView extends AbstractView<UserDto> {
    private final UserService userService;
    private final UserForm createUserForm;
    private final UserForm editUserForm;
    private final UserForm changePasswordForm;
    private final Button changePasswordBtn;
    private final Dialog editDialog;
    private UserDto changePassUserDto;

    @Autowired
    public UserView(final UserService userService, final UserDataProvider userDataProvider) {
        super(userDataProvider, userService, new Grid<>());

        this.userService = userService;
        this.createUserForm = new UserForm(userService);
        this.editUserForm = new UserForm(userService);
        this.changePasswordForm = new UserForm(userService);

        createUserForm.remove(createUserForm.getActive(), createUserForm.getRoles());
        createUserForm.makeCreateValidation();

        editUserForm.remove(editUserForm.getPassword());
        editUserForm.makeEditValidation();
        editUserForm.getUsername().setLabel("Username");
        editUserForm.getEmail().setLabel("Email");
        editUserForm.getFirstName().setLabel("First Name");
        editUserForm.getLastName().setLabel("Last Name");
        editUserForm.getRoles().setLabel("Roles");

        changePasswordForm.removeAll();
        changePasswordForm.makeChangePassValidation();
        changePasswordForm.add(changePasswordForm.getPassword());
        changePasswordForm.getPassword().setLabel("Password");
        changePasswordForm.getPassword().setPlaceholder("Enter new password");

        final Dialog createDialog = new Dialog(new H4("Create user"));
        editDialog = new Dialog(new H4("Edit user"));
        final Dialog changePasswordDialog = new Dialog();

        final Button createButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        createButton.getElement().setAttribute("theme", "primary");
        createButton.addClickListener(event -> {
            if (createUserForm.isValid()) {
                create();
                createDialog.close();
            }
        });

        final Button editButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        editButton.getElement().setAttribute("theme", "primary");
        editButton.addClickListener(event -> {
            if (editUserForm.isValid()) {
                update();
                editDialog.close();
            }
        });

        final Button changePassBtn = new Button("Change", new Icon(VaadinIcon.ENVELOPE_OPEN));
        changePassBtn.getElement().setAttribute("theme", "primary");
        changePassBtn.addClickListener(event -> {
            if (changePasswordForm.isValid()) {
                changePassword();
                changePasswordDialog.close();
            }
        });

        final Button cancelChangePassBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelChangePassBtn.addClickListener(event -> changePasswordDialog.close());

        createDialog.setWidth("400px");
        createDialog.add(createUserForm, createButton, new CancelButton(createDialog, null));

        editDialog.setWidth("400px");
        editDialog.add(editUserForm, editButton, new CancelButton(editDialog, null));

        changePasswordDialog.setWidth("400px");

        addBtn.setText("New user");
        addBtn.addClickListener(e -> {
            createUserForm.setUserDto(new UserDto());
            createDialog.open();
        });

        final H4 changePassText = new H4();

        this.changePasswordBtn = new Button("Change Password");
        changePasswordBtn.setEnabled(false);
        changePasswordBtn.addClickListener(event -> {
            final UserDto userDto = grid.getSelectedItems().stream().findFirst().orElse(null);
            if (userDto != null) {
                changePassUserDto = userDto;
                changePasswordForm.setUserDto(userDto);
                changePassText.setText("Change password for " + userDto.getUsername());
                changePasswordForm.getPassword().focus();
                changePasswordDialog.add(changePassText, changePasswordForm, changePassBtn, cancelChangePassBtn);
                changePasswordDialog.open();
            }
        });

        final HorizontalLayout buttons = new HorizontalLayout(changePasswordBtn);

        add(buttons, grid);
    }


    @Override
    protected void setupGrid(final Grid<UserDto> grid) {
        grid.addColumn(UserDto::getId).setHeader("User Id").setVisible(false);
        grid.addColumn(UserDto::getUsername).setSortProperty("username").setHeader("Username");
        grid.addColumn(UserDto::getEmail).setSortProperty("email").setHeader("Email");
        grid.addColumn(UserDto::getFirstName).setSortProperty("firstName").setHeader("First Name");
        grid.addColumn(UserDto::getLastName).setSortProperty("lastName").setHeader("Last Name");
        grid.addColumn(userDto -> userDto.getRoles().toString().replaceAll("ROLE_", ""))
                .setSortProperty("roles").setHeader("Roles");
        grid.addColumn(userDto -> userDto.isActive() ? "Enabled" : "Disabled")
                .setSortProperty("active").setHeader("Activity").setWidth("90px").setFlexGrow(0);
        grid.addComponentColumn(userDto -> {
            final Button edit = new Button();
            edit.setIcon(VaadinIcon.EDIT.create());
            edit.addClickListener(event -> {
                editUserForm.setUserDto(userDto);
                editDialog.open();
            });
            return edit;
        }).setWidth("80px").setFlexGrow(0);

        grid.asMultiSelect().addSelectionListener(event -> {
            if (event.getValue().size() == 1) {
                changePasswordBtn.setEnabled(true);
            } else {
                changePasswordBtn.setEnabled(false);
            }
        });

        grid.setWidth("1200px");
    }

    private void update() {
        UserDto userDto = userService.update(editUserForm.getUserDto(), editUserForm.getUserDto().getId());
        dataProvider.refreshItem(userDto);
        editUserForm.setUserDto(null);
    }

    private void create() {
        userService.create(createUserForm.getUserDto());
        dataProvider.refreshAll();
        createUserForm.setUserDto(null);
    }

    private void changePassword() {
        userService.changeUserPassword(changePassUserDto, changePassUserDto.getId(), changePassUserDto.getPassword());
        dataProvider.refreshAll();
        changePasswordForm.setUserDto(null);
        grid.deselectAll();
    }
}
