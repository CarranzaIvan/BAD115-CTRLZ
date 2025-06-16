package com.bad.ctrlz.views.Seguridad;

import com.bad.ctrlz.model.Usuario;
import com.bad.ctrlz.service.UsuarioService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Bloqueos | Usuarios")
@Route(value = "bloqueos-usuarios")
public class UserUnblockView extends VerticalLayout {

        @Autowired
        private UsuarioService usuarioService;

        private List<Usuario> usuariosBloqueados;
        private List<Usuario> solicitudesDesbloqueo;
        private Grid<Usuario> blockedGrid;
        private Grid<Usuario> requestsGrid;

        public UserUnblockView(UsuarioService usuarioService) {
                this.usuarioService = usuarioService;
                setSpacing(false);
                setPadding(false);
                setSizeFull();

                // Remover padding del contenedor principal para evitar desbordamiento
                getStyle().set("overflow-x", "hidden");

                // Cargar datos iniciales
                cargarDatos();

                // Crear contenido principal
                add(createHeader());
                add(createMainContent());
        }

        private void cargarDatos() {
                usuariosBloqueados = usuarioService.obtenerListadoBloqueados();
                solicitudesDesbloqueo = usuarioService.obtenerListadoSolicitud();
        }

        private void actualizarDatos() {
                cargarDatos();

                // Actualizar grid de usuarios bloqueados
                if (blockedGrid != null) {
                        blockedGrid.setItems(usuariosBloqueados);
                }

                // Actualizar grid de solicitudes
                if (requestsGrid != null) {
                        requestsGrid.setItems(solicitudesDesbloqueo);
                }
        }

        private Component createHeader() {
                HorizontalLayout header = new HorizontalLayout();
                header.setWidthFull();
                header.setJustifyContentMode(JustifyContentMode.START);
                header.setAlignItems(Alignment.CENTER);
                header.setSpacing(true);
                header.setPadding(false);

                // Estilo mejorado del encabezado
                header.getStyle()
                                .set("background", "#2d3441")
                                .set("border-bottom", "1px solid #4a5568")
                                .set("padding", "16px 24px")
                                .set("margin", "0")
                                .set("border-radius", "0");

                // Botón de regreso estilizado
                Button backButton = new Button(VaadinIcon.ARROW_LEFT.create());
                backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                backButton.getStyle()
                                .set("color", "#e2e8f0")
                                .set("background", "transparent")
                                .set("border", "1px solid #4a5568")
                                .set("border-radius", "6px")
                                .set("padding", "8px 12px")
                                .set("font-size", "14px")
                                .set("transition", "all 0.3s ease");
                backButton.addClickListener(e -> UI.getCurrent().navigate("inicio-administracion-usuarios"));

                // Ícono del título
                Icon userIcon = VaadinIcon.USER.create();
                userIcon.setSize("20px");
                userIcon.getStyle()
                                .set("color", "#e2e8f0")
                                .set("margin-left", "16px");

                // Título
                H2 title = new H2("Gestión de Estados de Usuario");
                title.addClassNames(LumoUtility.Margin.NONE);
                title.getStyle()
                                .set("color", "#ffffff")
                                .set("font-weight", "600")
                                .set("font-size", "20px")
                                .set("margin-left", "8px");

                header.add(backButton, userIcon, title);
                return header;
        }

        private Component createMainContent() {
                VerticalLayout mainContent = new VerticalLayout();
                mainContent.setWidthFull();
                mainContent.setSizeFull();
                mainContent.setPadding(true);
                mainContent.setSpacing(true);

                // Contenedor con ancho máximo para evitar desbordamiento
                mainContent.getStyle()
                                .set("max-width", "1400px")
                                .set("margin", "0 auto")
                                .set("padding", "24px");
                VerticalLayout blockedSection = createBlockedUsersSection();
                VerticalLayout requestsSection = createUnblockRequestsSection();

                // Dar más espacio a una sección específica (opcional)
                mainContent.setFlexGrow(1, blockedSection); // La sección de bloqueados toma más espacio
                mainContent.setFlexGrow(1, requestsSection); // Ambas secciones toman el mismo espacio

                mainContent.add(
                                createStatsCards(),
                                blockedSection,
                                requestsSection);

                return mainContent;
        }

