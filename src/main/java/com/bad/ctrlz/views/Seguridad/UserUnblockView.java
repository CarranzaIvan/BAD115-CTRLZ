package com.bad.ctrlz.views.Seguridad;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Bloqueos | Usuarios")
@Route(value = "bloqueos-usuarios")
public class UserUnblockView extends VerticalLayout {

    // Clases de datos estáticos
    public static class BlockedUser {
        private String usuario;
        private String email;
        private LocalDate fechaBloqueo;
        private String motivo;
        private LocalDate ultimoAcceso;

        public BlockedUser(String usuario, String email, LocalDate fechaBloqueo, String motivo,
                LocalDate ultimoAcceso) {
            this.usuario = usuario;
            this.email = email;
            this.fechaBloqueo = fechaBloqueo;
            this.motivo = motivo;
            this.ultimoAcceso = ultimoAcceso;
        }

        // Getters
        public String getUsuario() {
            return usuario;
        }

        public String getEmail() {
            return email;
        }

        public LocalDate getFechaBloqueo() {
            return fechaBloqueo;
        }

        public String getMotivo() {
            return motivo;
        }

        public LocalDate getUltimoAcceso() {
            return ultimoAcceso;
        }
    }

    public static class UnblockRequest {
        private String usuario;
        private String email;
        private LocalDate fechaSolicitud;
        private String estado;

        public UnblockRequest(String usuario, String email, LocalDate fechaSolicitud, String estado) {
            this.usuario = usuario;
            this.email = email;
            this.fechaSolicitud = fechaSolicitud;
            this.estado = estado;
        }

        // Getters
        public String getUsuario() {
            return usuario;
        }

        public String getEmail() {
            return email;
        }

        public LocalDate getFechaSolicitud() {
            return fechaSolicitud;
        }

        public String getEstado() {
            return estado;
        }
    }

    private List<BlockedUser> blockedUsers;
    private List<UnblockRequest> unblockRequests;

    public UserUnblockView() {
        setSpacing(false);
        setPadding(false);
        setSizeFull();
        addClassName("user-unblock-view");

        // Inicializar datos estáticos
        initializeData();

        // Crear contenido principal
        add(createHeader());
        add(createStatsCards());
        add(createBlockedUsersSection());
        add(createUnblockRequestsSection());

        // Aplicar estilos CSS principales
        getElement().getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("min-height", "100vh")
                .set("padding", "20px");
    }

    private void initializeData() {
        // Datos estáticos para usuarios bloqueados
        blockedUsers = new ArrayList<>();
        blockedUsers.add(new BlockedUser("María Rodríguez", "maria.rodriguez@email.com",
                LocalDate.of(2024, 1, 5), "Actividad sospechosa", LocalDate.of(2024, 1, 4)));
        blockedUsers.add(new BlockedUser("Pedro Sánchez", "pedro.sanchez@email.com",
                LocalDate.of(2024, 1, 8), "Violación de términos", LocalDate.of(2024, 1, 7)));
        blockedUsers.add(new BlockedUser("Carmen López", "carmen.lopez@email.com",
                LocalDate.of(2024, 1, 10), "Múltiples intentos fallidos", LocalDate.of(2024, 1, 9)));

        // Datos estáticos para solicitudes de desbloqueo
        unblockRequests = new ArrayList<>();
        unblockRequests.add(new UnblockRequest("María Rodríguez", "maria.rodriguez@email.com",
                LocalDate.of(2024, 1, 16), "Pendiente"));
        unblockRequests.add(new UnblockRequest("Pedro Sánchez", "pedro.sanchez@email.com",
                LocalDate.of(2024, 1, 18), "Pendiente"));
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(false);
        header.setSpacing(true);
        header.addClassName("header-section");

        // Botón volver con estilo mejorado
        Button backButton = new Button("Volver al menú", VaadinIcon.ARROW_LEFT.create());
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle()
                .set("background", "rgba(255, 255, 255, 0.2)")
                .set("color", "white")
                .set("border", "1px solid rgba(255, 255, 255, 0.3)")
                .set("border-radius", "8px")
                .set("padding", "8px 16px")
                .set("backdrop-filter", "blur(10px)")
                .set("transition", "all 0.3s ease");
        backButton.addClickListener(e -> {
            Notification.show("Navegando al menú principal...");
        });

        // Título con icono
        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setSpacing(true);
        titleLayout.setAlignItems(Alignment.CENTER);

        Icon titleIcon = VaadinIcon.SHIELD.create();
        titleIcon.setSize("32px");
        titleIcon.setColor("white");

        H1 title = new H1("Gestión de Desbloqueo de Usuarios");
        title.addClassName("main-title");
        title.getStyle()
                .set("color", "white")
                .set("font-size", "2rem")
                .set("margin", "0")
                .set("font-weight", "600")
                .set("text-shadow", "0 2px 4px rgba(0,0,0,0.3)");

        titleLayout.add(titleIcon, title);

        header.add(backButton, titleLayout);
        header.setAlignItems(Alignment.CENTER);
        header.expand(titleLayout);
        header.getStyle().set("margin-bottom", "30px");

        return header;
    }

