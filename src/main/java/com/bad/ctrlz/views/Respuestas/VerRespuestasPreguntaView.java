package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.RespuestaIndividualDTO;
import com.bad.ctrlz.service.RespuestaService;
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
import com.bad.ctrlz.views.MainLayout;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value="ver-respuestas/:idPregunta", layout = MainLayout.class)
@PageTitle("Respuestas de Pregunta")
@PermitAll
public class VerRespuestasPreguntaView extends VerticalLayout implements BeforeEnterObserver {

    private final RespuestaService respuestaService;
    private final Grid<RespuestaIndividualDTO> grid = new Grid<>(RespuestaIndividualDTO.class, false);

    @Autowired
    public VerRespuestasPreguntaView(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;

        setSizeFull();
        getStyle().set("padding", "2rem");

        // Título con estilo
        H2 titulo = new H2("🗨️ Respuestas individuales");
        titulo.getStyle()
              .set("color", "#1E3A8A")
              .set("font-weight", "bold");

        // Botones de navegación arriba
        Button btnVolver = new Button("Volver", new Icon(VaadinIcon.ARROW_LEFT));
        btnVolver.getStyle()
                 .set("background-color", "#3B82F6")
                 .set("color", "white");
        btnVolver.addClickListener(e ->
            getUI().ifPresent(ui -> ui.getPage().getHistory().back())
        );

        Button btnDashboard = new Button("Dashboard", new Icon(VaadinIcon.LINES_LIST));
        btnDashboard.getStyle()
                    .set("background-color", "#2563EB")
                    .set("color", "white");
        btnDashboard.addClickListener(e ->
            getUI().ifPresent(ui -> ui.navigate("dashboard-respuestas"))
        );

        Button btnInicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
        btnInicio.getStyle()
                 .set("background-color", "#1E40AF")
                 .set("color", "white");
        btnInicio.addClickListener(e ->
            getUI().ifPresent(ui -> ui.navigate("inicio"))
        );

        HorizontalLayout navegacion = new HorizontalLayout(btnVolver, btnDashboard, btnInicio);
        navegacion.setSpacing(true);
        navegacion.setAlignItems(FlexComponent.Alignment.CENTER);

        configurarGrid();

        // Contenedor blanco con sombra para el grid
        Div contenedorTabla = new Div();
        contenedorTabla.setSizeFull();
        grid.setSizeFull();
        contenedorTabla.add(grid);
        contenedorTabla.getStyle()
                .set("background-color", "white")
                .set("padding", "1.5rem")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.1)");

        setFlexGrow(1, contenedorTabla);

        add(navegacion, titulo, contenedorTabla);
    }

    private void configurarGrid() {
        grid.addColumn(RespuestaIndividualDTO::getNombreUsuario)
            .setHeader("Usuario")
            .setAutoWidth(true)
            .setFlexGrow(0);

        grid.addColumn(RespuestaIndividualDTO::getRespuesta)
            .setHeader("Respuesta")
            .setFlexGrow(1);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Integer idPregunta = Integer.parseInt(event.getRouteParameters().get("idPregunta").orElse("0"));
        List<RespuestaIndividualDTO> respuestas = respuestaService.obtenerRespuestasPorPregunta(idPregunta);
        grid.setItems(respuestas);
    }
}
