package com.bad.ctrlz.views;

import com.bad.ctrlz.views.components.Navbar;

import org.springframework.beans.factory.annotation.Autowired;

import com.bad.ctrlz.configuraciones.Seguridad.CorreoElectronico.SendMail;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Inicio")
@Route(value = "", layout = Navbar.class)
public class MainView extends VerticalLayout {

    @Autowired
    private SendMail emailService;

    public MainView() {
        // Título simple
        add("¡Bienvenido a la aplicación CtrlZ!");

        // Botón que envía correo
        Button btn = new Button("Enviar correo a CS21024");
        btn.addClickListener(e -> enviarCorreoCS21024());

        add(btn);
    }

    private void enviarCorreoCS21024() {
        // Aquí defines tus variables (puedes obtenerlas de formularios, base de datos,
        // etc.)
        String subject = "Solicitud desde CtrlZ";
        String descripcion = "Descripción de la solicitud...";
        String usuarioCorreo = "cs21024@ues.edu.sv";
        String nuevaContrasena = "nuevaPass123";
        String nombre = "Juan Pérez";
        String consideracion = "Solicitud urgente";

        boolean enviado = false;
        enviado = emailService.sendEmail(subject, descripcion, usuarioCorreo, nuevaContrasena, nombre,
                consideracion);

        if (enviado) {
            Notification.show("¡Correo enviado exitosamente a CS21024!");
        } else {
            Notification.show("Error al enviar el correo. Verifica la configuración.");
        }
    }
}
