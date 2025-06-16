package com.bad.ctrlz.views;

import com.bad.ctrlz.views.components.Navbar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Inicio")
@Route(value = "inicio", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    public MainView() {
        add("¡Bienvenido a la aplicación CtrlZ!");
    }
}