    private HorizontalLayout createStatsCards() {
        HorizontalLayout statsContainer = new HorizontalLayout();
        statsContainer.setWidthFull();
        statsContainer.setPadding(false);
        statsContainer.setSpacing(true);
        statsContainer.addClassName("stats-container");
        statsContainer.getStyle().set("margin-bottom", "30px");

        // Tarjeta usuarios bloqueados
        Div blockedCard = createStatsCard("3", "Usuarios Bloqueados", "#ff6b6b", VaadinIcon.LOCK);

        // Tarjeta solicitudes pendientes
        Div pendingCard = createStatsCard("2", "Solicitudes Pendientes", "#ffa726", VaadinIcon.CLOCK);

        // Tarjeta solicitudes aprobadas
        Div approvedCard = createStatsCard("0", "Solicitudes Aprobadas", "#66bb6a", VaadinIcon.CHECK_CIRCLE);

        statsContainer.add(blockedCard, pendingCard, approvedCard);
        return statsContainer;
    }

    private Div createStatsCard(String number, String label, String color, VaadinIcon icon) {
        Div card = new Div();
        card.addClassName("stats-card");
        card.getStyle()
                .set("background", "rgba(255, 255, 255, 0.95)")
                .set("border-radius", "16px")
                .set("padding", "24px")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.1)")
                .set("backdrop-filter", "blur(10px)")
                .set("border", "1px solid rgba(255, 255, 255, 0.2)")
                .set("text-align", "center")
                .set("flex", "1")
                .set("min-width", "200px")
                .set("transition", "transform 0.3s ease, box-shadow 0.3s ease")
                .set("cursor", "pointer");

