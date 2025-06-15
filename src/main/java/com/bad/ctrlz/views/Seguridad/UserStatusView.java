package com.bad.ctrlz.views.Seguridad;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


@PageTitle("Gestión de Estados de Usuario")
@Route(value = "estado-usuario")
public class UserStatusView extends VerticalLayout {

    @Autowired
    private UsuarioService usuarioService;

    private Grid<UserData> grid;

    public UserStatusView(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        
        // Aplicar el fondo oscuro como en la imagen
        
        add(createHeader());
        add(createMainContent());
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.START);
        header.setAlignItems(Alignment.CENTER);
        header.setPadding(true);
        header.setSpacing(true);
        
        // Estilo del header exactamente como en la imagen
        header.getStyle().set("background", "#2d3441");
        header.getStyle().set("border-bottom", "1px solid #4a5568");
        header.getStyle().set("padding", "16px 24px");

        // Botón de volver mejorado
        Button backButton = new Button(VaadinIcon.ARROW_LEFT.create());
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.getStyle().set("color", "#e2e8f0");
        backButton.getStyle().set("background", "transparent");
        backButton.getStyle().set("border", "1px solid #4a5568");
        backButton.getStyle().set("border-radius", "6px");
        backButton.getStyle().set("padding", "8px 12px");
        backButton.getStyle().set("font-size", "14px");
        backButton.addClickListener(e -> UI.getCurrent().navigate("inicio-administracion-usuarios"));

        // Icono de usuario
        Icon userIcon = VaadinIcon.USER.create();
        userIcon.setSize("20px");
        userIcon.getStyle().set("color", "#e2e8f0");
        userIcon.getStyle().set("margin-left", "16px");

        // Título
        H2 title = new H2("Gestión de Estados de Usuario");
        title.addClassNames(LumoUtility.Margin.NONE);
        title.getStyle().set("color", "#ffffff");
        title.getStyle().set("font-weight", "600");
        title.getStyle().set("font-size", "20px");
        title.getStyle().set("margin-left", "8px");

