package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.service.EncuestaService;
import com.bad.ctrlz.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
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
import java.util.UUID;

@Route(value = "dashboard-encuestas", layout = MainLayout.class)
@PageTitle("Dashboard de Encuestas")
public class DashboardEncuestasView extends VerticalLayout {

    private final EncuestaService encuestaService;

    public DashboardEncuestasView(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;

        configurarLayout();
        construirVista();
    }

    private void configurarLayout() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        getStyle().set("background-color", "#f5f5f5");
        getStyle().set("font-family", "Arial, sans-serif");
    }

    private void construirVista() {
        Button crearBtn = new Button("Crear Encuesta", event -> getUI().ifPresent(ui -> ui.navigate("crear-encuesta")));
        crearBtn.getStyle()
                .set("background-color", "#007bff")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("margin-bottom", "20px")
                .set("border-radius", "5px")
                .set("padding", "10px 20px");

        H2 titulo = new H2("Gestión de Encuestas");
        titulo.getStyle().set("margin-top", "0").set("color", "#333");

        add(crearBtn, titulo);
        cargarEncuestas();
    }

    private void cargarEncuestas() {
        List<Encuesta> encuestas = encuestaService.obtenerTodas();

        if (encuestas.isEmpty()) {
            add(new Span("No hay encuestas registradas."));
            return;
        }

        for (Encuesta encuesta : encuestas) {
            add(crearCardEncuesta(encuesta));
        }
    }

    private Component crearCardEncuesta(Encuesta encuesta) {
        Div card = new Div();
        card.getStyle()
                .set("padding", "20px")
                .set("margin-bottom", "20px")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)")
                .set("background-color", "#fff")
                .set("max-width", "800px")
                .set("margin-left", "auto")
                .set("margin-right", "auto");

        Span titulo = new Span("Título: " + encuesta.getTitulo());
        Span objetivo = new Span("Objetivo: " + encuesta.getObjetivo());
        Span grupoMeta = new Span("Grupo Meta: " + encuesta.getGrupoMeta());
        Span estado = new Span("Estado: " + ("A".equals(encuesta.getEstado()) ? "Activo" : "Inactivo"));

        titulo.getStyle().set("font-weight", "bold").set("font-size", "18px");
        objetivo.getStyle().set("display", "block").set("margin-top", "5px");
        grupoMeta.getStyle().set("display", "block").set("margin-top", "5px");
        estado.getStyle().set("display", "block").set("margin-top", "5px");

        HorizontalLayout acciones = crearBotonesAcciones(encuesta);

        card.add(titulo, objetivo, grupoMeta, estado, acciones);
        return card;
    }

    private HorizontalLayout crearBotonesAcciones(Encuesta encuesta) {
        Button editarBtn = crearBoton("Editar", "#007bff", "white", () -> editarEncuesta(encuesta));
        Button eliminarBtn = crearBoton("Eliminar", "#dc3545", "white", () -> confirmarEliminar(encuesta));
        Button agregarPreguntasBtn = crearBoton("Agregar Preguntas", "#ffc107", "black", () -> agregarPreguntas(encuesta));
        Button generarLinkBtn = crearBoton("Generar Link", "#28a745", "white", () -> generarLinkEncuesta(encuesta));

        HorizontalLayout acciones = new HorizontalLayout(editarBtn, eliminarBtn, agregarPreguntasBtn, generarLinkBtn);
        acciones.getStyle().set("margin-top", "15px");
        return acciones;
    }

    private Button crearBoton(String texto, String colorFondo, String colorTexto, Runnable accion) {
        Button boton = new Button(texto, event -> accion.run());
        boton.getStyle()
                .set("background-color", colorFondo)
                .set("color", colorTexto)
                .set("border", "none")
                .set("border-radius", "5px")
                .set("padding", "8px 15px")
                .set("font-weight", "bold");
        return boton;
    }

    private void editarEncuesta(Encuesta encuesta) {
        getUI().ifPresent(ui -> ui.navigate("editar-encuesta/" + encuesta.getIdEncuesta()));
    }

    private void agregarPreguntas(Encuesta encuesta) {
        getUI().ifPresent(ui -> ui.navigate(encuesta.getIdEncuesta() + "/preguntas"));
    }

    private void confirmarEliminar(Encuesta encuesta) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirmar eliminación");

        Button confirmar = crearBoton("Eliminar", "#dc3545", "white", () -> {
            encuestaService.eliminar(encuesta);
            confirmDialog.close();
            Notification.show("Encuesta eliminada correctamente", 3000, Notification.Position.TOP_CENTER);
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        Button cancelar = crearBoton("Cancelar", "#6c757d", "white", confirmDialog::close);

        HorizontalLayout botones = new HorizontalLayout(confirmar, cancelar);
        confirmDialog.add(new Span("¿Está seguro de eliminar esta encuesta?"), botones);
        confirmDialog.open();
    }

    private void generarLinkEncuesta(Encuesta encuesta) {
        String baseUrl = "http://localhost:8081/public/encuesta/";

        if (encuesta.getLinkPublico() != null && !encuesta.getLinkPublico().isEmpty()) {
            mostrarDialogoLink(baseUrl + encuesta.getLinkPublico());
        } else {
            String nuevoLink = UUID.randomUUID().toString();
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
        copiarBtn.addClickListener(e -> {
            linkField.getElement().executeJs("navigator.clipboard.writeText($0);", linkField.getValue());
            copiarBtn.setText("¡Copiado!");
        });

        Button cerrarBtn = new Button("Cerrar", e -> dialog.close());

        HorizontalLayout botones = new HorizontalLayout(copiarBtn, cerrarBtn);
        dialog.add(label, linkField, botones);
        dialog.open();
    }
}
