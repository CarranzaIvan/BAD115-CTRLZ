package com.bad.ctrlz.views.Seguridad;

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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Autenticación")
@CssImport("./styles/auth-view.css")
public class LoginView extends VerticalLayout {

    private Tabs tabs;
    private Div pages;
    private Tab loginTab;
    private Tab registerTab;
    private Component loginForm;
    private Component registerForm;
    private Component resetPasswordForm;
    private boolean isResetPasswordVisible = false;

    public LoginView() {
        addClassName("auth-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Div card = createCard();
        add(card);
    }

    private Div createCard() {
        Div card = new Div();
        card.addClassName("auth-card");
        card.setWidth("400px");
        card.getStyle()
            .set("padding", "30px")
            .set("border-radius", "10px")
            .set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)")
            .set("background-color", "white");

        H2 title = new H2("Bienvenido");
        title.addClassName("auth-title");
        title.getStyle()
            .set("text-align", "center")
            .set("color", "#2c3e50")
            .set("margin-bottom", "10px");

        Paragraph description = new Paragraph("Inicia sesión o crea una nueva cuenta");
        description.addClassName("auth-description");
        description.getStyle()
            .set("text-align", "center")
            .set("color", "#7f8c8d")
            .set("margin-bottom", "20px");

        VerticalLayout header = new VerticalLayout(title, description);
        header.setPadding(false);
        header.setSpacing(false);
        header.setAlignItems(Alignment.CENTER);

        // Crear pestañas y contenido
        createTabs();
        createForms();

        VerticalLayout content = new VerticalLayout(header, tabs, pages);
        content.setPadding(true);
        content.setSpacing(true);
        content.setAlignItems(Alignment.STRETCH);

        card.add(content);
        return card;
    }

    private void createTabs() {
        loginTab = new Tab("Iniciar Sesión");
        registerTab = new Tab("Registrarse");
        
        tabs = new Tabs(loginTab, registerTab);
        tabs.setWidthFull();
        tabs.getStyle().set("margin-bottom", "20px");
        tabs.addSelectedChangeListener(event -> {
            if (isResetPasswordVisible) {
                showAuthTabs();
            } else {
                updateContent();
            }
        });
        
        pages = new Div();
        pages.setSizeFull();
    }

    private void createForms() {
        loginForm = createLoginForm();
        registerForm = createRegisterForm();
        resetPasswordForm = createResetPasswordForm();
        
        pages.add(loginForm, registerForm, resetPasswordForm);
        loginForm.setVisible(true);
        registerForm.setVisible(false);
        resetPasswordForm.setVisible(false);
    }

    private Component createLoginForm() {
        FormLayout formLayout = new FormLayout();
        formLayout.addClassName("auth-form");
        
        EmailField emailField = new EmailField("Email");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setWidthFull();
        emailField.setPlaceholder("tu@email.com");
        emailField.setRequired(true);
        emailField.getStyle().set("margin-bottom", "15px");
        
        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField.setWidthFull();
        passwordField.setPlaceholder("••••••••");
        passwordField.setRequired(true);
        passwordField.getStyle().set("margin-bottom", "20px");
        
        Button loginButton = new Button("Iniciar Sesión");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        loginButton.setWidthFull();
        loginButton.getStyle()
            .set("height", "45px")
            .set("font-weight", "bold");
        
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
        
        formLayout.add(emailField, passwordField, loginButton, forgotPasswordButton);
        return formLayout;
    }

    private Component createRegisterForm() {
        FormLayout formLayout = new FormLayout();
        formLayout.addClassName("auth-form");
        
        TextField nameField = new TextField("Nombre completo");
        nameField.setPrefixComponent(VaadinIcon.USER.create());
        nameField.setWidthFull();
        nameField.setPlaceholder("Tu nombre completo");
        nameField.setRequired(true);
        nameField.getStyle().set("margin-bottom", "15px");
        
        EmailField emailField = new EmailField("Email");
        emailField.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        emailField.setWidthFull();
        emailField.setPlaceholder("tu@email.com");
        emailField.setRequired(true);
        emailField.getStyle().set("margin-bottom", "15px");
        
        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setPrefixComponent(VaadinIcon.LOCK.create());
        passwordField.setWidthFull();
        passwordField.setPlaceholder("••••••••");
        passwordField.setRequired(true);
        passwordField.getStyle().set("margin-bottom", "15px");
        
        PasswordField confirmPasswordField = new PasswordField("Confirmar contraseña");
        confirmPasswordField.setPrefixComponent(VaadinIcon.LOCK.create());
        confirmPasswordField.setWidthFull();
        confirmPasswordField.setPlaceholder("••••••••");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.getStyle().set("margin-bottom", "20px");
        
        Button registerButton = new Button("Crear Cuenta");
        registerButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        registerButton.setWidthFull();
        registerButton.getStyle()
            .set("height", "45px")
            .set("font-weight", "bold");
        
        // Eventos de prueba
        registerButton.addClickListener(event -> {
            System.out.println("REGISTER - Name: " + nameField.getValue());
            System.out.println("REGISTER - Email: " + emailField.getValue());
            System.out.println("REGISTER - Password: " + passwordField.getValue());
            System.out.println("REGISTER - Confirm: " + confirmPasswordField.getValue());
            showSuccessNotification("¡Cuenta creada exitosamente!");
        });
        
        formLayout.add(nameField, emailField, passwordField, confirmPasswordField, registerButton);
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
    //     Notification notification = new Notification(message, 3000, Notification.Position.TOP_CENTER);
    //     notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    //     notification.open();
    // }
}