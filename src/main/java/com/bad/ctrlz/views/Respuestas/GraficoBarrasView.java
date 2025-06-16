package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.GraficoPreguntaDTO;
import com.bad.ctrlz.service.RespuestaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
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
        setPadding(true);
        setSpacing(true);

        // Botones de navegación
        Button btnVolver = new Button("← Volver", e ->
            getUI().ifPresent(ui -> ui.getPage().getHistory().back())
        );

        Button btnDashboard = new Button("Ir a Dashboard", e ->
            getUI().ifPresent(ui -> ui.navigate("dashboard-respuestas"))
        );

        Button btnInicio = new Button("Ir a Inicio", e ->
            getUI().ifPresent(ui -> ui.navigate(""))
        );

        HorizontalLayout navegacion = new HorizontalLayout(btnVolver, btnDashboard, btnInicio);
        navegacion.setSpacing(true);

        add(navegacion);
    }

    @Override
    public void setParameter(BeforeEvent event, Integer idPregunta) {
        List<GraficoPreguntaDTO> datos = respuestaService.obtenerGraficoPorPregunta(idPregunta);

        JsonArray labels = Json.createArray();
        JsonArray data = Json.createArray();

        int index = 0;
        for (GraficoPreguntaDTO dto : datos) {
            labels.set(index, Json.create(dto.getEtiqueta()));
            data.set(index, Json.create(dto.getCantidad()));
            index++;
        }

        Div chartDiv = new Div();
        chartDiv.setId("chartBar");
        chartDiv.setWidth("600px");
        chartDiv.setHeight("400px");

        add(chartDiv);

        getElement().executeJs("""
            const contenedor = document.getElementById('chartBar');
            contenedor.innerHTML = '';
            const canvas = document.createElement('canvas');
            contenedor.appendChild(canvas);

            const ctx = canvas.getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: $0,
                    datasets: [{
                        label: 'Respuestas',
                        data: $1,
                        backgroundColor: 'rgba(54, 162, 235, 0.5)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: { y: { beginAtZero: true } },
                    plugins: { legend: { display: true } }
                }
            });
        """, labels, data);
    }
}