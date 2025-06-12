package com.bad.ctrlz.views.Seguridad;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route("login")
@PageTitle("Bienvenido a CtrlZ")
public class LoginView extends VerticalLayout {

    private Tabs tabs;
    private Tab loginTab;
    private Tab registerTab;

    // Componentes del Login
    private TextField loginUsername;
    private PasswordField loginPassword;
    private Button loginButton;
    private Div loginForm;

    // Componentes del Registro
    private TextField registerUsername;
    private EmailField registerEmail;
    private PasswordField registerPassword;
    private PasswordField confirmPassword;
    private Button registerButton;
    private Div registerForm;

    // Constructor para crear las pestañas de Login y Registro
    private void createTabs() {
        loginTab = new Tab("Iniciar Sesión");
        registerTab = new Tab("Registrarse");

        tabs = new Tabs(loginTab, registerTab);
        tabs.setWidthFull();
        tabs.getStyle().set("margin-bottom", "20px");

        // Listener para cambiar entre formularios
        tabs.addSelectedChangeListener(event -> {
            if (tabs.getSelectedTab() == loginTab) {
                showLoginForm();
            } else {
                showRegisterForm();
            }
        });
    }

    public LoginView() {
        // Configurar el layout principal
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Crear la plantilla
        createAuthTemplate();
    }

    private void createAuthTemplate() {
        // Contenedor principal
        Div authCard = new Div();
        authCard.setWidth("400px");
        authCard.getStyle()
                .set("padding", "30px")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)")
                .set("background-color", "white");

        // Título
        H1 title = new H1("Bienvenido");
        H2 title2 = new H2("Inicio sesión o registro");
        title2.getStyle().set("margin-top", "10px");
        title.getStyle()
                .set("text-align", "center")
                .set("color", "#2c3e50")
                .set("margin-bottom", "20px")
                .set("font-size", "2rem");

        // Crear tabs
        createTabs();

        // Crear formularios
        createLoginForm();
        createRegisterForm();

        // Crear enlace de recuperar contraseña
        Button forgotPasswordLink = new Button("¿Olvidaste tu contraseña?");
        forgotPasswordLink.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        forgotPasswordLink.getStyle()
                .set("margin-top", "15px")
                .set("color", "#3498db")
                .set("text-decoration", "underline")
                .set("font-size", "14px")
                .set("background", "none")
                .set("border", "none")
                .set("cursor", "pointer");

        forgotPasswordLink.addClickListener(e -> {
            // Aquí iría la lógica para recuperar contraseña
            System.out.println("Recuperar contraseña clickeado");
        });

        // Agregar componentes al contenedor
        authCard.add(title,title2, tabs, loginForm, registerForm, forgotPasswordLink);

        // Agregar el contenedor a la vista
        add(authCard);

        // Estilo del fondo de la página
        getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("min-height", "100vh");
    }

    private void createLoginForm() {
        loginForm = new Div();

        // Campo de usuario
        loginUsername = new TextField("Usuario");
        loginUsername.setPlaceholder("Ingresa tu usuario");
        loginUsername.setWidthFull();
        loginUsername.getStyle().set("margin-bottom", "15px");

        // Campo de contraseña
        loginPassword = new PasswordField("Contraseña");
        loginPassword.setPlaceholder("Ingresa tu contraseña");
        loginPassword.setWidthFull();
        loginPassword.getStyle().set("margin-bottom", "20px");

        // Botón de login
        loginButton = new Button("Iniciar Sesión");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidthFull();
        loginButton.getStyle()
                .set("height", "45px")
                .set("font-weight", "bold");

        // Evento del botón login
        loginButton.addClickListener(e -> {
            System.out.println("LOGIN - Usuario: " + loginUsername.getValue());
            System.out.println("LOGIN - Contraseña: " + loginPassword.getValue());
        });

        // Agregar componentes al formulario
        loginForm.add(loginUsername, loginPassword, loginButton);
    }

    private void createRegisterForm() {
        registerForm = new Div();
        registerForm.setVisible(false); // Oculto por defecto

        // Campo de usuario
        registerUsername = new TextField("Usuario");
        registerUsername.setPlaceholder("Elige un nombre de usuario");
        registerUsername.setWidthFull();
        registerUsername.getStyle().set("margin-bottom", "15px");

        // Campo de email
        registerEmail = new EmailField("Email");
        registerEmail.setPlaceholder("tu@email.com");
        registerEmail.setWidthFull();
        registerEmail.getStyle().set("margin-bottom", "15px");

        // Campo de contraseña
        registerPassword = new PasswordField("Contraseña");
        registerPassword.setPlaceholder("Ingresa una contraseña");
        registerPassword.setWidthFull();
        registerPassword.getStyle().set("margin-bottom", "15px");

        // Campo de confirmar contraseña
        confirmPassword = new PasswordField("Confirmar Contraseña");
        confirmPassword.setPlaceholder("Confirma tu contraseña");
        confirmPassword.setWidthFull();
        confirmPassword.getStyle().set("margin-bottom", "20px");

        // Botón de registro
        registerButton = new Button("Registrarse");
        registerButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        registerButton.setWidthFull();
        registerButton.getStyle()
                .set("height", "45px")
                .set("font-weight", "bold");

        // Evento del botón registro
        registerButton.addClickListener(e -> {
            System.out.println("REGISTRO - Usuario: " + registerUsername.getValue());
            System.out.println("REGISTRO - Email: " + registerEmail.getValue());
            System.out.println("REGISTRO - Contraseña: " + registerPassword.getValue());
            System.out.println("REGISTRO - Confirmar: " + confirmPassword.getValue());
        });

        // Agregar componentes al formulario
        registerForm.add(registerUsername, registerEmail, registerPassword, confirmPassword, registerButton);
    }

    private void showLoginForm() {
        loginForm.setVisible(true);
        registerForm.setVisible(false);
    }

    private void showRegisterForm() {
        loginForm.setVisible(false);
        registerForm.setVisible(true);
    }
}