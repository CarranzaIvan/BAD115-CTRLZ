package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.GraficoPreguntaDTO;
import com.bad.ctrlz.service.RespuestaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import elemental.json.Json;
import elemental.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("ver-grafico-barras")
@PageTitle("Gráfico de Barras")
@JsModule("https://cdn.jsdelivr.net/npm/chart.js")
public class GraficoBarrasView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RespuestaService respuestaService;

    @Autowired
    public GraficoBarrasView(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;

        setSizeFull();
        getStyle().set("padding", "2rem");

        // Título
        H2 titulo = new H2("📊 Gráfico de Barras");
        titulo.getStyle()
              .set("color", "#1E3A8A")
              .set("font-weight", "bold");

        // Botones
        Button btnVolver = new Button("Volver", new Icon(VaadinIcon.ARROW_LEFT));
        btnVolver.getStyle().set("background-color", "#3B82F6").set("color", "white");
        btnVolver.addClickListener(e -> getUI().ifPresent(ui -> ui.getPage().getHistory().back()));

        Button btnDashboard = new Button("Dashboard", new Icon(VaadinIcon.LINES_LIST));
        btnDashboard.getStyle().set("background-color", "#2563EB").set("color", "white");
        btnDashboard.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("dashboard-respuestas")));

        Button btnInicio = new Button("Inicio", new Icon(VaadinIcon.HOME));
        btnInicio.getStyle().set("background-color", "#1E40AF").set("color", "white");
        btnInicio.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        HorizontalLayout navegacion = new HorizontalLayout(btnVolver, btnDashboard, btnInicio);
        navegacion.setSpacing(true);
        navegacion.setAlignItems(FlexComponent.Alignment.CENTER);

        add(navegacion, titulo);
    }

    @Override
    public void setParameter(BeforeEvent event, Integer idPregunta) {
        List<GraficoPreguntaDTO> datos = respuestaService.obtenerGraficoPorPregunta(idPregunta);

        JsonArray labels = Json.createArray();
        JsonArray data = Json.createArray();
        JsonArray bgColors = Json.createArray();
        JsonArray borderColors = Json.createArray();

        // Colores rotativos
        String[] colores = {
            "rgba(54, 162, 235, 0.5)",
            "rgba(255, 99, 132, 0.5)",
            "rgba(255, 206, 86, 0.5)",
            "rgba(75, 192, 192, 0.5)",
            "rgba(153, 102, 255, 0.5)",
            "rgba(255, 159, 64, 0.5)"
        };

        String[] bordeColores = {
            "rgba(54, 162, 235, 1)",
            "rgba(255, 99, 132, 1)",
            "rgba(255, 206, 86, 1)",
            "rgba(75, 192, 192, 1)",
            "rgba(153, 102, 255, 1)",
            "rgba(255, 159, 64, 1)"
        };

        int index = 0;
        for (GraficoPreguntaDTO dto : datos) {
            labels.set(index, Json.create(dto.getEtiqueta()));
            data.set(index, Json.create(dto.getCantidad()));
            bgColors.set(index, Json.create(colores[index % colores.length]));
            borderColors.set(index, Json.create(bordeColores[index % bordeColores.length]));
            index++;
        }

        // Estilo del gráfico
        Div chartContainer = new Div();
        chartContainer.setId("chartBar");
        chartContainer.setWidth("100%");
        chartContainer.setHeight("500px");
        chartContainer.getStyle()
            .set("background-color", "white")
            .set("padding", "1.5rem")
            .set("border-radius", "12px")
            .set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.1)")
            .set("display", "flex")
            .set("justify-content", "center")
            .set("align-items", "center");

        setFlexGrow(1, chartContainer);
        add(chartContainer);

        // Script JS para dibujar
        getElement().executeJs("""
            const contenedor = document.getElementById('chartBar');
            contenedor.innerHTML = '';
            const canvas = document.createElement('canvas');
            canvas.style.maxWidth = '800px';
            canvas.style.maxHeight = '400px';
            contenedor.appendChild(canvas);

            const ctx = canvas.getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: $0,
                    datasets: [{
                        label: 'Respuestas',
                        data: $1,
                        backgroundColor: $2,
                        borderColor: $3,
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: { y: { beginAtZero: true } },
                    plugins: { legend: { display: true } }
                }
            });
        """, labels, data, bgColors, borderColors);
    }
}
