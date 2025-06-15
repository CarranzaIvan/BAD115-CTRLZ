package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.RespuestaIndividualDTO;
import com.bad.ctrlz.service.RespuestaService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("ver-respuestas/:idPregunta")
@PageTitle("Respuestas de Pregunta")
@PermitAll
public class VerRespuestasPreguntaView extends VerticalLayout implements BeforeEnterObserver {

    private final RespuestaService respuestaService;
    private final Grid<RespuestaIndividualDTO> grid = new Grid<>(RespuestaIndividualDTO.class, false);

    @Autowired
    public VerRespuestasPreguntaView(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;

        setSizeFull();
        setSpacing(true);
        setPadding(true);

        add(new H2("Respuestas individuales"), grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Integer idPregunta = Integer.parseInt(event.getRouteParameters().get("idPregunta").orElse("0"));
        List<RespuestaIndividualDTO> respuestas = respuestaService.obtenerRespuestasPorPregunta(idPregunta);

        grid.addColumn(RespuestaIndividualDTO::getNombreUsuario).setHeader("Usuario");
        grid.addColumn(RespuestaIndividualDTO::getRespuesta).setHeader("Respuesta");
        grid.setItems(respuestas);
    }
}