        private HorizontalLayout createStatsCards() {
                HorizontalLayout statsContainer = new HorizontalLayout();
                statsContainer.setWidthFull();
                statsContainer.setPadding(false);
                statsContainer.setSpacing(false);

                // Configuración mejorada para responsive y alineación
                statsContainer.getStyle()
                                .set("margin-bottom", "30px")
                                .set("gap", "20px")
                                .set("display", "flex")
                                .set("flex-wrap", "wrap")
                                .set("justify-content", "space-between")
                                .set("align-items", "stretch");

                // Datos de las cartas
                String bloqueados = Integer.toString(usuariosBloqueados.size());
                String solicitudes = Integer.toString(solicitudesDesbloqueo.size());
                String activos = Integer.toString(usuarioService.obtenerActivos());

                // Crear las tarjetas
                Div blockedCard = createStatsCard(bloqueados, "Usuarios Bloqueados", "#e53e3e", VaadinIcon.LOCK);
                Div pendingCard = createStatsCard(solicitudes, "Solicitudes Pendientes", "#ff8c00", VaadinIcon.CLOCK);
                Div approvedCard = createStatsCard(activos, "Usuarios Activos", "#38a169", VaadinIcon.CHECK_CIRCLE);

                statsContainer.add(blockedCard, pendingCard, approvedCard);
                return statsContainer;
        }

        private Div createStatsCard(String number, String label, String color, VaadinIcon icon) {
                Div card = new Div();

                // Configuración mejorada para tarjetas uniformes
                card.getStyle()
                                .set("background", "#ffffff")
                                .set("border-radius", "12px")
                                .set("padding", "24px")
                                .set("box-shadow", "0 4px 6px rgba(0,0,0,0.07)")
                                .set("border", "1px solid #e2e8f0")
                                .set("flex", "1")
                                .set("min-width", "280px")
                                .set("transition", "all 0.3s ease")
                                .set("cursor", "default");

                // Hover effects mejorados
                card.getElement().addEventListener("mouseenter", e -> card.getStyle()
                                .set("transform", "translateY(-2px)")
                                .set("box-shadow", "0 8px 25px rgba(0,0,0,0.12)"));
                card.getElement().addEventListener("mouseleave", e -> card.getStyle()
                                .set("transform", "translateY(0)")
                                .set("box-shadow", "0 4px 6px rgba(0,0,0,0.07)"));

                // Layout interno mejorado
                HorizontalLayout content = new HorizontalLayout();
                content.setWidthFull();
                content.setAlignItems(Alignment.CENTER);
                content.setJustifyContentMode(JustifyContentMode.BETWEEN);
                content.setPadding(false);
                content.setMargin(false);

                // Contenido de texto
                VerticalLayout textContent = new VerticalLayout();
                textContent.setSpacing(false);
                textContent.setPadding(false);
                textContent.setMargin(false);
                textContent.getStyle().set("gap", "8px");

                Span numberSpan = new Span(number);
                numberSpan.getStyle()
                                .set("font-size", "32px")
                                .set("font-weight", "700")
                                .set("color", color)
                                .set("line-height", "1");

                Span labelSpan = new Span(label);
                labelSpan.getStyle()
                                .set("font-size", "14px")
                                .set("color", "#64748b")
                                .set("font-weight", "500")
                                .set("line-height", "1.4");

                textContent.add(numberSpan, labelSpan);

                // Contenedor del ícono mejorado
                Div iconContainer = new Div();
                iconContainer.getStyle()
                                .set("width", "56px")
                                .set("height", "56px")
                                .set("border-radius", "50%")
                                .set("display", "flex")
                                .set("align-items", "center")
                                .set("justify-content", "center")
                                .set("background", hexToLight(color))
                                .set("flex-shrink", "0");

                Icon cardIcon = icon.create();
                cardIcon.setSize("24px");
                cardIcon.setColor(color);
                iconContainer.add(cardIcon);

                content.add(textContent, iconContainer);
                card.add(content);
                return card;
        }

