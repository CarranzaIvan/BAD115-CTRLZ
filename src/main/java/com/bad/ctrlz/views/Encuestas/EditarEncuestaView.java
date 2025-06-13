package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.service.EncuestaService;
import com.bad.ctrlz.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;

import java.util.Optional;

@Route(value = "editar-encuesta/:id", layout = MainLayout.class)
@PageTitle("Editar Encuesta")
public class EditarEncuestaView extends VerticalLayout implements BeforeEnterObserver {

    private final EncuestaService encuestaService;
    private final Binder<Encuesta> binder = new Binder<>(Encuesta.class);
    private Encuesta encuestaExistente;

    // Campos reutilizables del formulario
    private final TextField titulo = new TextField("Título");
    private final TextArea objetivo = new TextArea("Objetivo");
    private final TextField grupoMeta = new TextField("Grupo Meta");
    private final ComboBox<String> estado = new ComboBox<>("Estado");

    public EditarEncuestaView(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        Div card = construirFormulario();
        add(card);
    }

    private Div construirFormulario() {
        Div card = new Div();
        card.getStyle().set("width", "600px");
        card.getStyle().set("padding", "30px");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)");
        card.getStyle().set("background-color", "#ffffff");

        H2 tituloFormulario = new H2("Editar Encuesta");

        titulo.setRequired(true);
        objetivo.setHeight("100px");
        estado.setItems("Activo", "Inactivo");
        estado.setRequired(true);

        // Binder igual al crear
        binder.forField(titulo)
                .asRequired("Debe ingresar el título")
                .bind(Encuesta::getTitulo, Encuesta::setTitulo);

        binder.forField(objetivo)
                .bind(Encuesta::getObjetivo, Encuesta::setObjetivo);

        binder.forField(grupoMeta)
                .bind(Encuesta::getGrupoMeta, Encuesta::setGrupoMeta);

        binder.forField(estado)
                .asRequired("Seleccione el estado")
                .withConverter(
                        s -> s.equals("Activo") ? "A" : "I",
                        c -> c.equals("A") ? "Activo" : "Inactivo"
                )
                .bind(Encuesta::getEstado, Encuesta::setEstado);

        Button guardarBtn = new Button("Actualizar Encuesta");
        guardarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardarBtn.getStyle().set("font-size", "16px")
                .set("padding", "10px 30px")
                .set("border-radius", "8px");

        guardarBtn.addClickListener(event -> {
            if (binder.validate().isOk()) {
                try {
                    binder.writeBean(encuestaExistente);
                    encuestaService.guardar(encuestaExistente);
                    Notification.show("Encuesta actualizada correctamente", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception e) {
                    Notification.show("Error al actualizar la encuesta", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } else {
                Notification.show("Complete los campos obligatorios", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        FormLayout formulario = new FormLayout(titulo, objetivo, grupoMeta, estado);
        formulario.setColspan(objetivo, 2);
        formulario.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        HorizontalLayout botones = new HorizontalLayout(guardarBtn);
        botones.setJustifyContentMode(JustifyContentMode.CENTER);
        botones.setWidthFull();
        botones.getStyle().set("margin-top", "25px");

        card.add(tituloFormulario, formulario, botones);
        return card;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idParam = event.getRouteParameters().get("id").orElse(null);
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                Optional<Encuesta> optionalEncuesta = encuestaService.buscarPorId(id);
                if (optionalEncuesta.isPresent()) {
                    encuestaExistente = optionalEncuesta.get();
                    binder.readBean(encuestaExistente);
                } else {
                    Notification.show("Encuesta no encontrada", 3000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    event.forwardTo(DashboardEncuestasView.class);
                }
            } catch (NumberFormatException e) {
                Notification.show("ID inválido", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                event.forwardTo(DashboardEncuestasView.class);
            }
        }
    }
}
