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

@Route("ver-grafico-pastel")
@PageTitle("Gráfico de Pastel")
@JsModule("https://cdn.jsdelivr.net/npm/chart.js")
public class GraficoPastelView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final RespuestaService respuestaService;

    @Autowired
    public GraficoPastelView(RespuestaService respuestaService) {
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
        chartDiv.setId("chartPie");
        chartDiv.setWidth("600px");
        chartDiv.setHeight("400px");

        add(chartDiv);

        getElement().executeJs("""
            const contenedor = document.getElementById('chartPie');
            contenedor.innerHTML = '';
            const canvas = document.createElement('canvas');
            contenedor.appendChild(canvas);

            const ctx = canvas.getContext('2d');
            new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: $0,
                    datasets: [{
                        label: 'Respuestas',
                        data: $1,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.5)',
                            'rgba(54, 162, 235, 0.5)',
                            'rgba(255, 206, 86, 0.5)',
                            'rgba(75, 192, 192, 0.5)',
                            'rgba(153, 102, 255, 0.5)',
                            'rgba(255, 159, 64, 0.5)'
                        ],
                        borderColor: 'rgba(255, 255, 255, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: { legend: { position: 'bottom' } }
                }
            });
        """, labels, data);
    }
}