        private VerticalLayout createBlockedUsersSection() {
                VerticalLayout section = new VerticalLayout();
  
                section.setFlexGrow(1); // AÑADIR: para que tome el espacio disponible
                section.getStyle().set("margin-bottom", "30px");

                // Contenedor mejorado
                Div container = new Div();
                container.getStyle()
                                .set("background", "#ffffff")
                                .set("border-radius", "12px")
                                .set("padding", "24px")
                                .set("box-shadow", "0 4px 6px rgba(0,0,0,0.07)")
                                .set("border", "1px solid #e2e8f0")
                                .set("height", "100%") // AÑADIR: para que use toda la altura
                                .set("display", "flex") // AÑADIR: para control de layout
                                .set("flex-direction", "column"); // AÑADIR: dirección vertical

                // Título mejorado
                H2 sectionTitle = new H2("Usuarios Bloqueados");
                sectionTitle.getStyle()
                                .set("color", "#1e293b")
                                .set("font-size", "20px")
                                .set("margin", "0 0 8px 0")
                                .set("font-weight", "600");

                Span sectionSubtitle = new Span("Gestiona usuarios bloqueados y envía correos de desbloqueo");
                sectionSubtitle.getStyle()
                                .set("color", "#64748b")
                                .set("font-size", "14px")
                                .set("margin-bottom", "20px")
                                .set("display", "block");

                // Verificar si hay datos
                if (usuariosBloqueados == null || usuariosBloqueados.isEmpty()) {
                        container.add(sectionTitle, sectionSubtitle, createNoDataDiv("No hay usuarios bloqueados"));
                } else {
                        // Grid mejorado
                        blockedGrid = new Grid<>(Usuario.class, false);
                        blockedGrid.setItems(usuariosBloqueados);
                        blockedGrid.setHeight("400px");
                        blockedGrid.setSizeFull(); // CAMBIAR: era setHeight("400px")
                        blockedGrid.setMinHeight("300px"); // AÑADIR: altura mínima

                        // Columnas optimizadas
                        blockedGrid.addColumn(usuario -> usuario.getNombre() + " " + usuario.getApellido())
                                        .setHeader("Nombre Usuario")
                                        .setAutoWidth(true)
                                        .setFlexGrow(1);

                        blockedGrid.addColumn(Usuario::getCorreo)
                                        .setHeader("Correo Electrónico")
                                        .setAutoWidth(true)
                                        .setFlexGrow(1);

                        // Columna de acciones con botón mejorado
                        blockedGrid.addComponentColumn(usuario -> {
                                Button unblockBtn = new Button("Desbloquear", VaadinIcon.UNLOCK.create());
                                unblockBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
                                unblockBtn.addClickListener(e -> handleUnblock(usuario));
                                return unblockBtn;
                        }).setHeader("Acciones").setAutoWidth(true).setFlexGrow(0);

                        // Estilos del grid
                        blockedGrid.getStyle()
                                        .set("border-radius", "8px")
                                        .set("border", "1px solid #e2e8f0");

                        container.add(sectionTitle, sectionSubtitle, blockedGrid);
                }

                section.add(container);
                return section;
        }

