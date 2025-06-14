package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.service.EncuestaService;
import com.bad.ctrlz.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "dashboard-encuestas", layout = MainLayout.class)
@PageTitle("Dashboard de Encuestas")
public class DashboardEncuestasView extends VerticalLayout {

    private final EncuestaService encuestaService;

    public DashboardEncuestasView(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;

        setSizeFull();
        setMargin(true);
        setSpacing(true);
        setPadding(true);

        Button crearBtn = new Button("Crear Encuesta", e -> getUI().ifPresent(ui -> ui.navigate("crear-encuesta")));
        crearBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        crearBtn.getStyle().set("margin-bottom", "20px").set("font-weight", "bold");

        H2 titulo = new H2("Gestión de Encuestas");
        add(crearBtn, titulo);
        cargarEncuestas();
    }

    private void cargarEncuestas() {
        List<Encuesta> encuestas = encuestaService.obtenerTodas();

        if (encuestas.isEmpty()) {
            add(new Span("No hay encuestas registradas."));
            return;
        }

        encuestas.forEach(encuesta -> {
            add(crearCard(encuesta));
        });
    }

    private Div crearCard(Encuesta encuesta) {
        Div card = new Div();
        card.getStyle().set("padding", "20px").set("margin-bottom", "20px")
                .set("border-radius", "10px").set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)")
                .set("background-color", "#FFFFFF").set("width", "80%")
                .set("max-width", "800px").set("margin-left", "auto").set("margin-right", "auto");

        Span titulo = new Span("Título: " + encuesta.getTitulo());
        Span objetivo = new Span("Objetivo: " + encuesta.getObjetivo());
        Span grupoMeta = new Span("Grupo Meta: " + encuesta.getGrupoMeta());
        Span estado = new Span("Estado: " + ("A".equals(encuesta.getEstado()) ? "Activo" : "Inactivo"));

        titulo.getStyle().set("font-weight", "bold").set("font-size", "18px");
        objetivo.getStyle().set("display", "block");
        grupoMeta.getStyle().set("display", "block");
        estado.getStyle().set("display", "block");

        HorizontalLayout acciones = crearBotonesAcciones(encuesta);

        card.add(titulo, objetivo, grupoMeta, estado, acciones);
        return card;
    }

    private HorizontalLayout crearBotonesAcciones(Encuesta encuesta) {
        Button editar = new Button("Editar", e -> editarEncuesta(encuesta));
        Button eliminar = new Button("Eliminar", e -> confirmarEliminar(encuesta));
        Button agregarPreguntas = new Button("Agregar Preguntas", e -> agregarPreguntas(encuesta));
        Button generarLink = new Button("Generar Link", e -> generarLinkEncuesta(encuesta));

        editar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        eliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        agregarPreguntas.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        generarLink.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout acciones = new HorizontalLayout(editar, eliminar, agregarPreguntas, generarLink);
        acciones.setSpacing(true);
        return acciones;
    }

    private void editarEncuesta(Encuesta encuesta) {
        getUI().ifPresent(ui -> ui.navigate("editar-encuesta/" + encuesta.getIdEncuesta()));
    }

    private void agregarPreguntas(Encuesta encuesta) {
        Integer id = encuesta.getIdEncuesta();
        getUI().ifPresent(ui -> ui.navigate(id + "/preguntas"));
    }

    private void confirmarEliminar(Encuesta encuesta) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirmar eliminación");

        Button confirmar = new Button("Eliminar", e -> {
            encuestaService.eliminar(encuesta);
            confirmDialog.close();
            Notification.show("Encuesta eliminada correctamente", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        confirmar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelar = new Button("Cancelar", e -> confirmDialog.close());

        confirmDialog.add("¿Está seguro de eliminar esta encuesta?");
        confirmDialog.getFooter().add(confirmar, cancelar);
        confirmDialog.open();
    }

    private void generarLinkEncuesta(Encuesta encuesta) {
        String baseUrl = "http://localhost:8081/public/encuesta/";

        // Si ya tiene un link generado, lo usamos
        if (encuesta.getLinkPublico() != null && !encuesta.getLinkPublico().isEmpty()) {
            mostrarDialogoLink(baseUrl + encuesta.getLinkPublico());
        } else {
            // Generamos un UUID nuevo
            String nuevoLink = java.util.UUID.randomUUID().toString();
            encuesta.setLinkPublico(nuevoLink);
            encuestaService.actualizar(encuesta);
            mostrarDialogoLink(baseUrl + nuevoLink);
        }
    }

    private void mostrarDialogoLink(String link) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Link generado");

        Span label = new Span("Este es el link de acceso público:");
        TextField linkField = new TextField();
        linkField.setValue(link);
        linkField.setReadOnly(true);
        linkField.setWidthFull();

        Button copiarBtn = new Button("Copiar");
        copiarBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        copiarBtn.addClickListener(e -> {
            linkField.getElement().executeJs(
                    "navigator.clipboard.writeText($0).then(function() {" +
                            "  $1.$server.copied();" +
                            "});",
                    linkField.getValue(), copiarBtn);
        });

        // Agregamos feedback visual en el servidor (opcional)
        copiarBtn.getElement().setProperty("serverOnly", true);
        copiarBtn.getElement().addPropertyChangeListener("copied", e -> {
            copiarBtn.setText("¡Copiado!");
        });

        // Alternativamente, mucho más simple:
        copiarBtn.addClickListener(e -> {
            linkField.getElement().executeJs("navigator.clipboard.writeText($0);", linkField.getValue());
            copiarBtn.setText("¡Copiado!");
        });

        Button cerrar = new Button("Cerrar", e -> dialog.close());
        cerrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout botones = new HorizontalLayout(copiarBtn, cerrar);
        botones.setSpacing(true);

        dialog.add(label, linkField, botones);
        dialog.open();
    }
}
