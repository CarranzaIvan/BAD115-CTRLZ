package com.bad.ctrlz.views.Encuestas;

import com.bad.ctrlz.model.*;
import com.bad.ctrlz.service.*;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.*;

@Route("public/encuesta")
@PageTitle("Responder Encuesta")
public class ResponderEncuestaPublicaView extends VerticalLayout implements HasUrlParameter<String> {

    private final EncuestaService encuestaService;
    private final PreguntaService preguntaService;
    private final OpcionService opcionService;
    private final RespuestaService respuestaService;
    private final RespuestaEncuestaService respuestaEncuestaService;

    private final Map<Pregunta, Component> componentesPorPregunta = new HashMap<>();
    private Encuesta encuestaActual;

    @Autowired
    public ResponderEncuestaPublicaView(EncuestaService encuestaService, PreguntaService preguntaService,
            OpcionService opcionService, RespuestaService respuestaService,
            RespuestaEncuestaService respuestaEncuestaService) {
        this.encuestaService = encuestaService;
        this.preguntaService = preguntaService;
        this.opcionService = opcionService;
        this.respuestaService = respuestaService;
        this.respuestaEncuestaService = respuestaEncuestaService;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("background-color", "#f3f6fb");
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        encuestaService.buscarPorLinkPublico(parameter).ifPresentOrElse(encuesta -> {
            if ("A".equals(encuesta.getEstado())) {
                encuestaActual = encuesta;
                mostrarEncuesta(encuesta);
            } else {
                mostrarMensaje("Esta encuesta no está disponible actualmente.");
            }
        }, () -> {
            mostrarMensaje("Encuesta no encontrada o link inválido.");
        });
    }

    private void mostrarEncuesta(Encuesta encuesta) {
        Div container = new Div();
        container.getStyle().set("max-width", "900px").set("width", "95%").set("max-height", "85vh")
                .set("overflow-y", "auto")
                .set("margin", "20px auto").set("padding", "30px").set("background-color", "white")
                .set("border-radius", "15px").set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)");

        H2 titulo = new H2(encuesta.getTitulo());
        titulo.getStyle().set("margin-bottom", "10px").set("color", "#1565c0").set("font-weight", "bold");

        Paragraph objetivo = new Paragraph("Objetivo: " + encuesta.getObjetivo());
        objetivo.getStyle().set("margin-bottom", "5px").set("font-size", "16px");

        Paragraph grupo = new Paragraph("Grupo Meta: " + encuesta.getGrupoMeta());
        grupo.getStyle().set("margin-bottom", "20px").set("font-size", "16px");

        container.add(titulo, objetivo, grupo);

        List<Pregunta> preguntas = preguntaService.obtenerPreguntasPorEncuesta(encuesta.getIdEncuesta());
        for (Pregunta pregunta : preguntas) {
            Div preguntaCard = crearPreguntaCard(pregunta);
            container.add(preguntaCard);
        }

        add(container);

