package com.bad.ctrlz.views.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon; 
import com.vaadin.flow.router.RouterLink;
import com.bad.ctrlz.views.MainView;

public class Navbar extends AppLayout {


    public Navbar() {
        // Título de la app
        H1 logo = new H1("Sistema de Encuestas");
        logo.getStyle()
            .set("font-size", "var(--lumo-font-size-l)")
            .set("margin", "0 0 0 20px")
            .set("color", "#ffffff ");

        // Ícono de casa
        Icon casaIcono = VaadinIcon.HOME.create();
        casaIcono.getStyle()
            .set("margin-right", "5px")
            .set("color", "#ffffff ");

        // Enlaces de navegación
        RouterLink inicioLink = new RouterLink("", MainView.class);
        inicioLink.add(casaIcono, new com.vaadin.flow.component.html.Span("Inicio"));
        inicioLink.getStyle()
            .set("font-size", "16px")       // Aumenta el tamaño de fuente
            .set("color", "#ffffff ")          // Establece el color del texto
            .set("font-weight", "bold");    // (Opcional) Negrita para destacarlo más

        
        // Layout de navegación alineado a la derecha
        HorizontalLayout navLinks = new HorizontalLayout(inicioLink);
        navLinks.setSpacing(true);
        navLinks.setAlignItems(FlexComponent.Alignment.CENTER);
        navLinks.getStyle().set("margin-right", "20px");

        // Contenedor principal de la barra
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setWidthFull();
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN); // Logo a la izquierda, enlaces a la derecha
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.getStyle()
            .set("background-color", "#2D3741") // azul oscuro
            .set("padding", "10px 0"); // espacio vertical
        topBar.add(logo, navLinks);

        addToNavbar(topBar);
        }
}

