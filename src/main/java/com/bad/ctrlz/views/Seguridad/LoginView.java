package com.bad.ctrlz.views.Seguridad;

import org.springframework.beans.factory.annotation.Autowired;

import com.bad.ctrlz.model.Usuario;
import com.bad.ctrlz.service.UsuarioService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.data.validator.EmailValidator;


@Route("login")
@PageTitle("Bienvenido | CtrlZ")
@CssImport("./styles/auth-view.css")
public class LoginView extends VerticalLayout {

    // Conexiones a bases de datos y servicios se pueden agregar aquí
    @Autowired
    private UsuarioService usuarioService;

    // Componentes de la vista
    private Tabs tabs; // Pestañas para iniciar sesión y registrarse
    private Div pages; // Contenedor para los formularios de inicio de sesión y registro
    private Tab loginTab; // Pestaña de inicio de sesión
    private Tab registerTab; // Pestaña de registro
    private Component loginForm; // Formulario de inicio de sesión
    private Component registerForm; // Formulario de registro
    private Component resetPasswordForm; // Formulario de recuperación de contraseña
    private boolean isResetPasswordVisible = false; // Bandera para controlar la visibilidad del formulario de
                                                    // recuperación de contraseña

    // Constructor de la vista
    public LoginView() {
        addClassName("auth-view"); // Clase CSS para estilos personalizados
        setSizeFull(); // Ocupa todo el espacio disponible
        setAlignItems(Alignment.CENTER); // Alinea los elementos al centro
        setJustifyContentMode(JustifyContentMode.CENTER); // Justifica el contenido al centro
        Div card = createCard(); // Crea el contenedor de la tarjeta de autenticación
        add(card); // Agrega la tarjeta a la vista
    }

    /**
     * Crea el contenedor principal de la tarjeta de autenticación.
     * Contiene el título, descripción y los formularios de inicio de sesión y
     * registro.
     */
    private Div createCard() {
        Div card = new Div(); // Contenedor principal de la tarjeta
        card.addClassName("auth-card"); // Clase CSS para estilos personalizados
        card.setWidth("400px"); // Ancho fijo de la tarjeta
        card.getStyle()
                .set("padding", "30px")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)")
                .set("background-color", "white");

        // Crear título y descripción
        H2 title = new H2("Bienvenido");
        title.addClassName("auth-title");
        title.getStyle()
                .set("text-align", "center")
                .set("color", "#2c3e50")
                .set("margin-bottom", "10px");

        // Descripción debajo del título
        Paragraph description = new Paragraph("Inicia sesión o crea una nueva cuenta");
        description.addClassName("auth-description");
        description.getStyle()
                .set("text-align", "center")
                .set("color", "#7f8c8d")
                .set("margin-bottom", "20px");

        // Contenedor para el título y descripción
        VerticalLayout header = new VerticalLayout(title, description);
        header.setPadding(false);
        header.setSpacing(false);
        header.setAlignItems(Alignment.CENTER);

        // Crear pestañas y contenido
        createTabs();
        createForms();

        // Configurar la pestaña de inicio de sesión como seleccionada por defecto
        VerticalLayout content = new VerticalLayout(header, tabs, pages); // Contenido principal de la tarjeta
        content.setPadding(true); // Espaciado interno
        content.setSpacing(true); // Espaciado entre componentes
        content.setAlignItems(Alignment.STRETCH); // Alinea los componentes al inicio

