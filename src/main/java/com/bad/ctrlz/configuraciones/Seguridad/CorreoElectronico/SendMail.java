package com.bad.ctrlz.configuraciones.Seguridad.CorreoElectronico;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bad.ctrlz.views.Seguridad.EmailView;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendMail {
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailView emailView;
    
    @Autowired
    private EmailConf emailConfig;

    public Boolean sendEmail(String subject, String descripcion, String usuarioCorreo, 
                         String nuevaContrasena, String nombre, String consideracion) {
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        
        try {
            helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
                                         StandardCharsets.UTF_8.name());

            // Generar el contenido HTML de la plantilla
            String htmlContent = emailView.generateEmailTemplate(subject, nombre, nuevaContrasena, 
                                                     descripcion, consideracion, usuarioCorreo);

            // Configurar el correo
            helper.setTo(usuarioCorreo);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indica que el contenido es HTML
            helper.setFrom(emailConfig.getFromEmail());

            mailSender.send(message);
            System.out.println("Correo enviado exitosamente a: " + usuarioCorreo);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }


}