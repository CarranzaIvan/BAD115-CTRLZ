package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.model.Respuesta;
import com.bad.ctrlz.service.RespuestaService;
import com.bad.ctrlz.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import elemental.json.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Route(value = "graficos-respuestas", layout = MainLayout.class)
@PageTitle("Gráfico de Respuestas")
public class GraficosRespuestasView extends VerticalLayout {

    @Autowired
    public GraficosRespuestasView(RespuestaService respuestaService) {

        List<Respuesta> respuestas = respuestaService.obtenerTodasRespuestas();

        Map<String, Long> conteoRespuestas = respuestas.stream()
                .filter(r -> r.getOpcion() != null)
                .collect(Collectors.groupingBy(r -> r.getOpcion().getTextoOpcion(), Collectors.counting()));

        JsonArray labels = Json.createArray();
        JsonArray data = Json.createArray();

        int index = 0;
        for (Map.Entry<String, Long> entry : conteoRespuestas.entrySet()) {
            labels.set(index, Json.create(entry.getKey()));
            data.set(index, Json.create(entry.getValue()));
            index++;
        }

        Div chartDiv = new Div();
        chartDiv.setId("myChart");
        chartDiv.setWidth("600px");
        chartDiv.setHeight("400px");
        add(chartDiv);

        // Esperamos que el Div esté completamente renderizado antes de ejecutar el Chart.js
        getElement().executeJs("""
            window.requestAnimationFrame(() => {
                const canvas = document.createElement('canvas');
                canvas.id = 'canvasChart';
                document.getElementById('myChart').appendChild(canvas);
                
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
                    scales: {
                    y: { beginAtZero: true }
                    }
                }
                });
            });
        """, labels, data);

    }
}
