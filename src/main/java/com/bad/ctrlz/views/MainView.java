package com.bad.ctrlz.views;

import com.bad.ctrlz.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("Inicio")
@Route(value = "inicio", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    public MainView() {
        setSizeFull();
        setPadding(true);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 titulo = new H2("¡Bienvenido a la aplicación CtrlZ!");
        titulo.getStyle().set("margin-bottom", "50px");
        add(titulo);

        FlexLayout tarjetasLayout = new FlexLayout();
        tarjetasLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        tarjetasLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        tarjetasLayout.getStyle().set("gap", "50px");

        tarjetasLayout.add(
                crearTarjeta("Gestión de Usuarios", "Administrar los usuarios del sistema", "inicio-administracion-usuarios", VaadinIcon.USERS),
                crearTarjeta("Gestión de Encuestas", "Crear y gestionar encuestas", "dashboard-encuestas", VaadinIcon.CLIPBOARD_TEXT),
                crearTarjeta("Ver Respuestas", "Visualizar las respuestas de las encuestas", "dashboard-respuestas", VaadinIcon.BAR_CHART)
        );

        add(tarjetasLayout);
    }

    private Div crearTarjeta(String titulo, String descripcion, String ruta, VaadinIcon icono) {
        Div card = new Div();
        card.getStyle().set("width", "300px");
        card.getStyle().set("height", "360px");
        card.addClassNames(
                LumoUtility.Background.CONTRAST_5,
                LumoUtility.Padding.XLARGE,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BoxShadow.MEDIUM,
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.JustifyContent.CENTER
        );

        // Ícono perfectamente centrado
        Div iconWrapper = new Div();
        iconWrapper.getStyle().set("display", "flex");
        iconWrapper.getStyle().set("justify-content", "center");
        iconWrapper.getStyle().set("align-items", "center");
        iconWrapper.getStyle().set("margin-bottom", "20px");

        Icon iconoPrincipal = icono.create();
        iconoPrincipal.setSize("70px");
        iconoPrincipal.getStyle().set("color", "#0d6efd");

        iconWrapper.add(iconoPrincipal);

        H2 tituloElemento = new H2(titulo);
        tituloElemento.getStyle().set("font-size", "1.4em");
        tituloElemento.getStyle().set("text-align", "center");
        tituloElemento.getStyle().set("margin-bottom", "15px");

        Div descripcionElemento = new Div();
        descripcionElemento.setText(descripcion);
        descripcionElemento.getStyle().set("text-align", "center");
        descripcionElemento.getStyle().set("font-size", "1em");
        descripcionElemento.getStyle().set("margin-bottom", "25px");

        // Botón perfectamente centrado
        Div botonWrapper = new Div();
        botonWrapper.getStyle().set("display", "flex");
        botonWrapper.getStyle().set("justify-content", "center");
        botonWrapper.getStyle().set("width", "100%");

        Button boton = new Button("Ir");
        boton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        boton.addClickListener(e -> UI.getCurrent().navigate(ruta));

        botonWrapper.add(boton);

        card.add(iconWrapper, tituloElemento, descripcionElemento, botonWrapper);
        return card;
    }
}
