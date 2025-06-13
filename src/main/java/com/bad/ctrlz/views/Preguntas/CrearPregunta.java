package com.bad.ctrlz.views.Preguntas;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.model.Pregunta;
import com.bad.ctrlz.model.TipoPregunta;
import com.bad.ctrlz.service.EncuestaService;
import com.bad.ctrlz.service.PreguntaService;
import com.bad.ctrlz.service.TipoPreguntaService;
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


import java.util.List;

@Route(value = ":idEncuesta/preguntas", layout = MainLayout.class)
@PageTitle("Preguntas de Encuesta")
public class CrearPregunta extends VerticalLayout implements BeforeEnterObserver {

    private final PreguntaService preguntaService;
    private final EncuestaService encuestaService;
    private final TipoPreguntaService tipoPreguntaService;

    private Grid<Pregunta> grid;
    private Integer idEncuesta;

    public CrearPregunta(PreguntaService preguntaService,
                         EncuestaService encuestaService,
                         TipoPreguntaService tipoPreguntaService) {
        this.preguntaService = preguntaService;
        this.encuestaService = encuestaService;
        this.tipoPreguntaService = tipoPreguntaService;

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

        acciones.add(btnVer,btnEditar, btnEliminar);
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

        ComboBox<TipoPregunta> comboTipo = new ComboBox<>("Tipo de Pregunta");
        comboTipo.setItems(tipoPreguntaService.listarTodos());
        comboTipo.setItemLabelGenerator(TipoPregunta::getNombreTipo);
        comboTipo.setWidthFull();

        TextArea textoPregunta = new TextArea("Texto de la Pregunta");
        textoPregunta.setWidthFull();
        textoPregunta.setMaxLength(1000);

        Checkbox chkObligatoria = new Checkbox("¿Es obligatoria?");

        TextField validacion = new TextField("Regla de Validación");

        NumberField inicioEscala = new NumberField("Valor inicio escala");
        NumberField finEscala = new NumberField("Valor fin escala");
        NumberField incremento = new NumberField("Incremento escala");

        VerticalLayout formulario = new VerticalLayout(
                comboTipo,
                textoPregunta,
                chkObligatoria,
                validacion,
                inicioEscala,
                finEscala,
                incremento
        );

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
            nueva.setTipoPregunta(comboTipo.getValue());
            nueva.setTextoPregunta(textoPregunta.getValue());
            nueva.setObligatorio(chkObligatoria.getValue() ? "S" : "N");
            nueva.setValidacion(validacion.getValue());
            nueva.setValorInicioEscala(inicioEscala.getValue());
            nueva.setValorFinEscala(finEscala.getValue());
            nueva.setIncrementoEscala(incremento.getValue());

            preguntaService.guardar(nueva);
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

    TextField validacion = new TextField("Regla de Validación");
    validacion.setValue(pregunta.getValidacion() != null ? pregunta.getValidacion() : "");
    validacion.setReadOnly(true);

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
            id, tipo, texto, obligatorio, validacion, inicio, fin, incremento
    );

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

    ComboBox<TipoPregunta> comboTipo = new ComboBox<>("Tipo de Pregunta");
    comboTipo.setItems(tipoPreguntaService.listarTodos());
    comboTipo.setItemLabelGenerator(TipoPregunta::getNombreTipo);
    comboTipo.setValue(pregunta.getTipoPregunta());
    comboTipo.setWidthFull();

    TextArea textoPregunta = new TextArea("Texto de la Pregunta");
    textoPregunta.setValue(pregunta.getTextoPregunta() != null ? pregunta.getTextoPregunta() : "");
    textoPregunta.setWidthFull();
    textoPregunta.setMaxLength(1000);

    Checkbox chkObligatoria = new Checkbox("¿Es obligatoria?");
    chkObligatoria.setValue("S".equalsIgnoreCase(pregunta.getObligatorio()));

    TextField validacion = new TextField("Regla de Validación");
    validacion.setValue(pregunta.getValidacion() != null ? pregunta.getValidacion() : "");

    NumberField inicioEscala = new NumberField("Valor inicio escala");
    inicioEscala.setValue(pregunta.getValorInicioEscala());

    NumberField finEscala = new NumberField("Valor fin escala");
    finEscala.setValue(pregunta.getValorFinEscala());

    NumberField incremento = new NumberField("Incremento escala");
    incremento.setValue(pregunta.getIncrementoEscala());

    VerticalLayout formulario = new VerticalLayout(
            comboTipo, textoPregunta, chkObligatoria,
            validacion, inicioEscala, finEscala, incremento
    );

    Button guardar = new Button("Guardar", e -> {
        if (comboTipo.isEmpty() || textoPregunta.isEmpty()) {
            Notification.show("Complete los campos obligatorios.", 3000, Notification.Position.MIDDLE);
            return;
        }

        pregunta.setTipoPregunta(comboTipo.getValue());
        pregunta.setTextoPregunta(textoPregunta.getValue());
        pregunta.setObligatorio(chkObligatoria.getValue() ? "S" : "N");
        pregunta.setValidacion(validacion.getValue());
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



}
