package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.DashboardEncuestaDTO;
import com.bad.ctrlz.service.EncuestaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route("dashboard-respuestas")
@PageTitle("Dashboard de Respuestas por Encuesta")
@PermitAll
public class DashboardRespuestasView extends VerticalLayout {

    private final EncuestaService encuestaService;
    private final Grid<DashboardEncuestaDTO> grid = new Grid<>(DashboardEncuestaDTO.class, false);

    @Autowired
    public DashboardRespuestasView(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Dashboard de Respuestas por Encuesta");

        // Botones de navegación
        Button btnVolver = new Button("Volver", e ->
            getUI().ifPresent(ui -> ui.getPage().getHistory().back())
        );

        Button btnInicio = new Button("Ir a Inicio", e ->
            getUI().ifPresent(ui -> ui.navigate(""))
        );

        HorizontalLayout navegacion = new HorizontalLayout(btnVolver, btnInicio);
        navegacion.setSpacing(true);

        cargarDatos();
        add(navegacion, titulo, configurarGrid());
    }


    private Grid<DashboardEncuestaDTO> configurarGrid() {
    grid.addColumn(DashboardEncuestaDTO::getIdEncuesta)
         .setHeader("ID Encuesta")
         .setAutoWidth(true);

    grid.addColumn(DashboardEncuestaDTO::getTitulo)
         .setHeader("Título")
         .setAutoWidth(true);

    grid.addColumn(DashboardEncuestaDTO::getTotalRespuestas)
         .setHeader("Total de Respuestas")
         .setAutoWidth(true);

    // Nueva columna de acciones
    grid.addComponentColumn(dto -> {
        Button verDetalle = new Button("Ver resultados");
        verDetalle.addClickListener(e ->
            verDetalle.getUI().ifPresent(ui ->
                ui.navigate("detalle-resultados/" + dto.getIdEncuesta())
            )
        );
        return verDetalle;
    }).setHeader("Acciones");

    grid.setAllRowsVisible(true);
    return grid;
}


    private void cargarDatos() {
        List<DashboardEncuestaDTO> datos = encuestaService.obtenerDashboardRespuestas();
        grid.setItems(datos);
    }
}