        Button enviar = new Button("Enviar Respuestas", e -> guardarRespuestas());
        enviar.getStyle().set("margin-top", "20px").set("background-color", "#1565c0").set("color", "white")
                .set("font-weight", "bold");
        add(enviar);
    }

    private Div crearPreguntaCard(Pregunta pregunta) {
        Div preguntaCard = new Div();
        preguntaCard.getStyle().set("border", "1px solid #ddd").set("border-radius", "8px").set("padding", "20px")
                .set("margin-bottom", "20px").set("background-color", "#fafafa");

        H4 labelPregunta = new H4(pregunta.getTextoPregunta());
        labelPregunta.getStyle().set("margin-bottom", "10px").set("color", "#333");

        preguntaCard.add(labelPregunta);
        Integer idTipo = pregunta.getTipoPregunta().getIdTipoPregunta();

        switch (idTipo) {
            case 1 -> {
                TextField campoTexto = new TextField();
                campoTexto.setWidthFull();
                preguntaCard.add(campoTexto);
                componentesPorPregunta.put(pregunta, campoTexto);
            }
            case 8, 9 -> {
                RadioButtonGroup<Opcion> radioUnica = new RadioButtonGroup<>();
                List<Opcion> opcionesUnica = opcionService.obtenerOpcionesPorPregunta(pregunta.getIdPregunta());
                radioUnica.setItems(opcionesUnica);
                radioUnica.setItemLabelGenerator(Opcion::getTextoOpcion);
                radioUnica.setWidthFull();
                preguntaCard.add(radioUnica);
                componentesPorPregunta.put(pregunta, radioUnica);
            }
            case 5 -> {
                CheckboxGroup<Opcion> multiple = new CheckboxGroup<>();
                List<Opcion> opcionesMultiple = opcionService.obtenerOpcionesPorPregunta(pregunta.getIdPregunta());
                multiple.setItems(opcionesMultiple);
                multiple.setItemLabelGenerator(Opcion::getTextoOpcion);
                multiple.setWidthFull();
                preguntaCard.add(multiple);
                componentesPorPregunta.put(pregunta, multiple);
            }
            case 6 -> {
                VerticalLayout rankingLayout = new VerticalLayout();
                rankingLayout.setWidth("100%");
                rankingLayout.getStyle().set("border", "1px solid #ccc").set("padding", "10px");

                List<Opcion> opcionesRanking = opcionService.obtenerOpcionesPorPregunta(pregunta.getIdPregunta());
                for (Opcion opcion : opcionesRanking) {
                    Div item = new Div();
                    item.setText(opcion.getTextoOpcion());
                    item.getElement().setAttribute("data-id-opcion", opcion.getIdOpcion().toString());
                    item.getStyle().set("padding", "10px").set("border", "1px solid #ddd").set("margin", "5px")
                            .set("cursor", "grab").set("background-color", "#f9f9f9");
                    DragSource<Div> dragSource = DragSource.create(item);
                    dragSource.setDraggable(true);
                    DropTarget<Div> dropTarget = DropTarget.create(item);
                    dropTarget.addDropListener(event -> event.getDragSourceComponent().ifPresent(dragged -> {
                        int draggedIndex = rankingLayout.indexOf(dragged);
                        int targetIndex = rankingLayout.indexOf(item);
                        rankingLayout.remove(dragged);
                        rankingLayout.addComponentAtIndex(targetIndex, dragged);
                    }));
                    rankingLayout.add(item);
                }
                preguntaCard.add(new Label("Arrastra para ordenar:"), rankingLayout);
                componentesPorPregunta.put(pregunta, rankingLayout);
            }
            case 10 -> {
                Double min = pregunta.getValorInicioEscala();
                Double max = pregunta.getValorFinEscala();
                Double step = pregunta.getIncrementoEscala();

                Div escalaLayout = new Div();
                escalaLayout.getStyle().set("width", "100%");

                Div labels = new Div();
                labels.getStyle().set("display", "flex").set("justify-content", "space-between");
                labels.add(new Span("Mín: " + min), new Span("Máx: " + max));
                escalaLayout.add(labels);

                Span valorActual = new Span(String.valueOf(min));
                valorActual.getStyle().set("font-weight", "bold");

                Element inputRange = new Element("input");
                inputRange.setAttribute("type", "range");
                inputRange.setAttribute("min", String.valueOf(min));
                inputRange.setAttribute("max", String.valueOf(max));
                inputRange.setAttribute("step", String.valueOf(step));
                inputRange.setAttribute("value", String.valueOf(min));
                inputRange.getStyle().set("width", "100%");

                // Agregamos sincronización al servidor
                inputRange.addEventListener("input", e -> {
                    inputRange.setProperty("serverValue", inputRange.getProperty("value"));
                });

                inputRange.executeJs("this.addEventListener('input', function() { $0.textContent = this.value; });",
                        valorActual.getElement());

                Div sliderDiv = new Div();
                sliderDiv.getElement().appendChild(inputRange);
                escalaLayout.add(sliderDiv, valorActual);
                preguntaCard.add(escalaLayout);

                componentesPorPregunta.put(pregunta, sliderDiv);
            }

            case 11 -> {
                ComboBox<Opcion> comboNominal = new ComboBox<>();
                List<Opcion> opcionesNominal = opcionService.obtenerOpcionesPorPregunta(pregunta.getIdPregunta());
                comboNominal.setItems(opcionesNominal);
                comboNominal.setItemLabelGenerator(Opcion::getTextoOpcion);
                comboNominal.setWidthFull();
                preguntaCard.add(comboNominal);
                componentesPorPregunta.put(pregunta, comboNominal);
            }
            case 12 -> {
                RadioButtonGroup<Opcion> likert = new RadioButtonGroup<>();
                List<Opcion> opcionesLikert = opcionService.obtenerOpcionesPorPregunta(pregunta.getIdPregunta());
                likert.setItems(opcionesLikert);
                likert.setItemLabelGenerator(Opcion::getTextoOpcion);
                likert.setWidthFull();
                preguntaCard.add(likert);
                componentesPorPregunta.put(pregunta, likert);
            }
            case 3 -> {
                VerticalLayout mixtoLayout = new VerticalLayout();
                mixtoLayout.setSpacing(false);
                mixtoLayout.setPadding(false);
                mixtoLayout.setWidthFull();

                RadioButtonGroup<Opcion> radioMixto = new RadioButtonGroup<>();
                List<Opcion> opcionesMixto = opcionService.obtenerOpcionesPorPregunta(pregunta.getIdPregunta());
                radioMixto.setItems(opcionesMixto);
                radioMixto.setItemLabelGenerator(Opcion::getTextoOpcion);
                radioMixto.setWidthFull();

                TextField otroCampo = new TextField("Especifique:");
                otroCampo.setWidthFull();
                otroCampo.setVisible(false);

                radioMixto.addValueChangeListener(event -> {
                    Opcion seleccion = event.getValue();
                    boolean mostrarOtro = seleccion != null && Boolean.TRUE.equals(seleccion.getEsOtro());

                    otroCampo.setVisible(mostrarOtro);
                    if (!mostrarOtro) {
                        otroCampo.clear();
                    }

                    mixtoLayout.getElement().executeJs("this.requestLayout && this.requestLayout()");
                });

                mixtoLayout.add(radioMixto, otroCampo);
                preguntaCard.add(mixtoLayout);
                componentesPorPregunta.put(pregunta, mixtoLayout);
            }
            default -> preguntaCard.add(new Label("Tipo de pregunta no implementado."));
        }
        return preguntaCard;
    }

    private void guardarRespuestas() {
        boolean validacionFallida = false;

        // Primero limpiamos todos los estilos de error
        componentesPorPregunta.forEach((pregunta, componente) -> {
            componente.getParent().ifPresent(parent -> parent.getElement().getStyle().remove("border"));
        });

        for (Map.Entry<Pregunta, Component> entry : componentesPorPregunta.entrySet()) {
            Pregunta pregunta = entry.getKey();
            Component componente = entry.getValue();
            boolean esObligatoria = "S".equalsIgnoreCase(pregunta.getObligatorio());

            Integer idTipo = pregunta.getTipoPregunta().getIdTipoPregunta();
            boolean invalido = false;

            switch (idTipo) {
                case 1 -> {
                    String val = ((TextField) componente).getValue();
                    invalido = esObligatoria && (val == null || val.isBlank());
                }
                case 8, 9 -> {
                    Opcion seleccion = ((RadioButtonGroup<Opcion>) componente).getValue();
                    invalido = esObligatoria && (seleccion == null);
                }
                case 5 -> {
                    CheckboxGroup<Opcion> multiple = (CheckboxGroup<Opcion>) componente;
                    invalido = esObligatoria && multiple.getSelectedItems().isEmpty();
                }
                case 6 -> {
                    // En ranking no validamos, porque siempre habrá orden
                }
                case 10 -> {

                }
                case 11 -> {
                    Opcion seleccion = ((ComboBox<Opcion>) componente).getValue();
                    invalido = esObligatoria && (seleccion == null);
                }
                case 12 -> {
                    Opcion seleccion = ((RadioButtonGroup<Opcion>) componente).getValue();
                    invalido = esObligatoria && (seleccion == null);
                }
                case 3 -> {
                    VerticalLayout mixtoLayout = (VerticalLayout) componente;
                    RadioButtonGroup<Opcion> radioMixto = (RadioButtonGroup<Opcion>) mixtoLayout.getComponentAt(0);
                    TextField otroCampo = (TextField) mixtoLayout.getComponentAt(1);

                    Opcion seleccion = radioMixto.getValue();
                    invalido = esObligatoria && (seleccion == null);

                    if (seleccion != null && Boolean.TRUE.equals(seleccion.getEsOtro())) {
                        invalido = invalido || (otroCampo.getValue() == null || otroCampo.getValue().isBlank());
                    }
                }
            }

            if (invalido) {
                componente.getParent()
                        .ifPresent(parent -> parent.getElement().getStyle().set("border", "2px solid red"));
                validacionFallida = true;
            }
        }

        if (validacionFallida) {
            Notification.show("Debe completar todas las preguntas obligatorias.", 4000, Notification.Position.MIDDLE);
            return;
        }

        // Si pasó la validación, ahora sí guardamos normalmente
        try {
            RespuestaEncuesta respuestaEncuesta = new RespuestaEncuesta();
            respuestaEncuesta.setFechaEnvio(LocalDateTime.now());
            respuestaEncuesta.setEncuesta(encuestaActual);
            respuestaEncuestaService.guardar(respuestaEncuesta);

            for (Map.Entry<Pregunta, Component> entry : componentesPorPregunta.entrySet()) {
                Pregunta pregunta = entry.getKey();
                Component componente = entry.getValue();
                Integer idTipo = pregunta.getTipoPregunta().getIdTipoPregunta();

                Respuesta respuesta = new Respuesta();
                respuesta.setPregunta(pregunta);
                respuesta.setRespuestaEncuesta(respuestaEncuesta);

                switch (idTipo) {
                    case 1 -> respuesta.setTextoAbierto(((TextField) componente).getValue());
                    case 8, 9 -> respuesta.setOpcion(((RadioButtonGroup<Opcion>) componente).getValue());
                    case 5 -> {
                        CheckboxGroup<Opcion> multiple = (CheckboxGroup<Opcion>) componente;
                        for (Opcion seleccion : multiple.getSelectedItems()) {
                            Respuesta respMultiple = new Respuesta();
                            respMultiple.setPregunta(pregunta);
                            respMultiple.setRespuestaEncuesta(respuestaEncuesta);
                            respMultiple.setOpcion(seleccion);
                            respuestaService.guardar(respMultiple);
                        }
                        continue;
                    }
                    case 6 -> {
                        VerticalLayout rankingLayout = (VerticalLayout) componente;
                        int posicion = 1;
                        for (Component child : rankingLayout.getChildren().toList()) {
                            String idOpcion = child.getElement().getAttribute("data-id-opcion");
                            Respuesta respRanking = new Respuesta();
                            respRanking.setPregunta(pregunta);
                            respRanking.setRespuestaEncuesta(respuestaEncuesta);
                            respRanking.setPosicionRanking(posicion);
                            respRanking.setOpcion(opcionService.buscarPorId(Integer.valueOf(idOpcion)).get());
                            respuestaService.guardar(respRanking);
                            posicion++;
                        }
                        continue;
                    }
                    case 10 -> {
                        Div sliderDiv = (Div) componente;
                        Element inputElement = sliderDiv.getElement().getChild(0);
                        String valor = inputElement.getProperty("value");

                        if (valor == null || valor.isBlank()) {
                            valor = String.valueOf(pregunta.getValorInicioEscala());
                        }

                        Double valorDouble = Double.valueOf(valor);
                        respuesta.setValorEscala(valorDouble.intValue());

                    }
                    case 11 -> respuesta.setOpcion(((ComboBox<Opcion>) componente).getValue());
                    case 12 -> respuesta.setOpcion(((RadioButtonGroup<Opcion>) componente).getValue());
                    case 3 -> {
                        VerticalLayout mixtoLayout = (VerticalLayout) componente;
                        RadioButtonGroup<Opcion> radioMixto = (RadioButtonGroup<Opcion>) mixtoLayout.getComponentAt(0);
                        TextField otroCampo = (TextField) mixtoLayout.getComponentAt(1);

                        Opcion seleccion = radioMixto.getValue();
                        respuesta.setOpcion(seleccion);

                        if (seleccion != null && Boolean.TRUE.equals(seleccion.getEsOtro())) {
                            respuesta.setTextoAbierto(otroCampo.getValue());
                        }
                    }
                }

                respuestaService.guardar(respuesta);
            }

            Notification.show("Respuestas guardadas correctamente.", 4000, Notification.Position.MIDDLE);
        } catch (Exception ex) {
            ex.printStackTrace();
            Notification.show("Error al guardar: " + ex.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }

    private void mostrarMensaje(String mensaje) {
        Div errorCard = new Div();
        errorCard.getStyle().set("background-color", "white").set("border-radius", "10px").set("padding", "40px")
                .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)").set("max-width", "600px").set("width", "100%");

        H2 mensajeError = new H2(mensaje);
        mensajeError.getStyle().set("color", "#d32f2f");
        errorCard.add(mensajeError);
        add(errorCard);
    }
}