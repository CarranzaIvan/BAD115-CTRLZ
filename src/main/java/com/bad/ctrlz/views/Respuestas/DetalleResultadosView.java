package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.PreguntaDetalleDTO;
import com.bad.ctrlz.service.EncuestaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("detalle-resultados/:idEncuesta")
@PageTitle("Detalle de Resultados por Encuesta")
@PermitAll
public class DetalleResultadosView extends VerticalLayout implements BeforeEnterObserver {

    private final EncuestaService encuestaService;
    private final Grid<PreguntaDetalleDTO> grid = new Grid<>(PreguntaDetalleDTO.class, false);

    @Autowired
    public DetalleResultadosView(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        add(new H2("Preguntas de la Encuesta"), grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idParam = event.getRouteParameters().get("idEncuesta").orElse("0");
        Long idEncuesta = Long.parseLong(idParam);

        List<PreguntaDetalleDTO> preguntas = encuestaService.obtenerPreguntasPorEncuesta(idEncuesta);

        grid.addColumn(PreguntaDetalleDTO::getTextoPregunta).setHeader("Pregunta");
        grid.addColumn(PreguntaDetalleDTO::getTipoPregunta).setHeader("Tipo");

        grid.addComponentColumn(p -> {
            HorizontalLayout acciones = new HorizontalLayout();

            Button verRespuestas = new Button("Ver respuestas");
            verRespuestas.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("ver-respuestas/" + p.getIdPregunta()))
            );

            Button verGrafico = new Button("Ver gráfico");
            verGrafico.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("ver-grafico/" + p.getIdPregunta()))
            );

            acciones.add(verRespuestas, verGrafico);
            return acciones;
        }).setHeader("Acciones");

        grid.setItems(preguntas);
    }
}
