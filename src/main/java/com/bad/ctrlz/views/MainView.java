package com.bad.ctrlz.views;

import com.bad.ctrlz.views.components.Navbar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Inicio")
@Route(value = "", layout = Navbar.class)
public class MainView extends VerticalLayout {

    public MainView() {
        // Título simple
        add("¡Bienvenido a la aplicación CtrlZ!");

        // Botón de ejemplo
        Button btn = new Button("Haz clic aquí");
        btn.addClickListener(e -> Notification.show("¡Has hecho clic!"));

        add(btn);
    }
}
