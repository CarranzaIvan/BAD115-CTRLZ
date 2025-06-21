package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.PreguntaDetalleDTO;
import com.bad.ctrlz.service.EncuestaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
        getStyle().set("padding", "2rem");

        // Título con estilo
        H2 titulo = new H2("📋 Preguntas de la Encuesta");
        titulo.getStyle()
              .set("color", "#1E3A8A")
              .set("font-weight", "bold");

        // Botón volver
        Button btnVolver = new Button("Volver", new Icon(VaadinIcon.ARROW_LEFT));
        btnVolver.getStyle()
                 .set("background-color", "#3B82F6")
                 .set("color", "white");
        btnVolver.addClickListener(e ->
            getUI().ifPresent(ui -> ui.getPage().getHistory().back())
        );

        // Botón dashboard
        Button btnDashboard = new Button("Dashboard", new Icon(VaadinIcon.LINES_LIST));
        btnDashboard.getStyle()
                    .set("background-color", "#2563EB")
                    .set("color", "white");
        btnDashboard.addClickListener(e ->
            getUI().ifPresent(ui -> ui.navigate("dashboard-respuestas"))
        );

        HorizontalLayout navegacion = new HorizontalLayout(btnVolver, btnDashboard);
        navegacion.setSpacing(true);
        navegacion.setAlignItems(FlexComponent.Alignment.CENTER);

        configurarGrid();

        // Contenedor expandible para el grid
        Div contenedorTabla = new Div();
        contenedorTabla.setSizeFull();
        grid.setSizeFull();
        contenedorTabla.add(grid);
        contenedorTabla.getStyle()
                .set("background-color", "white")
                .set("padding", "1.5rem")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.1)");

        // Asegurar que el contenedor crece
        setFlexGrow(1, contenedorTabla);

        add(navegacion, titulo, contenedorTabla);
    }

    private void configurarGrid() {
        grid.addColumn(PreguntaDetalleDTO::getTextoPregunta)
        .setHeader("Pregunta")
        .setFlexGrow(1)
        .setAutoWidth(false)
        .setWidth("50%");

        grid.addColumn(PreguntaDetalleDTO::getTipoPregunta)
            .setHeader("Tipo")
            .setAutoWidth(true)
            .setFlexGrow(0);


        grid.addComponentColumn(p -> {
            HorizontalLayout acciones = new HorizontalLayout();

            // Botón ver respuestas
            Button verRespuestas = new Button("Ver respuestas", new Icon(VaadinIcon.SEARCH));
            verRespuestas.getStyle()
                         .set("background-color", "#2563EB")
                         .set("color", "white");
            verRespuestas.addClickListener(e ->
                getUI().ifPresent(ui -> ui.navigate("ver-respuestas/" + p.getIdPregunta()))
            );
            acciones.add(verRespuestas);

            // Gráficos solo si no es abierta
            if (!"Abiertas".equalsIgnoreCase(p.getTipoPregunta())) {
                Button graficoBarras = new Button("Gráfico de Barras", new Icon(VaadinIcon.BAR_CHART));
                graficoBarras.getStyle()
                             .set("background-color", "#10B981") // verde
                             .set("color", "white");
                graficoBarras.addClickListener(e ->
                    getUI().ifPresent(ui -> ui.navigate("ver-grafico-barras/" + p.getIdPregunta()))
                );

                Button graficoPastel = new Button("Gráfico de Pastel", new Icon(VaadinIcon.PIE_CHART));
                graficoPastel.getStyle()
                             .set("background-color", "#F59E0B") // amarillo
                             .set("color", "white");
                graficoPastel.addClickListener(e ->
                    getUI().ifPresent(ui -> ui.navigate("ver-grafico-pastel/" + p.getIdPregunta()))
                );

                

                acciones.add(graficoBarras, graficoPastel);
            }

            return acciones;
        }).setHeader("Acciones")
        .setAutoWidth(true)
        .setFlexGrow(0);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String idParam = event.getRouteParameters().get("idEncuesta").orElse("0");
        Long idEncuesta = Long.parseLong(idParam);

        List<PreguntaDetalleDTO> preguntas = encuestaService.obtenerPreguntasPorEncuesta(idEncuesta);
        grid.setItems(preguntas);
    }
}
