package com.maks.assetaccounting.vaadin.user;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import com.maks.assetaccounting.vaadin.AppLayoutClass;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.ArrayList;
import java.util.List;

@Route(value = "users", layout = AppLayoutClass.class)
@PageTitle("Asset Accounting/Users")
@Secured("ROLE_ADMIN")
public class UserView extends VerticalLayout {
    private final UserService userService;
    private final UserForm createUserForm;
    private final UserForm editUserForm;
    private final UserForm changePasswordForm;
    private final TextField filterByUsername;
    private final Button deleteUsersBtn;
    private final Grid<UserDto> grid;

    @Autowired
    public UserView(final UserService userService) {
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
        final Dialog editDialog = new Dialog(new H4("Edit user"));
        final Dialog deleteDialog = new Dialog(new H4("Are you sure?"));
        final Dialog changePasswordDialog = new Dialog();

        final Button createButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        createButton.getElement().setAttribute("theme", "primary");
        createButton.addClickListener(event -> {
            if (createUserForm.getBinder().validate().isOk()) {
                create();
                createDialog.close();
            }
        });

        final Button editButton = new Button("Save", new Icon(VaadinIcon.ENVELOPE_OPEN));
        editButton.getElement().setAttribute("theme", "primary");
        editButton.addClickListener(event -> {
            if (editUserForm.getBinder().validate().isOk()) {
                update();
                editDialog.close();
            }
        });

        final Button deleteButton = new Button("Delete", new Icon(VaadinIcon.TRASH));
        deleteButton.addClickListener(event -> {
            delete();
            deleteDialog.close();
        });

        final Button changePassBtn = new Button("Change", new Icon(VaadinIcon.ENVELOPE_OPEN));
        changePassBtn.getElement().setAttribute("theme", "primary");
        changePassBtn.addClickListener(event -> {
            if (changePasswordForm.getBinder().validate().isOk()) {
                changePassword();
                changePasswordDialog.close();
            }
        });

        final Button cancelCreateBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelCreateBtn.addClickListener(event -> createDialog.close());

        final Button cancelEditBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelEditBtn.addClickListener(event -> editDialog.close());

        final Button cancelDelBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelDelBtn.getElement().setAttribute("theme", "primary");
        cancelDelBtn.addClickListener(event -> deleteDialog.close());

        final Button cancelChangePassBtn = new Button("Cancel", new Icon(VaadinIcon.CLOSE));
        cancelChangePassBtn.addClickListener(event -> changePasswordDialog.close());

        createDialog.setWidth("400px");
        createDialog.add(createUserForm, createButton, cancelCreateBtn);

        editDialog.setWidth("400px");
        editDialog.add(editUserForm, editButton, cancelEditBtn);

        deleteDialog.setWidth("400px");
        deleteDialog.add(deleteButton, cancelDelBtn);

        changePasswordDialog.setWidth("400px");

        this.filterByUsername = new TextField();
        filterByUsername.setPlaceholder("Filter by Username...");
        filterByUsername.addValueChangeListener(e -> updateList());
        final Button clearFilterByUsernameBtn = new Button(new Icon(VaadinIcon.CLOSE_CIRCLE));
        clearFilterByUsernameBtn.addClickListener(e -> filterByUsername.clear());
        final HorizontalLayout filtering = new HorizontalLayout(filterByUsername, clearFilterByUsernameBtn);

        final Button addUserBtn = new Button("Create user");
        addUserBtn.addClickListener(e -> {
            createUserForm.setUserDto(new UserDto());
            createDialog.open();
        });

        this.deleteUsersBtn = new Button("Delete user(s)");
        deleteUsersBtn.setEnabled(false);
        deleteUsersBtn.addClickListener(event -> {
            deleteDialog.open();
        });

        final HorizontalLayout addDeleteButtons = new HorizontalLayout(addUserBtn, deleteUsersBtn);
        final H4 changePassText = new H4();

        this.grid = new Grid<>();
        grid.setSizeFull();
        grid.addColumn(UserDto::getId).setHeader("User Id").setVisible(false);
        grid.addColumn(UserDto::getUsername).setSortable(true).setHeader("Username");
        grid.addColumn(UserDto::getEmail).setSortable(true).setHeader("Email");
        grid.addColumn(UserDto::getFirstName).setSortable(true).setHeader("First Name");
        grid.addColumn(UserDto::getLastName).setSortable(true).setHeader("Last Name");
        grid.addColumn(active -> active.isActive() ? "Enabled" : "Disabled").setSortable(true)
                .setHeader("Activity");
        grid.addColumn(u -> u.getRoles().toString().replaceAll("ROLE_", ""))
                .setSortable(true).setHeader("Roles");
        grid.addComponentColumn(userDto -> {
            final Button edit = new Button("Edit");
            edit.addClickListener(event -> {
                editUserForm.setUserDto(userDto);
                editDialog.open();
            });
            return edit;
        });
        grid.addComponentColumn(userDto -> {
            final Button changePassword = new Button("Change password");
            changePassword.addClickListener(event -> {
                changePassText.setText("Change password for " + userDto.getUsername());
                changePasswordForm.setUserDto(userDto);
                changePasswordForm.getPassword().focus();
                changePasswordDialog.add(changePassText, changePasswordForm, changePassBtn, cancelChangePassBtn);
                changePasswordDialog.open();
            });
            return changePassword;
        });

        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.asMultiSelect().addSelectionListener(event -> {
            if (!event.getValue().isEmpty()) {
                deleteUsersBtn.setEnabled(true);
            } else {
                deleteUsersBtn.setEnabled(false);
            }
        });

        add(filtering, addDeleteButtons, grid);
        setHeight("90vh");
        updateList();
    }

    private void updateList() {
        final String filterUsername = filterByUsername.getValue();
        if (filterUsername != null && !filterUsername.isEmpty()) {
            final UserDto filterUsernameDto = userService.getByName(filterUsername);
            if (filterUsernameDto != null) grid.setItems(filterUsernameDto);
        } else {
            final List<UserDto> userDtos = userService.getAll();
            if (userDtos != null)
                grid.setItems(userDtos);
        }
    }

    private void delete() {
        List<UserDto> userDtoList = new ArrayList<>(grid.getSelectedItems());
        userService.deleteAll(userDtoList);
        deleteUsersBtn.setEnabled(false);
        updateList();
    }

    private void update() {
        userService.update(editUserForm.getUserDto(), editUserForm.getUserDto().getId());
        updateList();
        editUserForm.setUserDto(null);
    }

    private void create() {
        userService.create(createUserForm.getUserDto());
        updateList();
        createUserForm.setUserDto(null);
    }

    private void changePassword() {
        userService.changeUserPassword(changePasswordForm.getUserDto(), changePasswordForm.getUserDto().getId(),
                changePasswordForm.getUserDto().getPassword());
        updateList();
        changePasswordForm.setUserDto(null);
    }
}