package com.bad.ctrlz.views.Preguntas;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.model.Opcion;
import com.bad.ctrlz.model.Pregunta;
import com.bad.ctrlz.model.TipoPregunta;
import com.bad.ctrlz.service.EncuestaService;
import com.bad.ctrlz.service.PreguntaService;
import com.bad.ctrlz.service.TipoPreguntaService;
import com.bad.ctrlz.service.OpcionService;
import com.bad.ctrlz.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Optional;
import com.vaadin.flow.component.textfield.IntegerField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route(value = ":idEncuesta/preguntas", layout = MainLayout.class)
@PageTitle("Preguntas de Encuesta")
public class CrearPregunta extends VerticalLayout implements BeforeEnterObserver {

    private final PreguntaService preguntaService;
    private final EncuestaService encuestaService;
    private final TipoPreguntaService tipoPreguntaService;
    private final OpcionService opcionService;

    private Grid<Pregunta> grid;
    private Integer idEncuesta;

    public CrearPregunta(PreguntaService preguntaService,
            EncuestaService encuestaService,
            TipoPreguntaService tipoPreguntaService,
            OpcionService opcionService) {
        this.preguntaService = preguntaService;
        this.encuestaService = encuestaService;
        this.tipoPreguntaService = tipoPreguntaService;
        this.opcionService = opcionService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 titulo = new H2("Preguntas de la Encuesta");
        add(titulo);

        // Contenedor centrado y con ancho limitado
        Div contenedor = new Div();
        contenedor.getStyle().set("max-width", "900px");
        contenedor.getStyle().set("margin", "0 auto");
        contenedor.getStyle().set("padding", "1rem");

        // Configuración del grid
        grid = new Grid<>(Pregunta.class, false);
        grid.setWidthFull();
        grid.addColumn(Pregunta::getIdPregunta).setHeader("ID").setAutoWidth(true);
        grid.addColumn(p -> {
            TipoPregunta tipo = p.getTipoPregunta();
            return tipo != null ? tipo.getNombreTipo() : "(sin tipo)";
        }).setHeader("Tipo").setAutoWidth(true);
        grid.addColumn(Pregunta::getTextoPregunta).setHeader("Texto").setFlexGrow(1);
        grid.addColumn(p -> p.getObligatorio().equalsIgnoreCase("S") ? "Sí" : "No")
                .setHeader("Obligatoria").setAutoWidth(true);

        // Columna de acciones
        grid.addComponentColumn(pregunta -> {
            HorizontalLayout acciones = new HorizontalLayout();
            acciones.setSpacing(true);

            Button btnVer = new Button(new Icon("lumo", "search"));
            btnVer.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY);
            btnVer.getElement().setProperty("title", "Ver");
            btnVer.addClickListener(e -> mostrarDetallePregunta(pregunta));

            Button btnEditar = new Button(new Icon("lumo", "edit"));
            btnEditar.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_TERTIARY);
            btnEditar.getElement().setProperty("title", "Editar");
            btnEditar.addClickListener(e -> mostrarFormularioEditar(pregunta));

            Button btnEliminar = new Button(new Icon("lumo", "cross"));
            btnEliminar.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            btnEliminar.getElement().setProperty("title", "Eliminar");
            btnEliminar.addClickListener(e -> confirmarEliminar(pregunta));

            acciones.add(btnVer, btnEditar, btnEliminar);
            return acciones;
        }).setHeader("Acciones").setAutoWidth(true);

        contenedor.add(grid);
        contenedor.setWidthFull();
        contenedor.setHeight("auto");

        add(contenedor);

        // Botón agregar
        Button btnAgregar = new Button("Agregar Pregunta", e -> mostrarFormulario());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAgregar.getStyle().set("margin", "1rem auto 0 auto");
        add(btnAgregar);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String param = event.getRouteParameters().get("idEncuesta").orElse(null);
        if (param != null) {
            try {
                idEncuesta = Integer.valueOf(param);
                actualizarGrid();
            } catch (NumberFormatException ex) {
                Notification.show("ID de encuesta inválido.");
            }
        }
    }

    private void actualizarGrid() {
        List<Pregunta> preguntas = preguntaService.listarPorEncuesta(idEncuesta);
        grid.setItems(preguntas);
    }

    private void mostrarFormulario() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nueva Pregunta");

        // Obtenemos de la base sólo los tipos permitidos
        List<String> nombresPermitidos = Arrays.asList("Abiertas", "Cerradas", "Mixtas");
        List<TipoPregunta> tiposPermitidos = tipoPreguntaService.listarTodos().stream()
                .filter(tp -> nombresPermitidos.contains(tp.getNombreTipo()))
                .toList(); // o .collect(Collectors.toList());

        ComboBox<TipoPregunta> comboTipo = new ComboBox<>("Tipo de Pregunta");
        comboTipo.setItems(tiposPermitidos);
        comboTipo.setItemLabelGenerator(TipoPregunta::getNombreTipo);
        comboTipo.setWidthFull();

        // SEGUNDO NIVEL
        ComboBox<String> comboCerradaTipo = new ComboBox<>("Cerrada tipo:");
        comboCerradaTipo.setItems("Elección única", "Elección múltiple", "Ranking", "Escala");
        comboCerradaTipo.setWidthFull();
        comboCerradaTipo.setVisible(false);

        // TERCER NIVEL
        ComboBox<String> comboCerradaEleccionUnicaTipo = new ComboBox<>("Elección única tipo:");
        comboCerradaEleccionUnicaTipo.setItems("Dicotómica", "Politómica");
        comboCerradaEleccionUnicaTipo.setWidthFull();
        comboCerradaEleccionUnicaTipo.setVisible(false);

        // CUARTO NIVEL
        ComboBox<String> comboCerradaEscalaTipo = new ComboBox<>("Escala tipo:");
        comboCerradaEscalaTipo.setItems("Numérica", "Nominal", "Likert");
        comboCerradaEscalaTipo.setWidthFull();
        comboCerradaEscalaTipo.setVisible(false);


        //Creamos el objeto de seccionOpciones
        List<Opcion> opcionesTemp = new ArrayList<>();
        VerticalLayout seccionOpciones = agregarSeccionOpciones(opcionesTemp,comboCerradaEleccionUnicaTipo,comboTipo);
        seccionOpciones.setVisible(false);



        // CAMPOS COMUNES
        TextArea textoPregunta = new TextArea("Texto de la Pregunta");
        textoPregunta.setWidthFull();
        textoPregunta.setMaxLength(1000);

        Checkbox chkObligatoria = new Checkbox("¿Es obligatoria?");
        NumberField inicioEscala = new NumberField("Valor inicio escala");
        NumberField finEscala = new NumberField("Valor fin escala");
        NumberField incremento = new NumberField("Incremento escala");

        inicioEscala.setVisible(false);
        finEscala.setVisible(false);
        incremento.setVisible(false);

        // Lógica de habilitación en cascada
        comboTipo.addValueChangeListener(event -> {
            TipoPregunta valor = event.getValue();

            if (valor != null && "Cerradas".equals(valor.getNombreTipo())) {
                comboCerradaTipo.setVisible(true);
            } else if (valor != null && "Mixtas".equals(valor.getNombreTipo())){
                comboCerradaTipo.setVisible(false);
                seccionOpciones.setVisible(true);
            }
            else {
                comboCerradaTipo.setVisible(false);
                seccionOpciones.setVisible(false);
            }
            comboCerradaTipo.clear();
            comboCerradaEleccionUnicaTipo.setVisible(false);
            comboCerradaEscalaTipo.setVisible(false);
            inicioEscala.setVisible(false);
            finEscala.setVisible(false);
            incremento.setVisible(false);
        });

        comboCerradaTipo.addValueChangeListener(event -> {
            comboCerradaEleccionUnicaTipo.clear();
            comboCerradaEscalaTipo.clear();
            
            String valor = event.getValue();

            // Primero limpiamos todo
            comboCerradaEleccionUnicaTipo.clear();
            comboCerradaEscalaTipo.clear();
            inicioEscala.clear();
            finEscala.clear();
            incremento.clear();
            opcionesTemp.clear();  // limpiar la lista de opciones en memoria
            seccionOpciones.removeAll();  // limpiar visualmente el layout


            // Ocultamos todo por defecto
            comboCerradaEleccionUnicaTipo.setVisible(false);
            comboCerradaEscalaTipo.setVisible(false);
            inicioEscala.setVisible(false);
            finEscala.setVisible(false);
            incremento.setVisible(false);

            //Cargamos nuevamente la seccion de opciones
            VerticalLayout nuevaSeccion = agregarSeccionOpciones(opcionesTemp,comboCerradaEleccionUnicaTipo,comboTipo);
            seccionOpciones.add(nuevaSeccion);

            // Ahora evaluamos cada sección
            if ("Elección única".equals(valor)) {
                comboCerradaEleccionUnicaTipo.setVisible(true);

            }

            if ("Elección múltiple".equals(valor)) {
                // Aquí irán los campos para Elección múltiple en el futuro
                seccionOpciones.setVisible(true);
            }

            if ("Ranking".equals(valor)) {
                // Aquí irán los campos para Ranking en el futuro
                seccionOpciones.setVisible(true);
            }

            if ("Escala".equals(valor)) {
                comboCerradaEscalaTipo.setVisible(true);
                seccionOpciones.setVisible(false);
            }
        });

        comboCerradaEleccionUnicaTipo.addValueChangeListener(event -> {
            String valor = event.getValue();

            // Siempre limpiar y ocultar primero
            opcionesTemp.clear();
            seccionOpciones.removeAll();


            //Cargamos nuevamente la seccion de opciones
            VerticalLayout nuevaSeccion = agregarSeccionOpciones(opcionesTemp,comboCerradaEleccionUnicaTipo,comboTipo);
            seccionOpciones.add(nuevaSeccion);

            // Activar campos según selección
            if ("Dicotómica".equals(valor)) {
                seccionOpciones.setVisible(true);
            }

            if ("Politómica".equals(valor)) {
                // Aquí podrías agregar futuros campos para Politómica
                seccionOpciones.setVisible(true);
            }
        });

        comboCerradaEscalaTipo.addValueChangeListener(event -> {
            String valor = event.getValue();

            // Primero siempre limpiamos todos los campos
            inicioEscala.clear();
            finEscala.clear();
            incremento.clear();
            opcionesTemp.clear();
            seccionOpciones.removeAll();

            //Cargamos nuevamente la seccion de opciones
            VerticalLayout nuevaSeccion = agregarSeccionOpciones(opcionesTemp,comboCerradaEleccionUnicaTipo,comboTipo);
            seccionOpciones.add(nuevaSeccion);

            inicioEscala.setVisible(false);
            finEscala.setVisible(false);
            incremento.setVisible(false);

            // Ahora según el valor, habilitamos los que correspondan
            if ("Numérica".equals(valor)) {
                inicioEscala.setVisible(true);
                finEscala.setVisible(true);
                incremento.setVisible(true);
            }

            if ("Nominal".equals(valor)) {
                // Aquí podrías agregar futuros campos para Nominal
                seccionOpciones.setVisible(true);
            }

            if ("Likert".equals(valor)) {
                // Aquí podrías agregar futuros campos para Likert
                seccionOpciones.setVisible(true);
            }
        });

        VerticalLayout formulario = new VerticalLayout(
                comboTipo,
                comboCerradaTipo,
                comboCerradaEleccionUnicaTipo,
                comboCerradaEscalaTipo,
                textoPregunta,
                chkObligatoria,
                inicioEscala,
                finEscala,
                incremento,
                seccionOpciones);

        Button guardar = new Button("Guardar", e -> {
            if (comboTipo.isEmpty() || textoPregunta.isEmpty()) {
                Notification.show("Complete los campos obligatorios.", 3000, Notification.Position.MIDDLE);
                return;
            }

            Optional<Encuesta> encuestaOpt = encuestaService.buscarPorId(idEncuesta);
            if (encuestaOpt.isEmpty()) {
                Notification.show("Encuesta no encontrada.", 3000, Notification.Position.MIDDLE);
                return;
            }
            Encuesta encuesta = encuestaOpt.get();

            Pregunta nueva = new Pregunta();
            nueva.setEncuesta(encuesta);

            // Logica para establecer correctamente el tipoPregunta
            TipoPregunta tipoSeleccionado = comboTipo.getValue();

            // Si es cerradas, evaluamos el segundo nivel
            if ("Cerradas".equals(tipoSeleccionado.getNombreTipo())) {

                String subTipo = comboCerradaTipo.getValue();

                if ("Elección única".equals(subTipo)) {
                    String subTipoUnica = comboCerradaEleccionUnicaTipo.getValue();
                    tipoSeleccionado = tipoPreguntaService.buscarPorNombre(subTipoUnica);
                }

                else if ("Elección múltiple".equals(subTipo)) {
                    tipoSeleccionado = tipoPreguntaService.buscarPorNombre("Elección Múltiple");
                }

                else if ("Ranking".equals(subTipo)) {
                    tipoSeleccionado = tipoPreguntaService.buscarPorNombre("Ranking");
                }

                else if ("Escala".equals(subTipo)) {
                    String subTipoEscala = comboCerradaEscalaTipo.getValue();
                    tipoSeleccionado = tipoPreguntaService.buscarPorNombre(subTipoEscala);
                }

            }

            // Finalmente seteamos el tipo exacto
            nueva.setTipoPregunta(tipoSeleccionado);

            nueva.setTextoPregunta(textoPregunta.getValue());
            nueva.setObligatorio(chkObligatoria.getValue() ? "S" : "N");
            nueva.setValorInicioEscala(inicioEscala.getValue());
            nueva.setValorFinEscala(finEscala.getValue());
            nueva.setIncrementoEscala(incremento.getValue());

            preguntaService.guardar(nueva);

            // Ahora guardamos las opciones si existen
            for (Opcion opcion : opcionesTemp) {
                opcion.setPregunta(nueva); // muy importante la relación
                opcionService.guardar(opcion);
            }
            dialog.close();
            actualizarGrid();
            Notification.show("Pregunta agregada correctamente.");
        });

        Button cancelar = new Button("Cancelar", e -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar);
        dialog.getFooter().add(acciones);
        dialog.setWidth("600px");
        dialog.setHeight("auto");

        dialog.add(formulario);
        dialog.open();
    }

    private void confirmarEliminar(Pregunta pregunta) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirmar eliminación");

        Button confirmar = new Button("Eliminar", e -> {
            preguntaService.eliminar(pregunta.getIdPregunta());
            confirmDialog.close();
            actualizarGrid();
            Notification.show("Pregunta eliminada correctamente", 3000, Notification.Position.TOP_CENTER);
        });

        confirmar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelar = new Button("Cancelar", e -> confirmDialog.close());

        confirmDialog.add("¿Está seguro de eliminar esta pregunta?");
        confirmDialog.getFooter().add(confirmar, cancelar);
        confirmDialog.open();
    }

    private void mostrarDetallePregunta(Pregunta pregunta) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Detalle de Pregunta");

        TextField id = new TextField("ID");
        id.setValue(String.valueOf(pregunta.getIdPregunta()));
        id.setReadOnly(true);

        TextField tipo = new TextField("Tipo de Pregunta");
        tipo.setValue(pregunta.getTipoPregunta() != null ? pregunta.getTipoPregunta().getNombreTipo() : "(sin tipo)");
        tipo.setReadOnly(true);

        TextArea texto = new TextArea("Texto de la Pregunta");
        texto.setValue(pregunta.getTextoPregunta() != null ? pregunta.getTextoPregunta() : "");
        texto.setReadOnly(true);
        texto.setWidthFull();

        Checkbox obligatorio = new Checkbox("¿Es obligatoria?");
        obligatorio.setValue("S".equalsIgnoreCase(pregunta.getObligatorio()));
        obligatorio.setReadOnly(true);

        NumberField inicio = new NumberField("Valor inicio escala");
        inicio.setValue(pregunta.getValorInicioEscala());
        inicio.setReadOnly(true);

        NumberField fin = new NumberField("Valor fin escala");
        fin.setValue(pregunta.getValorFinEscala());
        fin.setReadOnly(true);

        NumberField incremento = new NumberField("Incremento escala");
        incremento.setValue(pregunta.getIncrementoEscala());
        incremento.setReadOnly(true);

        VerticalLayout contenido = new VerticalLayout(
                id, tipo, texto, obligatorio, inicio, fin, incremento);

        Button cerrar = new Button("Cerrar", e -> dialog.close());
        cerrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(contenido);
        dialog.setWidth("600px");
        dialog.setHeight("auto");

        dialog.getFooter().add(cerrar);
        dialog.open();
    }

    private void mostrarFormularioEditar(Pregunta pregunta) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Editar Pregunta");
        dialog.setWidth("600px");

        // Cargar los tipos principales (Abierta, Cerrada, Mixta)
        List<TipoPregunta> tiposPrincipales = tipoPreguntaService.listarTodos().stream()
                .filter(tp -> List.of(1, 2, 3).contains(tp.getIdTipoPregunta()))
                .toList();

        ComboBox<TipoPregunta> comboTipo = new ComboBox<>("Tipo de Pregunta");
        comboTipo.setItems(tiposPrincipales);
        comboTipo.setItemLabelGenerator(TipoPregunta::getNombreTipo);
        comboTipo.setWidthFull();

        ComboBox<String> comboCerradaTipo = new ComboBox<>("Cerrada tipo:");
        comboCerradaTipo.setItems("Elección única", "Elección múltiple", "Ranking", "Escala");
        comboCerradaTipo.setWidthFull();

        ComboBox<String> comboCerradaEleccionUnicaTipo = new ComboBox<>("Elección única tipo:");
        comboCerradaEleccionUnicaTipo.setItems("Dicotómica", "Politómica");
        comboCerradaEleccionUnicaTipo.setWidthFull();

        ComboBox<String> comboCerradaEscalaTipo = new ComboBox<>("Escala tipo:");
        comboCerradaEscalaTipo.setItems("Numérica", "Nominal", "Likert");
        comboCerradaEscalaTipo.setWidthFull();

        TextArea textoPregunta = new TextArea("Texto de la Pregunta");
        textoPregunta.setWidthFull();
        textoPregunta.setMaxLength(1000);
        textoPregunta.setValue(pregunta.getTextoPregunta() != null ? pregunta.getTextoPregunta() : "");

        Checkbox chkObligatoria = new Checkbox("¿Es obligatoria?");
        chkObligatoria.setValue("S".equalsIgnoreCase(pregunta.getObligatorio()));

        NumberField inicioEscala = new NumberField("Valor inicio escala");
        inicioEscala.setValue(pregunta.getValorInicioEscala());
        inicioEscala.setVisible(false);

        NumberField finEscala = new NumberField("Valor fin escala");
        finEscala.setValue(pregunta.getValorFinEscala());
        finEscala.setVisible(false);

        NumberField incremento = new NumberField("Incremento escala");
        incremento.setValue(pregunta.getIncrementoEscala());
        incremento.setVisible(false);

        // Obtenemos el ID real de la pregunta
        TipoPregunta tipoActual = pregunta.getTipoPregunta();
        int id = tipoActual.getIdTipoPregunta();

        // Primero ocultamos todo
        comboCerradaTipo.setVisible(false);
        comboCerradaEleccionUnicaTipo.setVisible(false);
        comboCerradaEscalaTipo.setVisible(false);

        // Logica completa según ID
        if (id == 1) { // Abierta
            comboTipo.setValue(tipoPreguntaService.buscarPorId(1));
        } else if (id == 2) { // Cerradas base
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
        } else if (id == 3) { // Mixtas
            comboTipo.setValue(tipoPreguntaService.buscarPorId(3));
        } else if (id == 4) { // Elección única base
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Elección única");
        } else if (id == 5) { // Elección múltiple
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Elección múltiple");
        } else if (id == 6) { // Ranking
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Ranking");
        } else if (id == 7) { // Escala base
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Escala");
        } else if (id == 8) { // Dicotómica
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Elección única");
            comboCerradaEleccionUnicaTipo.setVisible(true);
            comboCerradaEleccionUnicaTipo.setValue("Dicotómica");
        } else if (id == 9) { // Politómica
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Elección única");
            comboCerradaEleccionUnicaTipo.setVisible(true);
            comboCerradaEleccionUnicaTipo.setValue("Politómica");
        } else if (id == 10) { // Numérica
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Escala");
            comboCerradaEscalaTipo.setVisible(true);
            comboCerradaEscalaTipo.setValue("Numérica");
            inicioEscala.setVisible(true);
            finEscala.setVisible(true);
            incremento.setVisible(true);
        } else if (id == 11) { // Nominal
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Escala");
            comboCerradaEscalaTipo.setVisible(true);
            comboCerradaEscalaTipo.setValue("Nominal");
        } else if (id == 12) { // Likert
            comboTipo.setValue(tipoPreguntaService.buscarPorId(2));
            comboCerradaTipo.setVisible(true);
            comboCerradaTipo.setValue("Escala");
            comboCerradaEscalaTipo.setVisible(true);
            comboCerradaEscalaTipo.setValue("Likert");
        }

        // Los combos siempre serán de solo lectura
        comboTipo.setReadOnly(true);
        comboCerradaTipo.setReadOnly(true);
        comboCerradaEleccionUnicaTipo.setReadOnly(true);
        comboCerradaEscalaTipo.setReadOnly(true);

        VerticalLayout formulario = new VerticalLayout(
                comboTipo,
                comboCerradaTipo,
                comboCerradaEleccionUnicaTipo,
                comboCerradaEscalaTipo,
                textoPregunta,
                chkObligatoria,
                inicioEscala,
                finEscala,
                incremento);

        Button guardar = new Button("Guardar", e -> {
            if (comboTipo.isEmpty() || textoPregunta.isEmpty()) {
                Notification.show("Complete los campos obligatorios.", 3000, Notification.Position.MIDDLE);
                return;
            }

            pregunta.setTextoPregunta(textoPregunta.getValue());
            pregunta.setObligatorio(chkObligatoria.getValue() ? "S" : "N");
            pregunta.setValorInicioEscala(inicioEscala.getValue());
            pregunta.setValorFinEscala(finEscala.getValue());
            pregunta.setIncrementoEscala(incremento.getValue());

            preguntaService.guardar(pregunta);
            dialog.close();
            actualizarGrid();
            Notification.show("Pregunta actualizada correctamente.", 3000, Notification.Position.TOP_CENTER);
        });

        Button cancelar = new Button("Cancelar", e -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout acciones = new HorizontalLayout(guardar, cancelar);
        dialog.getFooter().add(acciones);
        dialog.add(formulario);
        dialog.open();
    }


    private VerticalLayout agregarSeccionOpciones(List<Opcion> opcionesTemp, ComboBox<String> comboCerradaEleccionUnicaTipo, ComboBox<TipoPregunta> comboTipo) {
        
        Grid<Opcion> gridOpciones = new Grid<>(Opcion.class, false);
        gridOpciones.setHeight("200px");
        
        gridOpciones.addColumn(Opcion::getTextoOpcion).setHeader("Texto").setAutoWidth(true);
        gridOpciones.addColumn(Opcion::getOrden).setHeader("Correlativo").setAutoWidth(true);

        Grid.Column<Opcion> columnaSeleccion = gridOpciones.addComponentColumn(opcion -> {
        Checkbox checkOtro = new Checkbox();
        checkOtro.setValue(opcion.getesOtro());

        checkOtro.addValueChangeListener(event -> {
            if (event.getValue()) {
                for (Opcion op : opcionesTemp) {
                    op.setesOtro(false);
                }
                opcion.setesOtro(true);
                gridOpciones.getDataProvider().refreshAll();
            } else {
                opcion.setesOtro(false);
            }
        });

        return checkOtro;
        }).setHeader("Selección");
        
        //Visibilidad de la columna seleccion
        columnaSeleccion.setVisible(false);
        comboTipo.addValueChangeListener(e -> {
            boolean esMixta = e.getValue() != null && "Mixtas".equals(e.getValue().getNombreTipo());
            columnaSeleccion.setVisible(esMixta);

        });
        
        gridOpciones.addComponentColumn(opcion -> {
            Button eliminar = new Button(new Icon("lumo", "cross"));
            eliminar.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
            eliminar.addClickListener(ev -> {
                opcionesTemp.remove(opcion);
                gridOpciones.setItems(opcionesTemp);
            });
            return eliminar;
        }).setHeader("Acciones");

        TextField textoOpcion = new TextField("Texto de opción");
        IntegerField orden = new IntegerField("Correlativo");
        orden.setMin(1);
        orden.setStep(1);
        orden.setValue(1); // valor inicial
        orden.setWidth("150px");

        // Forzamos el atributo type="number" para habilitar las flechitas:
        orden.getElement().setAttribute("type", "number");

        Button btnAgregarOpcion = new Button("Agregar Opción", e -> {
            if (textoOpcion.isEmpty()) {
                Notification.show("Debe ingresar el texto de la opción.", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (orden.isEmpty()) {
                Notification.show("Debe ingresar el número de orden.", 3000, Notification.Position.MIDDLE);
                return;
            }

            int ordenIngresado = orden.getValue().intValue();

            if (ordenIngresado < 1) {
                Notification.show("El orden debe ser un número entero mayor o igual a 1.", 3000, Notification.Position.MIDDLE);
                return;
            }

            boolean yaExiste = opcionesTemp.stream()
                    .anyMatch(op -> op.getOrden() != null && op.getOrden().equals(ordenIngresado));

            if (yaExiste) {
                Notification.show("El orden ingresado ya está asignado a otra opción.", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Validación de máximo 2 cuando es Dicotómica
            if ("Dicotómica".equals(comboCerradaEleccionUnicaTipo.getValue()) && opcionesTemp.size() >= 2) {
                Notification.show("Solo puede ingresar máximo 2 opciones para preguntas dicotómicas");
                return;
            }

            Opcion nueva = new Opcion();
            nueva.setTextoOpcion(textoOpcion.getValue());
            nueva.setOrden(ordenIngresado);
            opcionesTemp.add(nueva);
            gridOpciones.setItems(opcionesTemp);

            textoOpcion.clear();
            orden.setValue(1);
        });

        HorizontalLayout filaBoton = new HorizontalLayout(btnAgregarOpcion);
        filaBoton.setWidthFull();
        filaBoton.setJustifyContentMode(JustifyContentMode.END);

        VerticalLayout seccion = new VerticalLayout( textoOpcion, orden, filaBoton, gridOpciones);
        seccion.setPadding(false);
        seccion.setSpacing(false);
        seccion.getStyle().set("border", "1px solid #ddd").set("padding", "10px");
        return seccion;
    }

}