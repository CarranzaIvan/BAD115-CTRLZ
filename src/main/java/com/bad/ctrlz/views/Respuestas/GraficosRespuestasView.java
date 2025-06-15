package com.bad.ctrlz.views.Respuestas;

import com.bad.ctrlz.dto.GraficoPreguntaDTO;
import com.bad.ctrlz.service.RespuestaService;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import elemental.json.Json;
import elemental.json.JsonArray;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("ver-grafico/:idPregunta")
@PageTitle("Gráfico de Respuestas")
@PermitAll
@JsModule("https://cdn.jsdelivr.net/npm/chart.js") // Asegura carga externa
public class GraficosRespuestasView extends VerticalLayout implements BeforeEnterObserver {

    private final RespuestaService respuestaService;

    @Autowired
    public GraficosRespuestasView(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
        setSizeFull();
        setPadding(true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Integer idPregunta = Integer.parseInt(
                event.getRouteParameters().get("idPregunta").orElse("0")
        );

        List<GraficoPreguntaDTO> datos = respuestaService.obtenerGraficoPorPregunta(idPregunta);

        // Construir los datos para Chart.js
        JsonArray labels = Json.createArray();
        JsonArray data = Json.createArray();

        int index = 0;
        for (GraficoPreguntaDTO dato : datos) {
            String etiqueta = dato.getEtiqueta() != null ? dato.getEtiqueta() : "(sin respuesta)";
            labels.set(index, Json.create(etiqueta));
            data.set(index, Json.create(dato.getCantidad()));
            index++;
        }


        add(new H2("Distribución de respuestas"));

        Div chartDiv = new Div();
        chartDiv.setId("myChart");
        chartDiv.setWidth("600px");
        chartDiv.setHeight("400px");
        add(chartDiv);

        // Ejecutar Chart.js
        getElement().executeJs("""
            window.requestAnimationFrame(() => {
                const container = document.getElementById('myChart');
                container.innerHTML = ''; // Limpiar anteriores

                const canvas = document.createElement('canvas');
                canvas.id = 'canvasChart';
                container.appendChild(canvas);

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
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            });
        """, labels, data);
    }
}
