package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.service.EncuestaService;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import org.springframework.beans.factory.annotation.Autowired;

@Route("public/encuesta")
@PageTitle("Responder Encuesta")
public class ResponderEncuestaPublicaView extends VerticalLayout implements HasUrlParameter<String> {

    private final EncuestaService encuestaService;

    @Autowired
    public ResponderEncuestaPublicaView(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", "#f5f5f5");
        getStyle().set("padding", "50px");
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        encuestaService.buscarPorLinkPublico(parameter).ifPresentOrElse(encuesta -> {
            if ("A".equals(encuesta.getEstado())) {
                mostrarEncuesta(encuesta);
            } else {
                mostrarMensaje("Esta encuesta no está disponible actualmente.");
            }
        }, () -> {
            mostrarMensaje("Encuesta no encontrada o link inválido.");
        });
    }

    private void mostrarEncuesta(Encuesta encuesta) {
        Div card = new Div();
        card.getStyle()
            .set("background-color", "white")
            .set("border-radius", "10px")
            .set("padding", "40px")
            .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)")
            .set("max-width", "600px")
            .set("width", "100%");

        H2 titulo = new H2(encuesta.getTitulo());
        titulo.getStyle().set("margin-bottom", "20px").set("color", "#1565c0");

        Paragraph objetivo = new Paragraph("Objetivo: " + encuesta.getObjetivo());
        objetivo.getStyle().set("margin-bottom", "10px").set("font-size", "16px");

        Paragraph grupo = new Paragraph("Grupo Meta: " + encuesta.getGrupoMeta());
        grupo.getStyle().set("margin-bottom", "20px").set("font-size", "16px");

        card.add(titulo, objetivo, grupo);

        add(card);
    }

    private void mostrarMensaje(String mensaje) {
        Div errorCard = new Div();
        errorCard.getStyle()
            .set("background-color", "white")
            .set("border-radius", "10px")
            .set("padding", "40px")
            .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)")
            .set("max-width", "600px")
            .set("width", "100%");

        H2 mensajeError = new H2(mensaje);
        mensajeError.getStyle().set("color", "#d32f2f");

        errorCard.add(mensajeError);
        add(errorCard);
    }
}
