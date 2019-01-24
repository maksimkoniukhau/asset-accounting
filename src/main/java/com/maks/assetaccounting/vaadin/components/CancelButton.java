package com.maks.assetaccounting.vaadin.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class CancelButton extends Button {

    public CancelButton(final Dialog dialog, final String theme) {
        if (theme != null) {
            getElement().setAttribute("theme", theme);
        }
        setText("Cancel");
        setIcon(new Icon(VaadinIcon.CLOSE));
        addClickListener(event -> dialog.close());
    }
}
