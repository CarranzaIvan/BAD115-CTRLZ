package com.bad.ctrlz.views.Seguridad;

import com.bad.ctrlz.views.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


@PageTitle("Inicio | Usuarios")
@Route(value = "inicio-administracion-usuarios")
public class ManagementUserView extends VerticalLayout {

    public ManagementUserView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Fondo degradado
        getStyle().set("background", "linear-gradient(135deg, #2c3e50 0%, #4a4a4a 50%, #666666 100%)");
        getStyle().set("min-height", "100vh");

        // Botones
        Button btnVolver = new Button("Volver", new Icon(VaadinIcon.ARROW_LEFT));
        btnVolver.getStyle().set("background-color", "#3B82F6").set("color", "white");
        btnVolver.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().getHistory().back()));

        Button btnInicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
        btnInicio.getStyle().set("background-color", "#2563EB").set("color", "white");
        btnInicio.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("inicio")));

        HorizontalLayout navegacion = new HorizontalLayout(btnVolver, btnInicio);
        navegacion.setSpacing(true);
        navegacion.setAlignItems(FlexComponent.Alignment.CENTER);
        add(navegacion); // 👈 Esto antes del header

        createHeader();
        createDashboard();

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
        navLinks.setAlignItems(FlexComponent.Alignment.END);
        navLinks.getStyle().set("margin-right", "20px");
        
        add(navLinks);
    }

    private void createHeader() {
        // Main title with icon
        Icon userIcon = VaadinIcon.USERS.create();
        userIcon.setSize("2em");
        userIcon.setColor("white");
        
        H1 title = new H1("Sistema de Gestión de Usuarios");
        title.getStyle().set("color", "white");
        title.getStyle().set("margin", "0");
        title.getStyle().set("font-weight", "bold");
        
        HorizontalLayout titleLayout = new HorizontalLayout(userIcon, title);
        titleLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        titleLayout.setSpacing(true);
        
        // Subtitle
        Span subtitle = new Span("Selecciona el módulo que deseas gestionar");
        subtitle.getStyle().set("color", "#b0b0b0");
        subtitle.getStyle().set("font-size", "1.1em");
        subtitle.getStyle().set("margin-top", "0.5em");
        
        VerticalLayout headerLayout = new VerticalLayout(titleLayout, subtitle);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        headerLayout.setSpacing(false);
        headerLayout.getStyle().set("margin-bottom", "2em");
        
        add(headerLayout);
    }

    private void createDashboard() {
        HorizontalLayout cardsLayout = new HorizontalLayout();
        cardsLayout.setSpacing(true);
        cardsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        cardsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        
        // Card 1: Gestión de Estados
        Div stateCard = createModuleCard(
            VaadinIcon.USER_CHECK,
            "Gestión de Estados",
            "Activa y desactiva usuarios del sistema",
            new String[]{"Activar usuarios", "Desactivar usuarios", "Ver estadísticas de usuarios"},
            new String[]{"#4CAF50", "#6C757D", "#2196F3"},
            UserStatusView.class
        );
        
        // Card 2: Gestión de Desbloqueos
        Div unlockCard = createModuleCard(
            VaadinIcon.SHIELD,
            "Gestión de Desbloqueos",
            "Desbloquea usuarios y gestiona peticiones por correo",
            new String[]{"Ver usuarios bloqueados", "Gestionar solicitudes de desbloqueo", "Enviar correos de desbloqueo"},
            new String[]{"#F44336", "#FF9800", "#2196F3"},
            UserUnblockView.class
        );
        
        cardsLayout.add(stateCard, unlockCard);
        add(cardsLayout);
    }

    private Div createModuleCard(VaadinIcon iconType, String title, String description, 
                                String[] features, String[] colors, Class<? extends Component> navigationTarget) {
        Div card = new Div();
        card.addClassName("module-card");
        
        // Card styling
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("padding", "2em");
        card.getStyle().set("margin", "1em");
        card.getStyle().set("width", "350px");
        card.getStyle().set("height", "280px");
        card.getStyle().set("box-shadow", "0 8px 24px rgba(0, 0, 0, 0.15)");
        card.getStyle().set("cursor", "pointer");
        card.getStyle().set("transition", "all 0.3s ease");
        card.getStyle().set("position", "relative");
        card.getStyle().set("overflow", "hidden");
        
        // Hover effect
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle().set("transform", "translateY(-5px)");
            card.getStyle().set("box-shadow", "0 12px 32px rgba(0, 0, 0, 0.2)");
        });
        
        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle().set("transform", "translateY(0)");
            card.getStyle().set("box-shadow", "0 8px 24px rgba(0, 0, 0, 0.15)");
        });
        
        // Icon
        Icon icon = iconType.create();
        icon.setSize("3em");
        icon.getStyle().set("color", "#4A90E2");
        icon.getStyle().set("margin-bottom", "1em");
        
        // Title
        H2 cardTitle = new H2(title);
        cardTitle.getStyle().set("color", "#2c3e50");
        cardTitle.getStyle().set("margin", "0 0 0.5em 0");
        cardTitle.getStyle().set("font-size", "1.5em");
        cardTitle.getStyle().set("font-weight", "600");
        
        // Description
        Span cardDescription = new Span(description);
        cardDescription.getStyle().set("color", "#6c757d");
        cardDescription.getStyle().set("font-size", "0.95em");
        cardDescription.getStyle().set("line-height", "1.4");
        cardDescription.getStyle().set("margin-bottom", "1.5em");
        cardDescription.getStyle().set("display", "block");
        
        // Features list
        VerticalLayout featuresList = new VerticalLayout();
        featuresList.setSpacing(false);
        featuresList.setPadding(false);
        featuresList.getStyle().set("margin-top", "1em");
        
        for (int i = 0; i < features.length; i++) {
            HorizontalLayout featureItem = new HorizontalLayout();
            featureItem.setAlignItems(FlexComponent.Alignment.CENTER);
            featureItem.setSpacing(true);
            
            // Colored dot
            Div dot = new Div();
            dot.getStyle().set("width", "8px");
            dot.getStyle().set("height", "8px");
            dot.getStyle().set("border-radius", "50%");
            dot.getStyle().set("background-color", colors[i]);
            dot.getStyle().set("flex-shrink", "0");
            
            Span featureText = new Span(features[i]);
            featureText.getStyle().set("color", "#495057");
            featureText.getStyle().set("font-size", "0.9em");
            
            featureItem.add(dot, featureText);
            featuresList.add(featureItem);
        }
        
        // Make card clickable
        RouterLink cardLink = new RouterLink();
        cardLink.setRoute(navigationTarget);
        cardLink.getStyle().set("text-decoration", "none");
        cardLink.getStyle().set("color", "inherit");
        
        // Icon container to center only the icon
        HorizontalLayout iconContainer = new HorizontalLayout(icon);
        iconContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        iconContainer.setWidthFull();
        iconContainer.getStyle().set("margin-bottom", "1em");
        
        VerticalLayout cardContent = new VerticalLayout(iconContainer, cardTitle, cardDescription, featuresList);
        cardContent.setAlignItems(FlexComponent.Alignment.START);
        cardContent.setSpacing(false);
        cardContent.setPadding(false);
        
        cardLink.add(cardContent);
        card.add(cardLink);
        
        return card;
    }
}