        // Efecto hover
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                    .set("transform", "translateY(-5px)")
                    .set("box-shadow", "0 12px 40px rgba(0,0,0,0.15)");
        });

        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                    .set("transform", "translateY(0)")
                    .set("box-shadow", "0 8px 32px rgba(0,0,0,0.1)");
        });

        Icon cardIcon = icon.create();
        cardIcon.setSize("28px");
        cardIcon.setColor(color);
        cardIcon.getStyle().set("margin-bottom", "12px");

        Span numberSpan = new Span(number);
        numberSpan.getStyle()
                .set("font-size", "2.8rem")
                .set("font-weight", "bold")
                .set("color", color)
                .set("display", "block")
                .set("margin", "8px 0")
                .set("line-height", "1");

        Span labelSpan = new Span(label);
        labelSpan.getStyle()
                .set("color", "#64748b")
                .set("font-size", "0.95rem")
                .set("font-weight", "500")
                .set("line-height", "1.4");

        card.add(cardIcon, numberSpan, labelSpan);
        return card;
    }

    private VerticalLayout createBlockedUsersSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setPadding(false);
        section.setSpacing(true);
        section.addClassName("blocked-users-section");
        section.getStyle().set("margin-bottom", "30px");

        // Contenedor con fondo
        Div container = new Div();
        container.getStyle()
                .set("background", "rgba(255, 255, 255, 0.95)")
                .set("border-radius", "16px")
                .set("padding", "24px")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.1)")
                .set("backdrop-filter", "blur(10px)")
                .set("border", "1px solid rgba(255, 255, 255, 0.2)");

        // Título de la sección
        H2 sectionTitle = new H2("Usuarios Bloqueados");
        sectionTitle.getStyle()
                .set("color", "#1e293b")
                .set("font-size", "1.5rem")
                .set("margin", "0 0 8px 0")
                .set("font-weight", "600");

        Span sectionSubtitle = new Span("Gestiona usuarios bloqueados y envía correos de desbloqueo");
        sectionSubtitle.getStyle()
                .set("color", "#64748b")
                .set("font-size", "0.9rem")
                .set("margin-bottom", "20px")
                .set("display", "block");

        // Grid de usuarios bloqueados
        Grid<BlockedUser> blockedGrid = new Grid<>(BlockedUser.class, false);
        blockedGrid.addClassName("blocked-users-grid");
        blockedGrid.setItems(blockedUsers);

        // Configurar columnas con mejor estilo
        blockedGrid.addColumn(BlockedUser::getUsuario)
                .setHeader("Usuario")
                .setFlexGrow(1);

        blockedGrid.addColumn(BlockedUser::getEmail)
                .setHeader("Email")
                .setFlexGrow(2);

        blockedGrid.addColumn(user -> user.getFechaBloqueo().toString())
                .setHeader("Fecha de Bloqueo")
                .setFlexGrow(1);

        blockedGrid.addColumn(BlockedUser::getMotivo)
                .setHeader("Motivo")
                .setFlexGrow(2);

        blockedGrid.addColumn(user -> user.getUltimoAcceso().toString())
                .setHeader("Último Acceso")
                .setFlexGrow(1);

        // Columna de acciones con botones mejorados
        blockedGrid.addComponentColumn(user -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setSpacing(true);

            Button unblockBtn = new Button("Desbloquear y Notificar", VaadinIcon.UNLOCK.create());
            unblockBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
            unblockBtn.getStyle()
                    .set("border-radius", "8px")
                    .set("border", "none")
                    .set("color", "white")
                    .set("font-weight", "500");
            unblockBtn.addClickListener(e -> {
                Notification.show("Desbloqueando usuario: " + user.getUsuario());
            });

            Button emailBtn = new Button("Enviar Email", VaadinIcon.ENVELOPE.create());
            emailBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            emailBtn.getStyle()
                    .set("border", "1px solid #e2e8f0")
                    .set("border-radius", "8px")
                    .set("color", "#64748b")
                    .set("font-weight", "500");
            emailBtn.addClickListener(e -> {
                Notification.show("Enviando email a: " + user.getEmail());
            });

            actions.add(unblockBtn, emailBtn);
            return actions;
        }).setHeader("Acciones").setFlexGrow(2);

        // Aplicar estilos al grid
        blockedGrid.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("border", "1px solid #e2e8f0")
                .set("overflow", "hidden");

        container.add(sectionTitle, sectionSubtitle, blockedGrid);
        section.add(container);
        return section;
    }

    private VerticalLayout createUnblockRequestsSection() {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.setPadding(false);
        section.setSpacing(true);
        section.addClassName("unblock-requests-section");

        // Contenedor con fondo
        Div container = new Div();
        container.getStyle()
                .set("background", "rgba(255, 255, 255, 0.95)")
                .set("border-radius", "16px")
                .set("padding", "24px")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.1)")
                .set("backdrop-filter", "blur(10px)")
                .set("border", "1px solid rgba(255, 255, 255, 0.2)");

        // Título de la sección
        H2 sectionTitle = new H2("Solicitudes de Desbloqueo");
        sectionTitle.getStyle()
                .set("color", "#1e293b")
                .set("font-size", "1.5rem")
                .set("margin", "0 0 8px 0")
                .set("font-weight", "600");

        Span sectionSubtitle = new Span("Gestiona las solicitudes de desbloqueo recibidas");
        sectionSubtitle.getStyle()
                .set("color", "#64748b")
                .set("font-size", "0.9rem")
                .set("margin-bottom", "20px")
                .set("display", "block");

        // Lista de solicitudes pendientes
        VerticalLayout requestsList = new VerticalLayout();
        requestsList.setSpacing(true);
        requestsList.setPadding(false);
        requestsList.addClassName("requests-list");

        for (UnblockRequest request : unblockRequests) {
            Div requestCard = createRequestCard(request);
            requestsList.add(requestCard);
        }

        container.add(sectionTitle, sectionSubtitle, requestsList);
        section.add(container);
        return section;
    }

    private Div createRequestCard(UnblockRequest request) {
        Div card = new Div();
        card.addClassName("request-card");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "20px")
                .set("box-shadow", "0 4px 16px rgba(0,0,0,0.08)")
                .set("margin-bottom", "12px")
                .set("border", "1px solid #e2e8f0")
                .set("border-left", "4px solid #ffa726")
                .set("transition", "all 0.3s ease");

        // Efecto hover
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                    .set("transform", "translateX(5px)")
                    .set("box-shadow", "0 8px 24px rgba(0,0,0,0.12)");
        });

        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                    .set("transform", "translateX(0)")
                    .set("box-shadow", "0 4px 16px rgba(0,0,0,0.08)");
        });

        // Layout principal del card
        HorizontalLayout cardLayout = new HorizontalLayout();
        cardLayout.setWidthFull();
        cardLayout.setAlignItems(Alignment.CENTER);

        // Información del usuario
        VerticalLayout userInfo = new VerticalLayout();
        userInfo.setSpacing(false);
        userInfo.setPadding(false);

        Span userName = new Span(request.getUsuario());
        userName.getStyle()
                .set("font-weight", "600")
                .set("color", "#1e293b")
                .set("font-size", "1.1rem");

        Span userEmail = new Span(request.getEmail());
        userEmail.getStyle()
                .set("color", "#64748b")
                .set("font-size", "0.9rem");

        Span requestDate = new Span("Fecha de solicitud: " + request.getFechaSolicitud().toString());
        requestDate.getStyle()
                .set("color", "#64748b")
                .set("font-size", "0.85rem");

        userInfo.add(userName, userEmail, requestDate);

        // Estado con mejor diseño
        Div statusBadge = new Div();
        statusBadge.setText("● " + request.getEstado());
        statusBadge.getStyle()
                .set("background", "linear-gradient(45deg, #fff3cd, #ffeaa7)")
                .set("color", "#856404")
                .set("padding", "6px 16px")
                .set("border-radius", "20px")
                .set("font-size", "0.85rem")
                .set("font-weight", "500")
                .set("border", "1px solid rgba(255, 193, 7, 0.3)");

        // Botones de acción mejorados
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);

        Button approveBtn = new Button("Aprobar", VaadinIcon.CHECK.create());
        approveBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_SMALL);
        approveBtn.getStyle()
                .set("background", "linear-gradient(45deg, #66bb6a, #4caf50)")
                .set("border-radius", "8px")
                .set("border", "none")
                .set("color", "white")
                .set("font-weight", "500");
        approveBtn.addClickListener(e -> {
            Notification.show("Solicitud aprobada para: " + request.getUsuario());
        });

        Button rejectBtn = new Button("Rechazar", VaadinIcon.CLOSE.create());
        rejectBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        rejectBtn.getStyle()
                .set("background", "linear-gradient(45deg, #ff6b6b, #f44336)")
                .set("border-radius", "8px")
                .set("border", "none")
                .set("color", "white")
                .set("font-weight", "500");
        rejectBtn.addClickListener(e -> {
            Notification.show("Solicitud rechazada para: " + request.getUsuario());
        });

        actions.add(approveBtn, rejectBtn);

        cardLayout.add(userInfo, statusBadge, actions);
        cardLayout.expand(userInfo);

        card.add(cardLayout);
        return card;
    }
}