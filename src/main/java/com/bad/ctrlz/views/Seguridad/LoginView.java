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
import com.vaadin.flow.component.tabs.TabsVariant;
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

        // Header dinámico que cambia según la vista
        private VerticalLayout dynamicHeader;
        private H2 mainTitle;
        private Paragraph mainDescription;

        // Constructor de la vista
        public LoginView() {
                addClassName("auth-view");
                addClassName("gradient-background"); // Nueva clase para el fondo
                setSizeFull();
                setMinHeight("100vh"); // Asegurar altura mínima completa
                setAlignItems(Alignment.CENTER);
                setJustifyContentMode(JustifyContentMode.CENTER);

                // Configurar el scroll y expansión automática
                getStyle().set("overflow-y", "auto");
                getStyle().set("min-height", "100vh");

                Div card = createCard(); // Crea el contenedor principal de la tarjeta
                card.addClassName("auth-card"); // Clase CSS para la tarjeta
                add(card);
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
                card.setMaxWidth("90vw"); // Responsivo en móviles
                card.getStyle()
                                .set("padding", "30px")
                                .set("border-radius", "10px")
                                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)")
                                .set("background-color", "white")
                                .set("margin", "20px auto") // Margen superior e inferior
                                .set("max-height", "90vh") // Altura máxima
                                .set("overflow-y", "auto"); // Scroll interno si es necesario

                // Crear header dinámico
                createDynamicHeader();

                // Crear pestañas y contenido
                createTabs();
                createForms();

                // Configurar la pestaña de inicio de sesión como seleccionada por defecto
                VerticalLayout content = new VerticalLayout(dynamicHeader, tabs, pages); // Contenido principal de la
                                                                                         // tarjeta
                content.setPadding(true); // Espaciado interno
                content.setSpacing(true); // Espaciado entre componentes
                content.setAlignItems(Alignment.STRETCH); // Alinea los componentes al inicio

                card.add(content); // Agrega es contenido a la tarjeta
                return card;
        }

        /**
         * Crea el header dinámico que cambia según la vista activa
         */
        private void createDynamicHeader() {
                // Crear título y descripción principales
                mainTitle = new H2("Bienvenido");
                mainTitle.addClassName("auth-title");
                mainTitle.getStyle()
                                .set("text-align", "center")
                                .set("color", "#2c3e50")
                                .set("margin-bottom", "10px");

                // Descripción debajo del título
                mainDescription = new Paragraph("Inicia sesión o crea una nueva cuenta");
                mainDescription.addClassName("auth-description");
                mainDescription.getStyle()
                                .set("text-align", "center")
                                .set("color", "#7f8c8d")
                                .set("margin-bottom", "20px");

                // Contenedor para el título y descripción
                dynamicHeader = new VerticalLayout(mainTitle, mainDescription);
                dynamicHeader.setPadding(false);
                dynamicHeader.setSpacing(false);
                dynamicHeader.setAlignItems(Alignment.CENTER);
        }

        /**
         * Actualiza el header según la vista activa
         */
        private void updateHeader(String title, String description) {
                mainTitle.setText(title);
                mainDescription.setText(description);
        }

        // Método para crear las pestañas de inicio de sesión y registro
        private void createTabs() {
                loginTab = new Tab("Iniciar Sesión"); // Pestaña de inicio de sesión
                registerTab = new Tab("Registrarse"); // Pestaña de registro

                tabs = new Tabs(loginTab, registerTab); // Agrupa las pestañas
                tabs.setWidthFull(); // Ancho completo
                tabs.getStyle().set("margin-bottom", "20px")
                                .set("text-align", "center");
                tabs.addThemeVariants(TabsVariant.LUMO_CENTERED); // Centra las pestañas
                tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS); // Igual ancho para las pestañas
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

                pages.add(loginForm, registerForm, resetPasswordForm); // Agrega los formularios al contenedor de
                                                                       // páginas
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
                nameField.setMaxLength(100);
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
                lastNameField.setMaxLength(100);
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
                emailField.setMaxLength(150);
                emailField.getStyle().set("margin-bottom", "15px");

                // Validaciones para email - CORREGIDAS
                binder.forField(emailField)
                                .asRequired("El email es obligatorio")
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
                passwordField.setMaxLength(255);
                passwordField.getStyle().set("margin-bottom", "15px");

                // Validaciones para contraseña
                binder.forField(passwordField)
                                .asRequired("La contraseña es obligatoria")
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
                confirmPasswordField.setMaxLength(255);
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
                registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); // Cambiado a azul
                registerButton.setWidthFull();
                registerButton.getStyle()
                                .set("height", "45px")
                                .set("font-weight", "bold");

                // Evento del botón con validaciones completas
                registerButton.addClickListener(event -> {
                        try {
                                // Validar campos individuales primero
                                if (!binder.validate().isOk()) {
                                        showErrorNotification("Por favor, corrija los errores en el formulario");
                                        return;
                                }

                                // Validar que las contraseñas coincidan
                                if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
                                        confirmPasswordField.setErrorMessage("Las contraseñas no coinciden");
                                        confirmPasswordField.setInvalid(true);
                                        showErrorNotification("Las contraseñas no coinciden");
                                        return;
                                }

                                // Validar que la confirmación no esté vacía
                                if (confirmPasswordField.getValue() == null
                                                || confirmPasswordField.getValue().trim().isEmpty()) {
                                        confirmPasswordField.setErrorMessage("Debe confirmar la contraseña");
                                        confirmPasswordField.setInvalid(true);
                                        showErrorNotification("Debe confirmar la contraseña");
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
                                                // Cambiar a la pestaña de login
                                                tabs.setSelectedTab(loginTab);
                                        } else {
                                                showErrorNotification("El email ya está registrado");
                                                emailField.focus();
                                        }
                                } else {
                                        showErrorNotification("Por favor, corrija los errores en el formulario");
                                }

                        } catch (Exception e) {
                                showErrorNotification("Error al crear la cuenta. Intente nuevamente.");
                                e.printStackTrace(); // Para debugging
                        }
                });

                // Configurar el bean inicial
                binder.setBean(new Usuario());

                formLayout.add(nameField, lastNameField, emailField, passwordField, confirmPasswordField,
                                registerButton);

                return formLayout;
        }

        /*
         * Formulario de Recuperacion de credenciales
         */
        private Component createResetPasswordForm() {
                FormLayout formLayout = new FormLayout();
                formLayout.addClassName("auth-form");

                // Crear Binder para validaciones
                Binder<Usuario> binder = new Binder<>(Usuario.class);

                EmailField emailField = new EmailField("Email");
                emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
                emailField.setWidthFull();
                emailField.setPlaceholder("tu@email.com");
                emailField.setRequired(true);
                emailField.getStyle().set("margin-bottom", "20px");

                // Validaciones para email en el formulario de recuperación - CORREGIDAS
                binder.forField(emailField)
                                .asRequired("El email es obligatorio")
                                .withValidator(new EmailValidator("El formato del email no es válido"))
                                .withValidator(value -> value.length() <= 150,
                                                "El email no puede exceder 150 caracteres")
                                .bind(Usuario::getCorreo, Usuario::setCorreo);

                Button sendLinkButton = new Button("Recupera credenciales");
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

                // Eventos con validaciones corregidas
                sendLinkButton.addClickListener(event -> {
                        // Validar usando el binder
                        if (!binder.validate().isOk()) {
                                showErrorNotification("Por favor, ingresa un email válido");
                                return;
                        }

                        String email = emailField.getValue();
                        Boolean emailValid = usuarioService.validarEmail(email);
                        if (!emailValid) {
                                showErrorNotification("El email no está registrado");
                                return;
                        }

                        Boolean emailBlock = usuarioService.validarBloqueo(email);
                        if (emailBlock) {
                                showErrorNotification(
                                                "El email se encuentra bloqueado, comunicarse con ctrlzbad@gmail.com");
                                return;
                        }

                        // Simular el envío del enlace de recuperación
                        Boolean enviado = usuarioService.enviarCorreoRecuperacion(email);

                        if (enviado) {
                                showSuccessNotification("Enlace de recuperación enviado a " + email);
                        } else {
                                showErrorNotification("Error al enviar el correo. Verifica la configuración.");
                        }
                        showAuthTabs();
                });

                // Configurar el bean inicial para el binder
                binder.setBean(new Usuario());

                formLayout.add(emailField, sendLinkButton, backButton);
                return formLayout;
        }

        private void updateContent() {
                if (loginTab.isSelected()) {
                        updateHeader("Bienvenido", "Inicia sesión con tu cuenta");
                        loginForm.setVisible(true);
                        registerForm.setVisible(false);
                } else if (registerTab.isSelected()) {
                        updateHeader("Crear Cuenta", "Completa los datos para crear tu nueva cuenta");
                        loginForm.setVisible(false);
                        registerForm.setVisible(true);
                }
        }

        private void showResetPasswordForm() {
                isResetPasswordVisible = true;
                updateHeader("Recuperar Contraseña", "Ingresa tu email para recibir un enlace de recuperación");
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

        private void showErrorNotification(String message) {
                Notification notification = new Notification(message, 3000,
                                Notification.Position.TOP_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.open();
        }
}