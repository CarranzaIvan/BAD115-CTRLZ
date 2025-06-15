package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.views.MainLayout;
import com.bad.ctrlz.model.Respuesta;
import com.bad.ctrlz.service.RespuestaService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "ver-respuestas", layout = MainLayout.class)
@PageTitle("Respuestas de Encuesta")
public class VerRespuestasView extends VerticalLayout {

    private final RespuestaService respuestaService;
    private final Grid<Respuesta> grid = new Grid<>(Respuesta.class, false);


    @Autowired
    public VerRespuestasView(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;

        setSizeFull();
        configurarGrid();

        List<Respuesta> respuestas = respuestaService.obtenerTodasRespuestas();
        grid.setItems(respuestas);

        add(grid);
    }

    private void configurarGrid() {
    grid.addColumn(r -> r.getIdRespuesta()).setHeader("ID").setAutoWidth(true).setFlexGrow(0);

    grid.addColumn(r -> {
        if (r.getPregunta() != null) {
            return r.getPregunta().getTextoPregunta();
        } else {
            return "(Pregunta no disponible)";
        }
    }).setHeader("Pregunta").setAutoWidth(true).setFlexGrow(1);

    grid.addColumn(r -> {
        if (r.getOpcion() != null) {
            return r.getOpcion().getTextoOpcion();
        } else if (r.getTextoAbierto() != null) {
            return r.getTextoAbierto();
        } else if (r.getValorEscala() != null) {
            return r.getValorEscala().toString();
        } else {
            return "(Sin respuesta)";
        }
    }).setHeader("Respuesta").setAutoWidth(true).setFlexGrow(2);

    grid.setSizeFull();
    grid.setWidthFull();
}

}