        header.add(backButton, userIcon, title);
        return header;
    }

    private Component createMainContent() {
        VerticalLayout mainContent = new VerticalLayout();
        mainContent.setSizeFull();
        mainContent.setPadding(true);
        mainContent.setSpacing(true);
        mainContent.getStyle().set("padding", "24px");
        
        mainContent.add(createStatsCards());
        mainContent.add(createUserListCard());
        
        return mainContent;
    }

    private Component createStatsCards() {
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        statsLayout.setPadding(false);
        statsLayout.setMargin(false);
        statsLayout.getStyle().set("gap", "16px");
        statsLayout.getStyle().set("margin-bottom", "24px");

        // Estadisticas de usuario
        String activos = Integer.toString(usuarioService.obtenerActivos()); 
        String bloqueados = Integer.toString(usuarioService.obtenerBloqueados());
        String inactivos = Integer.toString(usuarioService.obtenerInactivos());
        
        // Datos estáticos temporales
        statsLayout.add(
            createStatCard(activos, "Usuarios Activos", "success"),
            createStatCard(inactivos, "Usuarios Inactivos", "contrast"),
            createStatCard(bloqueados, "Usuarios Bloqueados", "error")
        );

        return statsLayout;
    }

    private Component createStatCard(String value, String title, String theme) {
        Div card = new Div();
        card.setWidthFull();
        
        // Estilo de la tarjeta exactamente como en la imagen
        card.getStyle().set("background", "#ffffff");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("padding", "20px");
        card.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
        card.getStyle().set("border", "1px solid #e2e8f0");

        HorizontalLayout content = new HorizontalLayout();
        content.setWidthFull();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.START);
        content.setSpacing(true);
        content.setPadding(false);
        content.setMargin(false);
        content.getStyle().set("gap", "12px");

        // Icono con círculo de fondo
        Div iconContainer = new Div();
        iconContainer.getStyle().set("width", "40px");
        iconContainer.getStyle().set("height", "40px");
        iconContainer.getStyle().set("border-radius", "50%");
        iconContainer.getStyle().set("display", "flex");
        iconContainer.getStyle().set("align-items", "center");
        iconContainer.getStyle().set("justify-content", "center");
        
        Icon icon = null;
        if ("success".equals(theme)) {
            icon = VaadinIcon.CHECK_CIRCLE.create();
            icon.getStyle().set("color", "#10b981");
            iconContainer.getStyle().set("background", "#d1fae5");
        } else if ("error".equals(theme)) {
            icon = VaadinIcon.BAN.create();
            icon.getStyle().set("color", "#ef4444");
            iconContainer.getStyle().set("background", "#fee2e2");
        } else {
            icon = VaadinIcon.USER.create();
            icon.getStyle().set("color", "#6b7280");
            iconContainer.getStyle().set("background", "#f3f4f6");
        }
        icon.setSize("20px");
        iconContainer.add(icon);

        // Contenido de texto
        VerticalLayout textContent = new VerticalLayout();
        textContent.setSpacing(false);
        textContent.setPadding(false);
        textContent.setMargin(false);
        textContent.getStyle().set("gap", "2px");

        // Valor numérico grande
        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("font-size", "28px");
        valueSpan.getStyle().set("font-weight", "700");
        valueSpan.getStyle().set("line-height", "1");
        
        if ("success".equals(theme)) {
            valueSpan.getStyle().set("color", "#10b981");
        } else if ("error".equals(theme)) {
            valueSpan.getStyle().set("color", "#ef4444");
        } else {
            valueSpan.getStyle().set("color", "#1f2937");
        }

        // Título descriptivo
        Span titleSpan = new Span(title);
        titleSpan.getStyle().set("color", "#6b7280");
        titleSpan.getStyle().set("font-size", "14px");
        titleSpan.getStyle().set("font-weight", "500");

        textContent.add(valueSpan, titleSpan);
        content.add(iconContainer, textContent);
        card.add(content);
        return card;
    }

    private Component createUserListCard() {
        Div listCard = new Div();
        listCard.setSizeFull();
        
        // Estilo de la tarjeta principal
        listCard.getStyle().set("background", "#ffffff");
        listCard.getStyle().set("border-radius", "12px");
        listCard.getStyle().set("padding", "24px");
        listCard.getStyle().set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
        listCard.getStyle().set("border", "1px solid #e2e8f0");

        VerticalLayout listContainer = new VerticalLayout();
        listContainer.setSizeFull();
        listContainer.setPadding(false);
        listContainer.setSpacing(true);
        listContainer.getStyle().set("gap", "16px");

        // Título y descripción
        H2 listTitle = new H2("Lista de Usuarios");
        listTitle.addClassNames(LumoUtility.Margin.Bottom.NONE);
        listTitle.getStyle().set("color", "#1f2937");
        listTitle.getStyle().set("font-size", "20px");
        listTitle.getStyle().set("font-weight", "600");
        listTitle.getStyle().set("margin", "0");

        Span description = new Span("Activa o desactiva usuarios del sistema");
        description.getStyle().set("color", "#6b7280");
        description.getStyle().set("font-size", "14px");

        // Grid de usuarios
        grid = new Grid<>(UserData.class, false);
        grid.setSizeFull();
        
        setupGrid();
        loadGridData();

        listContainer.add(listTitle, description, grid);
        listCard.add(listContainer);
        return listCard;
    }

    private void setupGrid() {
        // Estilo del grid para coincidir con la imagen
        grid.addThemeNames("no-border", "no-row-borders");
        grid.getStyle().set("background", "transparent");
        grid.getStyle().set("--lumo-contrast-10pct", "#f8fafc");

        // Columna Usuario
        grid.addColumn(UserData::getName)
            .setHeader("Usuario")
            .setFlexGrow(1)
            .setWidth("200px");

        // Columna Email
        grid.addColumn(UserData::getEmail)
            .setHeader("Email")
            .setFlexGrow(2)
            .setWidth("250px");

        // Columna Estado
        grid.addComponentColumn(this::createStatusBadge)
            .setHeader("Estado")
            .setFlexGrow(0)
            .setWidth("120px");

        // Columna Último Acceso
        grid.addColumn(user -> user.getLastAccess().toString())
            .setHeader("Último Acceso")
            .setFlexGrow(1)
            .setWidth("140px");

        // Columna Fecha de Registro
        grid.addColumn(user -> user.getRegistrationDate().toString())
            .setHeader("Fecha de Registro")
            .setFlexGrow(1)
            .setWidth("160px");

        // Columna Acciones
        grid.addComponentColumn(this::createActionButtons)
            .setHeader("Acciones")
            .setFlexGrow(0)
            .setWidth("150px");

        // Aplicar estilos de header de manera segura
        grid.getElement().executeJs(
            "this.shadowRoot.querySelectorAll('[part=\"header-cell\"]').forEach(cell => {" +
            "  cell.style.background = '#f8fafc';" +
            "  cell.style.color = '#374151';" +
            "  cell.style.fontWeight = '600';" +
            "  cell.style.fontSize = '13px';" +
            "  cell.style.padding = '12px 16px';" +
            "});"
        );
    }

    private Component createStatusBadge(UserData user) {
        Span badge = new Span();
        badge.getStyle().set("display", "inline-flex");
        badge.getStyle().set("align-items", "center");
        badge.getStyle().set("gap", "6px");
        badge.getStyle().set("padding", "4px 12px");
        badge.getStyle().set("border-radius", "16px");
        badge.getStyle().set("font-size", "12px");
        badge.getStyle().set("font-weight", "600");
        badge.getStyle().set("text-transform", "none");
        
        Icon statusIcon;
        
        switch (user.getStatus()) {
            case ACTIVE:
                badge.setText("Activo");
                badge.getStyle().set("background", "#d1fae5");
                badge.getStyle().set("color", "#065f46");
                statusIcon = VaadinIcon.CHECK_CIRCLE.create();
                statusIcon.setSize("12px");
                statusIcon.getStyle().set("color", "#10b981");
                badge.add(statusIcon);
                break;
            case INACTIVE:
                badge.setText("Inactivo");
                badge.getStyle().set("background", "#f3f4f6");
                badge.getStyle().set("color", "#374151");
                statusIcon = VaadinIcon.USER.create();
                statusIcon.setSize("12px");
                statusIcon.getStyle().set("color", "#6b7280");
                badge.add(statusIcon);
                break;
            case BLOCKED:
                badge.setText("Bloqueado");
                badge.getStyle().set("background", "#fee2e2");
                badge.getStyle().set("color", "#991b1b");
                statusIcon = VaadinIcon.BAN.create();
                statusIcon.setSize("12px");
                statusIcon.getStyle().set("color", "#ef4444");
                badge.add(statusIcon);
                break;
        }
        
        return badge;
    }

    private Component createActionButtons(UserData user) {
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.setPadding(false);

        if (user.getStatus() == UserStatus.BLOCKED) {
            // Para usuarios bloqueados, mostrar texto estático
            Span blockedText = new Span("Usuario bloqueado");
            blockedText.getStyle().set("color", "#6b7280");
            blockedText.getStyle().set("font-size", "12px");
            blockedText.getStyle().set("font-style", "italic");
            actions.add(blockedText);
        } else {
            // Botón de activar/desactivar con estilo exacto de la imagen
            Button actionButton = new Button();
            actionButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
            actionButton.getStyle().set("display", "inline-flex");
            actionButton.getStyle().set("align-items", "center");
            actionButton.getStyle().set("gap", "4px");
            actionButton.getStyle().set("padding", "6px 12px");
            actionButton.getStyle().set("border", "none");
            actionButton.getStyle().set("background", "transparent");
            actionButton.getStyle().set("border-radius", "6px");
            actionButton.getStyle().set("font-size", "12px");
            actionButton.getStyle().set("font-weight", "500");
            
            if (user.getStatus() == UserStatus.ACTIVE) {
                actionButton.setText("Desactivar");
                actionButton.setIcon(VaadinIcon.USER_CLOCK.create());
                actionButton.getStyle().set("color", "#6b7280");
                actionButton.addClickListener(e -> toggleUserStatus(user, UserStatus.INACTIVE));
            } else {
                actionButton.setText("Activar");
                actionButton.setIcon(VaadinIcon.USER_CHECK.create());
                actionButton.getStyle().set("color", "#6b7280");
                actionButton.addClickListener(e -> toggleUserStatus(user, UserStatus.ACTIVE));
            }
            
            // Hover effect
            actionButton.getElement().addEventListener("mouseenter", e -> {
                actionButton.getStyle().set("background", "#f3f4f6");
            });
            actionButton.getElement().addEventListener("mouseleave", e -> {
                actionButton.getStyle().set("background", "transparent");
            });
            
            actions.add(actionButton);
        }

        return actions;
    }

    private void toggleUserStatus(UserData user, UserStatus newStatus) {
        // TODO: Implementar el cambio de estado a través del servicio
        // try {
        //     userService.updateUserStatus(user.getId(), newStatus);
        //     user.setStatus(newStatus);
        //     grid.getDataProvider().refreshItem(user);
        //     
        //     String message = newStatus == UserStatus.ACTIVE ? 
        //         "Usuario activado correctamente" : "Usuario desactivado correctamente";
        //     Notification.show(message, 3000, Notification.Position.TOP_CENTER);
        // } catch (Exception e) {
        //     Notification.show("Error al cambiar el estado del usuario", 3000, Notification.Position.TOP_CENTER);
        // }

        // Implementación temporal para demostración
        user.setStatus(newStatus);
        grid.getDataProvider().refreshItem(user);
        
        String message = newStatus == UserStatus.ACTIVE ? 
            "Usuario activado correctamente" : "Usuario desactivado correctamente";
        Notification.show(message, 3000, Notification.Position.TOP_CENTER);
    }

    private void loadGridData() {
        // TODO: Cargar datos reales desde tu servicio
        // List<UserData> users = userService.getAllUsers();
        // grid.setItems(users);

        // Datos estáticos temporales
        List<UserData> staticUsers = createStaticUserData();
        grid.setItems(staticUsers);
    }

    // Método temporal para datos estáticos
    private List<UserData> createStaticUserData() {
        List<UserData> users = new ArrayList<>();
        
        users.add(new UserData(1L, "Ana García", "ana.garcia@email.com", 
            UserStatus.ACTIVE, LocalDate.of(2024, 1, 15), LocalDate.of(2023, 6, 10)));
        
        users.add(new UserData(2L, "Carlos López", "carlos.lopez@email.com", 
            UserStatus.INACTIVE, LocalDate.of(2024, 1, 10), LocalDate.of(2023, 8, 15)));
        
        users.add(new UserData(3L, "María Rodríguez", "maria.rodriguez@email.com", 
            UserStatus.BLOCKED, LocalDate.of(2024, 1, 5), LocalDate.of(2023, 9, 20)));
        
        users.add(new UserData(4L, "Juan Martínez", "juan.martinez@email.com", 
            UserStatus.ACTIVE, LocalDate.of(2024, 1, 14), LocalDate.of(2023, 7, 25)));
        
        users.add(new UserData(5L, "Laura Fernández", "laura.fernandez@email.com", 
            UserStatus.INACTIVE, LocalDate.of(2024, 1, 12), LocalDate.of(2023, 5, 18)));
        
        return users;
    }

    // Clase interna para los datos de usuario (muévela a un paquete separado)
    public static class UserData {
        private Long id;
        private String name;
        private String email;
        private UserStatus status;
        private LocalDate lastAccess;
        private LocalDate registrationDate;

        public UserData(Long id, String name, String email, UserStatus status, 
                       LocalDate lastAccess, LocalDate registrationDate) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.status = status;
            this.lastAccess = lastAccess;
            this.registrationDate = registrationDate;
        }

        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public UserStatus getStatus() { return status; }
        public void setStatus(UserStatus status) { this.status = status; }
        
        public LocalDate getLastAccess() { return lastAccess; }
        public void setLastAccess(LocalDate lastAccess) { this.lastAccess = lastAccess; }
        
        public LocalDate getRegistrationDate() { return registrationDate; }
        public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    }

    // Enum para los estados de usuario
    public enum UserStatus {
        ACTIVE, INACTIVE, BLOCKED
    }
}