        private VerticalLayout createUnblockRequestsSection() {
                VerticalLayout section = new VerticalLayout();
                section.setWidthFull();
                section.setSizeFull(); // CAMBIAR: para que use toda la altura
                section.setPadding(false);
                section.setSpacing(true);
                section.setFlexGrow(1); // AÑADIR: para que tome el espacio disponible

                // Contenedor mejorado
                Div container = new Div();
                container.getStyle()
                                .set("background", "#ffffff")
                                .set("border-radius", "12px")
                                .set("padding", "24px")
                                .set("box-shadow", "0 4px 6px rgba(0,0,0,0.07)")
                                .set("border", "1px solid #e2e8f0")
                                .set("height", "100%") // AÑADIR: para que use toda la altura
                                .set("display", "flex") // AÑADIR: para control de layout
                                .set("flex-direction", "column"); // AÑADIR: dirección vertical

                // Título mejorado
                H2 sectionTitle = new H2("Solicitudes de Desbloqueo");
                sectionTitle.getStyle()
                                .set("color", "#1e293b")
                                .set("font-size", "20px")
                                .set("margin", "0 0 8px 0")
                                .set("font-weight", "600");

                Span sectionSubtitle = new Span("Gestiona las solicitudes de desbloqueo recibidas");
                sectionSubtitle.getStyle()
                                .set("color", "#64748b")
                                .set("font-size", "14px")
                                .set("margin-bottom", "20px")
                                .set("display", "block");

                // Verificar si hay datos
                if (solicitudesDesbloqueo == null || solicitudesDesbloqueo.isEmpty()) {
                        container.add(sectionTitle, sectionSubtitle,
                                        createNoDataDiv("No hay solicitudes de desbloqueo pendientes"));
                } else {
                        // Grid mejorado
                        requestsGrid = new Grid<>(Usuario.class, false);
                        requestsGrid.setItems(solicitudesDesbloqueo);
                        requestsGrid.setSizeFull(); // CAMBIAR: era setHeight("400px")
                        requestsGrid.setMinHeight("300px"); // AÑADIR: altura mínima

                        // Columnas optimizadas
                        requestsGrid.addColumn(usuario -> usuario.getNombre() + " " + usuario.getApellido())
                                        .setHeader("Nombre")
                                        .setAutoWidth(true)
                                        .setFlexGrow(1);

                        requestsGrid.addColumn(Usuario::getCorreo)
                                        .setHeader("Correo Electrónico")
                                        .setAutoWidth(true)
                                        .setFlexGrow(1);

                        // Columna de acciones con botones correctamente coloreados
                        requestsGrid.addComponentColumn(request -> {
                                HorizontalLayout actions = new HorizontalLayout();
                                actions.setSpacing(true);

                                // Botón Aprobar - Verde
                                Button approveBtn = new Button("Aprobar", VaadinIcon.CHECK.create());
                                approveBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
                                approveBtn.getStyle()
                                                .set("background-color", "#38a169")
                                                .set("color", "white")
                                                .set("border", "none")
                                                .set("border-radius", "6px")
                                                .set("font-weight", "500");
                                approveBtn.addClickListener(e -> handleApprove(request));

                                // Botón Rechazar - Rojo
                                Button rejectBtn = new Button("Rechazar", VaadinIcon.CLOSE.create());
                                rejectBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
                                rejectBtn.getStyle()
                                                .set("background-color", "#e53e3e")
                                                .set("color", "white")
                                                .set("border", "none")
                                                .set("border-radius", "6px")
                                                .set("font-weight", "500");
                                rejectBtn.addClickListener(e -> handleReject(request));

                                actions.add(approveBtn, rejectBtn);
                                return actions;
                        }).setHeader("Acciones").setAutoWidth(true).setFlexGrow(0);

                        // Estilos del grid
                        requestsGrid.getStyle()
                                        .set("border-radius", "8px")
                                        .set("border", "1px solid #e2e8f0");

                        container.add(sectionTitle, sectionSubtitle, requestsGrid);
                }

                section.add(container);
                return section;
        }

        private Div createNoDataDiv(String message) {
                Div noDataDiv = new Div();
                noDataDiv.getStyle()
                                .set("text-align", "center")
                                .set("padding", "40px")
                                .set("color", "#64748b")
                                .set("background", "#f8fafc")
                                .set("border-radius", "8px")
                                .set("border", "2px dashed #e2e8f0");

                Icon noDataIcon = VaadinIcon.INFO_CIRCLE.create();
                noDataIcon.setSize("48px");
                noDataIcon.getStyle().set("color", "#94a3b8").set("margin-bottom", "12px");

                Span noDataText = new Span(message);
                noDataText.getStyle()
                                .set("font-size", "16px")
                                .set("font-weight", "500")
                                .set("display", "block");

                noDataDiv.add(noDataIcon, noDataText);
                return noDataDiv;
        }

        private void handleUnblock(Usuario usuario) {
                if (usuarioService.enviarDesbloqueo(usuario.getCorreo())) {
                        Notification notification = Notification.show(
                                        "Usuario desbloqueado: " + usuario.getNombre(),
                                        3000,
                                        Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        actualizarDatos();
                } else {
                        Notification notification = Notification.show(
                                        "Error al desbloquear usuario",
                                        3000,
                                        Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
        }

        private void handleApprove(Usuario request) {
                if (usuarioService.enviarDesbloqueo(request.getCorreo())) {
                        Notification notification = Notification.show(
                                        "Solicitud aprobada: " + request.getNombre(),
                                        3000,
                                        Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        actualizarDatos();
                } else {
                        Notification notification = Notification.show(
                                        "Error al aprobar solicitud",
                                        3000,
                                        Notification.Position.TOP_END);
                        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
        }

        private void handleReject(Usuario request) {
                // Aquí deberías implementar la lógica de rechazo en el servicio
                Notification notification = Notification.show(
                                "Solicitud rechazada: " + request.getNombre(),
                                3000,
                                Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                actualizarDatos();
        }

        private String hexToLight(String hex) {
                switch (hex) {
                        case "#38a169":
                                return "#c6f6d5"; // verde suave
                        case "#e53e3e":
                                return "#fed7d7"; // rojo suave
                        case "#ff8c00":
                                return "#feebc8"; // naranja suave
                        default:
                                return "#f7fafc"; // gris por defecto
                }
        }
}