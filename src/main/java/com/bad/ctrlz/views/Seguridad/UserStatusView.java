package com.bad.ctrlz.views.Seguridad;

import com.bad.ctrlz.model.Usuario;
import com.bad.ctrlz.service.UsuarioService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Gestión de Estados de Usuario")
@Route(value = "estado-usuario")
public class UserStatusView extends VerticalLayout {

    @Autowired
    private UsuarioService usuarioService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Grid<Usuario> grid;

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
        statsLayout.getStyle().set("max-width", "1250px")
                .set("margin-left", "auto")
                .set("margin-right", "auto");
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
                createStatCard(bloqueados, "Usuarios Bloqueados", "error"));

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
        listCard.getStyle().set("max-width", "1250px")
                .set("margin-left", "auto")
                .set("margin-right", "auto");

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
        grid = new Grid<>(Usuario.class, false);
        grid.setSizeFull();

        setupGrid();
        loadGridData();

        listContainer.add(listTitle, description, grid);
        listCard.add(listContainer);
        return listCard;
    }

    private void setupGrid() {
        // Estilos generales del grid
        grid.addThemeNames("no-border", "no-row-borders");
        grid.getStyle().set("background", "transparent");
        grid.getStyle().set("--lumo-contrast-10pct", "#f8fafc");
        grid.setSizeFull();

        // Columna: Usuario (Nombre completo)
        grid.addColumn(usuario -> usuario.getNombre() + " " + usuario.getApellido())
                .setHeader("Usuario")
                .setAutoWidth(true)
                .setFlexGrow(1);

        // Columna: Correo Electrónico
        grid.addColumn(Usuario::getCorreo)
                .setHeader("Email")
                .setAutoWidth(true)
                .setFlexGrow(1);

        // Columna: Estado (badge visual)
        grid.addComponentColumn(this::createStatusBadge)
                .setHeader("Estado")
                .setAutoWidth(true)
                .setFlexGrow(0);

        // Columna: Fecha de Registro
        grid.addColumn(usuario -> formatDateTime(usuario.getFechaCreacion()))
                .setHeader("Fecha de Registro")
                .setAutoWidth(true)
                .setFlexGrow(0);

        // Columna: Última Actualización
        grid.addColumn(usuario -> formatDateTime(usuario.getFechaActualizacion()))
                .setHeader("Última Actualización")
                .setAutoWidth(true)
                .setFlexGrow(0);

        // Columna: Acciones (botones)
        grid.addComponentColumn(this::createActionButtons)
                .setHeader("Acciones")
                .setAutoWidth(true)
                .setFlexGrow(0);

        // Estilo para headers: fondo claro, texto oscuro
        grid.getElement().executeJs("""
                    this.shadowRoot.querySelectorAll('[part="header-cell"]').forEach(cell => {
                        cell.style.background = '#f8fafc';
                        cell.style.color = '#1f2937'; // gris oscuro para mejor contraste
                        cell.style.fontWeight = '600';
                        cell.style.fontSize = '13px';
                        cell.style.padding = '12px 16px';
                    });
                """);
    }

    private Component createStatusBadge(Usuario usuario) {
        Span badge = new Span();
        badge.getStyle()
                .set("padding", "4px 8px")
                .set("border-radius", "12px")
                .set("font-size", "12px")
                .set("font-weight", "600")
                .set("color", "white");

        if (usuario.isAccountLocked()) {
            badge.setText("Bloqueado");
            badge.getStyle().set("background-color", "#ef4444"); // rojo
        } else if (!usuario.isEnabled()) {
            badge.setText("Inactivo");
            badge.getStyle().set("background-color", "#f59e0b"); // ámbar
        } else {
            badge.setText("Activo");
            badge.getStyle().set("background-color", "#10b981"); // verde
        }

        return badge;
    }

    private Component createActionButtons(Usuario usuario) {
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.setPadding(false);

        if (usuario.isAccountLocked()) {
            // Usuario bloqueado
            Span blockedText = new Span("Usuario bloqueado");
            blockedText.getStyle().set("color", "#6b7280");
            blockedText.getStyle().set("font-size", "12px");
            blockedText.getStyle().set("font-style", "italic");
            actions.add(blockedText);
        } else {
            // Botón de acción con modal de confirmación
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
            actionButton.getStyle().set("color", "#6b7280");

            boolean isActivo = usuario.isEnabled();

            actionButton.setText(isActivo ? "Desactivar" : "Activar");
            actionButton.setIcon(isActivo ? VaadinIcon.USER_CLOCK.create() : VaadinIcon.USER_CHECK.create());

            actionButton.addClickListener(e -> {
                String mensaje = isActivo
                        ? "¿Estás seguro de desactivar al usuario " + usuario.getNombre() + " " + usuario.getApellido()
                                + "?"
                        : "¿Estás seguro de activar al usuario " + usuario.getNombre() + " " + usuario.getApellido()
                                + "?";

                showConfirmationDialog(mensaje, () -> toggleUserStatus(usuario, !isActivo));
            });

            // Hover
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

    private void toggleUserStatus(Usuario usuario, boolean nuevoEstado) {
        try {
            usuario.setEnabled(nuevoEstado);
            usuarioService.actualizarUsuario(usuario); // <-- método que debes tener en tu servicio

            grid.getDataProvider().refreshItem(usuario);

            String mensaje = nuevoEstado ? "Usuario activado correctamente"
                    : "Usuario desactivado correctamente";
            Notification.show(mensaje, 3000, Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            Notification.show("Error al cambiar el estado del usuario", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void loadGridData() {
        grid.setItems(usuarioService.obtenerUsuarios());
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null)
            return "N/A";
        return dateTime.format(formatter);
    }

    private void showConfirmationDialog(String message, Runnable onConfirm) {
        Dialog dialog = new Dialog();
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(true);
        content.setSpacing(true);
        content.setAlignItems(FlexComponent.Alignment.CENTER); // Centra todo el contenido horizontalmente

        Span text = new Span(message);
        text.getStyle()
                .set("font-size", "14px")
                .set("color", "#374151");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Centrar botones
        buttons.setWidthFull(); // Ocupa todo el ancho disponible

        Button confirmBtn = new Button("Confirmar", e -> {
            onConfirm.run(); // Ejecutar la acción pasada como lambda
            dialog.close();
        });
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancelar", e -> dialog.close());

        buttons.add(confirmBtn, cancelBtn);
        content.add(text, buttons);

        dialog.add(content);
        dialog.open();
    }

}