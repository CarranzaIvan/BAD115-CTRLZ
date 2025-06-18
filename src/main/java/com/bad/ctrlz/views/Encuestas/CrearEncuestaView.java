package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.model.Usuario;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "crear-encuesta", layout = MainLayout.class)
@PageTitle("Crear Encuesta")
public class CrearEncuestaView extends VerticalLayout {

    private final EncuestaService encuestaService;
    private final Binder<Encuesta> binder = new Binder<>(Encuesta.class);

    public CrearEncuestaView(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        Div card = construirFormulario();
        add(card);

        //Boton regresar
        Button btnRegresar = new Button("← Volver");
        btnRegresar.getStyle().set("margin", "1rem auto 0 auto");
        btnRegresar.addClickListener(event ->
                btnRegresar.getUI().ifPresent(ui -> ui.navigate("dashboard-encuestas"))
        );
        add(btnRegresar);
    }

    private Div construirFormulario() {

        Div card = new Div();
        card.getStyle().set("width", "600px");
        card.getStyle().set("padding", "30px");
        card.getStyle().set("border-radius", "12px");
        card.getStyle().set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)");
        card.getStyle().set("background-color", "#ffffff");

        H2 tituloFormulario = new H2("Crear Nueva Encuesta");

        TextField titulo = new TextField("Título");
        titulo.setRequired(true);

        TextArea objetivo = new TextArea("Objetivo");
        objetivo.setHeight("100px");

        TextField grupoMeta = new TextField("Grupo Meta");

        ComboBox<String> estado = new ComboBox<>("Estado");
        estado.setItems("Activo", "Inactivo");
        estado.setRequired(true);

        Button guardarBtn = new Button("Guardar Encuesta");
        guardarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        guardarBtn.getStyle().set("font-size", "16px")
                              .set("padding", "10px 30px")
                              .set("border-radius", "8px");

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

        guardarBtn.addClickListener(event -> {
            if (binder.validate().isOk()) {
                Encuesta encuesta = new Encuesta();
                encuesta.setUsuario(obtenerUsuarioActual());
                binder.writeBeanIfValid(encuesta);
                encuestaService.guardar(encuesta);

                Notification.show("Encuesta creada correctamente", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                binder.readBean(new Encuesta());
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

    private Usuario obtenerUsuarioActual() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1);  // luego integrarás el login real
        return usuario;
    }
}
