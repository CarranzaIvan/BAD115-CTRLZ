package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.DashboardEncuestaDTO;
import com.bad.ctrlz.service.EncuestaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

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
        getStyle().set("padding", "2rem");

        // Título estilizado
        H2 titulo = new H2("📊 Dashboard de Respuestas por Encuesta");
        titulo.getStyle()
              .set("text-align", "center")
              .set("color", "#1E3A8A")
              .set("font-weight", "bold");

        // Botón Volver con ícono
        Button btnVolver = new Button("Volver", new Icon(VaadinIcon.ARROW_LEFT));
        btnVolver.getStyle()
                 .set("background-color", "#3B82F6")
                 .set("color", "white");
        btnVolver.addClickListener(e ->
            getUI().ifPresent(ui -> ui.getPage().getHistory().back())
        );

        // Botón Inicio con ícono
        Button btnInicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
        btnInicio.getStyle()
                 .set("background-color", "#2563EB")
                 .set("color", "white");
        btnInicio.addClickListener(e ->
            getUI().ifPresent(ui -> ui.navigate("inicio"))
        );

        HorizontalLayout navegacion = new HorizontalLayout(btnVolver, btnInicio);
        navegacion.setSpacing(true);
        navegacion.setJustifyContentMode(JustifyContentMode.START);
        navegacion.setAlignItems(FlexComponent.Alignment.CENTER);

        // Configurar y cargar datos del grid
        configurarGrid();
        cargarDatos();

        // Caja blanca con sombra para la tabla
        Div contenedorTabla = new Div();
        contenedorTabla.setWidthFull(); // Expande el div al 100%
        grid.setSizeFull();             // Expande también el grid
        contenedorTabla.add(grid);

        contenedorTabla.getStyle()
                    .set("background-color", "white")
                    .set("padding", "1.5rem")
                    .set("border-radius", "12px")
                    .set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.1)");


        add(navegacion, titulo, contenedorTabla);
    }

    private void configurarGrid() {
        grid.addColumn(DashboardEncuestaDTO::getIdEncuesta)
             .setHeader("ID Encuesta")
             .setAutoWidth(true);

        grid.addColumn(DashboardEncuestaDTO::getTitulo)
             .setHeader("Título")
             .setAutoWidth(true);

        grid.addColumn(DashboardEncuestaDTO::getTotalRespuestas)
             .setHeader("Total de Respuestas")
             .setAutoWidth(true);

        // Columna de acciones con ícono de lupa
        grid.addComponentColumn(dto -> {
            Button verDetalle = new Button("Ver resultados", new Icon(VaadinIcon.SEARCH));
            verDetalle.getStyle()
                      .set("background-color", "#1D4ED8")
                      .set("color", "white")
                      .set("border-radius", "6px");
            verDetalle.addClickListener(e ->
                verDetalle.getUI().ifPresent(ui ->
                    ui.navigate("detalle-resultados/" + dto.getIdEncuesta())
                )
            );
            return verDetalle;
        }).setHeader("Acciones");

        grid.setAllRowsVisible(true);
        grid.setSizeFull();
    }

    private void cargarDatos() {
        List<DashboardEncuestaDTO> datos = encuestaService.obtenerDashboardRespuestas();
        grid.setItems(datos);
    }
}