        card.add(content); // Agrega es contenido a la tarjeta
        return card;
    }

    // Método para crear las pestañas de inicio de sesión y registro
    private void createTabs() {
        loginTab = new Tab("Iniciar Sesión"); // Pestaña de inicio de sesión
        registerTab = new Tab("Registrarse"); // Pestaña de registro

        tabs = new Tabs(loginTab, registerTab); // Agrupa las pestañas
        tabs.setWidthFull(); // Ancho completo
        tabs.getStyle().set("margin-bottom", "20px");
        tabs.addSelectedChangeListener(event -> {
            if (isResetPasswordVisible) {
                showAuthTabs();
            } else {
                updateContent();
            }
        });

        pages = new Div(); // Contenedor para los formularios
        pages.setSizeFull(); // Ocupa todo el espacio disponible
    }

    /**
     * Crea los formularios de inicio de sesión, registro y recuperación de
     * contraseña.
     * Los agrega al contenedor de páginas y establece la visibilidad inicial.
     */
    private void createForms() {
        loginForm = createLoginForm(); // Formulario de inicio de sesión
        registerForm = createRegisterForm(); // Formulario de registro
        resetPasswordForm = createResetPasswordForm(); // Formulario de recuperación de contraseña

        pages.add(loginForm, registerForm, resetPasswordForm); // Agrega los formularios al contenedor de páginas
        loginForm.setVisible(true); // Muestra el formulario de inicio de sesión por defecto
        registerForm.setVisible(false); // Oculta el formulario de registro por defecto
        resetPasswordForm.setVisible(false); // Oculta el formulario de recuperación de contraseña por defecto
    }

    /**
     * Crea el formulario de inicio de sesión con campos de email y contraseña.
     * Incluye botones para iniciar sesión y recuperar contraseña.
     */
    private Component createLoginForm() {
        FormLayout formLayout = new FormLayout(); // Layout para el formulario
        formLayout.addClassName("auth-form"); // Clase CSS para estilos personalizados

        // Campo de email
        EmailField emailField = new EmailField("Email");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setWidthFull();
        emailField.setPlaceholder("tu@email.com");
        emailField.setRequired(true);
        emailField.getStyle().set("margin-bottom", "15px");

        // Campo de contraseña
        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField.setWidthFull();
        passwordField.setPlaceholder("••••••••");
        passwordField.setRequired(true);
        passwordField.getStyle().set("margin-bottom", "20px");

        // Botones de inicio de sesión
        Button loginButton = new Button("Iniciar Sesión");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidthFull();
        loginButton.getStyle()
                .set("height", "45px")
                .set("font-weight", "bold");

        // Botón para recuperar contraseña
        Button forgotPasswordButton = new Button("¿Olvidaste tu contraseña?");
        forgotPasswordButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        forgotPasswordButton.getStyle()
                .set("margin-top", "15px")
                .set("color", "#3498db");
        forgotPasswordButton.addClickListener(e -> showResetPasswordForm());

        // Eventos de prueba
        loginButton.addClickListener(event -> {
            System.out.println("LOGIN - Email: " + emailField.getValue());
            System.out.println("LOGIN - Password: " + passwordField.getValue());
            showSuccessNotification("¡Inicio de sesión exitoso!");
        });

        // Agregar los campos y botones al formulario
        formLayout.add(emailField, passwordField, loginButton, forgotPasswordButton);
        return formLayout;
    }

    /**
     * Crea el formulario de registro con campos para nombre, email, contraseña y
     * confirmación de contraseña.
     * Incluye un botón para crear la cuenta.
     */
    private Component createRegisterForm() {
        FormLayout formLayout = new FormLayout(); // Layout para el formulario de registro
        formLayout.addClassName("auth-form"); // Clase CSS para estilos personalizados

        // Crear Binder para validaciones
        Binder<Usuario> binder = new Binder<>(Usuario.class);

        // Campo de nombre
        TextField nameField = new TextField("Nombre");
        nameField.setPrefixComponent(VaadinIcon.USER.create());
        nameField.setWidthFull();
        nameField.setPlaceholder("Ingrese su nombre");
        nameField.setRequired(true);
        nameField.setMaxLength(100); // Límite de caracteres
        nameField.getStyle().set("margin-bottom", "15px");

        // Validaciones para nombre
        binder.forField(nameField)
                .withValidator(value -> value != null && !value.trim().isEmpty(),
                        "El nombre es obligatorio")
                .withValidator(value -> value.trim().length() >= 2,
                        "El nombre debe tener al menos 2 caracteres")
                .withValidator(value -> value.trim().length() <= 100,
                        "El nombre no puede exceder 100 caracteres")
                .withValidator(value -> value.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"),
                        "El nombre solo puede contener letras y espacios")
                .bind(Usuario::getNombre, Usuario::setNombre);

        // Campo de apellido
        TextField lastNameField = new TextField("Apellido");
        lastNameField.setPrefixComponent(VaadinIcon.USER.create());
        lastNameField.setWidthFull();
        lastNameField.setPlaceholder("Ingrese su apellido");
        lastNameField.setRequired(true);
        lastNameField.setMaxLength(100); // Límite de caracteres
        lastNameField.getStyle().set("margin-bottom", "15px");

        // Validaciones para apellido
        binder.forField(lastNameField)
                .withValidator(value -> value != null && !value.trim().isEmpty(),
                        "El apellido es obligatorio")
                .withValidator(value -> value.trim().length() >= 2,
                        "El apellido debe tener al menos 2 caracteres")
                .withValidator(value -> value.trim().length() <= 100,
                        "El apellido no puede exceder 100 caracteres")
                .withValidator(value -> value.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$"),
                        "El apellido solo puede contener letras y espacios")
                .bind(Usuario::getApellido, Usuario::setApellido);

        // Campo de email
        EmailField emailField = new EmailField("Email");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setWidthFull();
        emailField.setPlaceholder("tu@email.com");
        emailField.setRequired(true);
        emailField.setMaxLength(150); // Límite de caracteres
        emailField.getStyle().set("margin-bottom", "15px");

        // Validaciones para email
        binder.forField(emailField)
                .withValidator(value -> value != null && !value.trim().isEmpty(),
                        "El email es obligatorio")
                .withValidator(new EmailValidator("El formato del email no es válido"))
                .withValidator(value -> value.length() <= 150,
                        "El email no puede exceder 150 caracteres")
                .bind(Usuario::getCorreo, Usuario::setCorreo);

        // Campo de contraseña
        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField.setWidthFull();
        passwordField.setPlaceholder("••••••••");
        passwordField.setRequired(true);
        passwordField.setMaxLength(255); // Límite de caracteres
        passwordField.getStyle().set("margin-bottom", "15px");

        // Validaciones para contraseña
        binder.forField(passwordField)
                .withValidator(value -> value != null && !value.isEmpty(),
                        "La contraseña es obligatoria")
                .withValidator(value -> value.length() >= 6,
                        "La contraseña debe tener al menos 6 caracteres")
                .withValidator(value -> value.length() <= 255,
                        "La contraseña no puede exceder 255 caracteres")
                .withValidator(value -> value.matches(".*[A-Z].*"),
                        "La contraseña debe contener al menos una letra mayúscula")
                .withValidator(value -> value.matches(".*[a-z].*"),
                        "La contraseña debe contener al menos una letra minúscula")
                .withValidator(value -> value.matches(".*\\d.*"),
                        "La contraseña debe contener al menos un número")
                .withValidator(value -> !value.contains(" "),
                        "La contraseña no puede contener espacios")
                .bind(Usuario::getPassword, Usuario::setPassword);

        // Campo de confirmación de contraseña
        PasswordField confirmPasswordField = new PasswordField("Confirmar contraseña");
        confirmPasswordField.setPrefixComponent(VaadinIcon.LOCK.create());
        confirmPasswordField.setWidthFull();
        confirmPasswordField.setPlaceholder("••••••••");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setMaxLength(255); // Límite de caracteres
        confirmPasswordField.getStyle().set("margin-bottom", "20px");

        // Validación en tiempo real para confirmación de contraseña
        confirmPasswordField.addValueChangeListener(event -> {
            String password = passwordField.getValue();
            String confirmPassword = event.getValue();

            if (confirmPassword != null && !confirmPassword.isEmpty()) {
                if (!password.equals(confirmPassword)) {
                    confirmPasswordField.setErrorMessage("Las contraseñas no coinciden");
                    confirmPasswordField.setInvalid(true);
                } else {
                    confirmPasswordField.setInvalid(false);
                }
            }
        });

        // También validar cuando cambie la contraseña principal
        passwordField.addValueChangeListener(event -> {
            String password = event.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (confirmPassword != null && !confirmPassword.isEmpty()) {
                if (!password.equals(confirmPassword)) {
                    confirmPasswordField.setErrorMessage("Las contraseñas no coinciden");
                    confirmPasswordField.setInvalid(true);
                } else {
                    confirmPasswordField.setInvalid(false);
                }
            }
        });

        // Botón para crear cuenta
        Button registerButton = new Button("Crear Cuenta");
        registerButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        registerButton.setWidthFull();
        registerButton.getStyle()
                .set("height", "45px")
                .set("font-weight", "bold");

        // Evento del botón con validaciones completas
        registerButton.addClickListener(event -> {
            try {
                // Validar campos individuales primero
                if (!binder.validate().isOk()) {
                    Notification.show("Por favor, corrija los errores en el formulario",
                            3000, Notification.Position.TOP_CENTER);
                    return;
                }

                // Validar que las contraseñas coincidan
                if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
                    confirmPasswordField.setErrorMessage("Las contraseñas no coinciden");
                    confirmPasswordField.setInvalid(true);
                    Notification.show("Las contraseñas no coinciden",
                            3000, Notification.Position.TOP_CENTER);
                    return;
                }

                // Validar que la confirmación no esté vacía
                if (confirmPasswordField.getValue() == null || confirmPasswordField.getValue().trim().isEmpty()) {
                    confirmPasswordField.setErrorMessage("Debe confirmar la contraseña");
                    confirmPasswordField.setInvalid(true);
                    Notification.show("Debe confirmar la contraseña",
                            3000, Notification.Position.TOP_CENTER);
                    return;
                }

                // Crear y validar el usuario
                Usuario nuevoUsuario = new Usuario();

                // Usar el binder para poblar el objeto
                if (binder.writeBeanIfValid(nuevoUsuario)) {
                    // Limpiar espacios en blanco
                    nuevoUsuario.setNombre(nuevoUsuario.getNombre().trim());
                    nuevoUsuario.setApellido(nuevoUsuario.getApellido().trim());
                    nuevoUsuario.setCorreo(nuevoUsuario.getCorreo().trim().toLowerCase());

                    // Intentar guardar el usuario
                    boolean registrado = usuarioService.guardarUsuario(nuevoUsuario);

                    if (registrado) {
                        showSuccessNotification("¡Cuenta creada exitosamente!");
                        // Limpiar formulario
                        binder.setBean(new Usuario());
                        confirmPasswordField.clear();
                    } else {
                        Notification.show("El email ya está registrado",
                                3000, Notification.Position.TOP_CENTER);
                        emailField.focus();
                    }
                } else {
                    Notification.show("Por favor, corrija los errores en el formulario",
                            3000, Notification.Position.TOP_CENTER);
                }

            } catch (Exception e) {
                Notification.show("Error al crear la cuenta. Intente nuevamente.",
                        3000, Notification.Position.TOP_CENTER);
                e.printStackTrace(); // Para debugging
            }
        });

        // Configurar el bean inicial
        binder.setBean(new Usuario());

        formLayout.add(nameField, lastNameField, emailField, passwordField, confirmPasswordField, registerButton);
        return formLayout;
    }

    private Component createResetPasswordForm() {
        FormLayout formLayout = new FormLayout();
        formLayout.addClassName("auth-form");

        H2 resetTitle = new H2("Recuperar Contraseña");
        resetTitle.addClassName("auth-title");
        resetTitle.getStyle()
                .set("text-align", "center")
                .set("color", "#2c3e50")
                .set("margin-bottom", "10px");

        Paragraph resetDescription = new Paragraph("Ingresa tu email para recibir un enlace de recuperación");
        resetDescription.addClassName("auth-description");
        resetDescription.getStyle()
                .set("text-align", "center")
                .set("color", "#7f8c8d")
                .set("margin-bottom", "20px");

        EmailField emailField = new EmailField("Email");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setWidthFull();
        emailField.setPlaceholder("tu@email.com");
        emailField.setRequired(true);
        emailField.getStyle().set("margin-bottom", "20px");

        Button sendLinkButton = new Button("Enviar Enlace");
        sendLinkButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendLinkButton.setWidthFull();
        sendLinkButton.getStyle()
                .set("height", "45px")
                .set("font-weight", "bold");

        Button backButton = new Button("Volver al inicio de sesión");
        backButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        backButton.setWidthFull();
        backButton.getStyle().set("margin-top", "15px");
        backButton.addClickListener(e -> showAuthTabs());

        // Eventos de prueba
        sendLinkButton.addClickListener(event -> {
            System.out.println("RESET - Email: " + emailField.getValue());
            showSuccessNotification("Se ha enviado un enlace de recuperación a tu email.");
            showAuthTabs();
        });

        VerticalLayout header = new VerticalLayout(resetTitle, resetDescription);
        header.setPadding(false);
        header.setSpacing(false);
        header.setAlignItems(Alignment.CENTER);

        formLayout.add(header, emailField, sendLinkButton, backButton);
        return formLayout;
    }

    private void updateContent() {
        loginForm.setVisible(loginTab.isSelected());
        registerForm.setVisible(registerTab.isSelected());
    }

    private void showResetPasswordForm() {
        isResetPasswordVisible = true;
        tabs.setVisible(false);
        loginForm.setVisible(false);
        registerForm.setVisible(false);
        resetPasswordForm.setVisible(true);
    }

    private void showAuthTabs() {
        isResetPasswordVisible = false;
        tabs.setVisible(true);
        resetPasswordForm.setVisible(false);
        updateContent();
    }

    private void showSuccessNotification(String message) {
        Notification notification = new Notification(message, 3000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

    // private void showErrorNotification(String message) {
    // Notification notification = new Notification(message, 3000,
    // Notification.Position.TOP_CENTER);
    // notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    // notification.open();
    // }
}