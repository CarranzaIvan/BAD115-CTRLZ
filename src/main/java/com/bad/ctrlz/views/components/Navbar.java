package com.bad.ctrlz.views.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
// import com.vaadin.flow.component.html.Nav;
// import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
// import com.vaadin.flow.router.RouterLink;
// import com.bad.ctrlz.views.MainView;

public class Navbar extends AppLayout {

    public Navbar() {
        // Título de la app
        H1 logo = new H1("Mi App");
        logo.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)")
                .set("margin", "0")
                .set("position", "absolute");

        // Enlaces de navegación
        // RouterLink inicioLink = new RouterLink("Inicio", MainView.class);
        // RouterLink otraVista = new RouterLink("Otra Sección", OtraVista.class); // Usa esta línea cuando tengas la clase OtraVista
        // RouterLink otraVista = new RouterLink("Otra Sección", MainView.class); // Temporal, hasta que tengas OtraVista

        // Barra horizontal de navegación
        // HorizontalLayout navLinks = new HorizontalLayout(inicioLink, otraVista);
        // navLinks.setSpacing(true);
        // navLinks.setPadding(true);

        // Contenedor de navegación (opcional, si deseas estilizar con <nav>)
        //Nav nav = new Nav(navLinks);

        // Agregar elementos a la barra superior
        addToNavbar(logo);
    }
}

