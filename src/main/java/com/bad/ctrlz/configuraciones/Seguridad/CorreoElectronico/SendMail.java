package com.bad.ctrlz.configuraciones.Seguridad.CorreoElectronico;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SendMail {
    
    @Autowired
    private JavaMailSender mailSender;
    
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
            String htmlContent = generateEmailTemplate(subject, nombre, nuevaContrasena, 
                                                     descripcion, consideracion);

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

    /**
     * Genera la plantilla HTML completa del correo
     */
    private String generateEmailTemplate(String titulo, String nombre, String contrasena, 
                                       String descripcion, String consideracion) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>")
            .append("<html lang='es'>")
            .append("<head>")
            .append("<meta charset='UTF-8'>")
            .append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>")
            .append("<title>").append(titulo).append("</title>")
            .append("<style>").append(getEmailStyles()).append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<div class='email-container'>")
            .append("<div class='header'>")
            .append("<h1>").append(titulo).append("</h1>")
            .append("</div>")
            .append("<div class='content'>")
            .append("<p class='greeting'>Hola <strong>").append(nombre).append("</strong>,</p>")
            .append("<p class='description'>").append(descripcion).append("</p>")
            .append("<div class='credentials-section'>")
            .append("<h3>Tus nuevas credenciales de acceso:</h3>")
            .append("<div class='credential-item'>")
            .append("<span class='label'>Usuario:</span>")
            .append("<span class='value'>").append(nombre).append("</span>")
            .append("</div>")
            .append("<div class='credential-item'>")
            .append("<span class='label'>Nueva contraseña:</span>")
            .append("<span class='password'>").append(contrasena).append("</span>")
            .append("</div>")
            .append("</div>");
            
        // Agregar consideraciones si existen
        if (consideracion != null && !consideracion.trim().isEmpty()) {
            html.append("<div class='consideration-section'>")
                .append("<h4>Consideración importante:</h4>")
                .append("<p>").append(consideracion).append("</p>")
                .append("</div>");
        }
        
        html.append("<div class='security-notice'>")
            .append("<h4>Instrucciones de seguridad:</h4>")
            .append("<ul>")
            .append("<li>Cambia tu contraseña después del primer inicio de sesión</li>")
            .append("<li>No compartas estas credenciales con nadie</li>")
            .append("<li>Si tienes problemas para acceder, contacta al administrador</li>")
            .append("</ul>")
            .append("</div>")
            .append("<div class='footer'>")
            .append("<p>Este es un correo automático, por favor no responder.</p>")
            .append("<p>Si tienes alguna pregunta, contacta al equipo de soporte: ")
            .append("<a href='mailto:").append(emailConfig.getSupportEmail()).append("'>")
            .append(emailConfig.getSupportEmail()).append("</a></p>")
            .append("<p>&copy; 2025 ").append(emailConfig.getCompanyName()).append(". Todos los derechos reservados.</p>")
            .append("</div>")
            .append("</div>")
            .append("</body>")
            .append("</html>");
            
        return html.toString();
    }

    /**
     * Estilos CSS para el correo electrónico
     */
    private String getEmailStyles() {
        return """
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            
            body {
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                line-height: 1.6;
                color: #333333;
                background-color: #f4f6f8;
                padding: 20px;
            }
            
            .email-container {
                max-width: 600px;
                margin: 0 auto;
                background-color: #ffffff;
                border-radius: 12px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                overflow: hidden;
            }
            
            .header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 40px 30px;
                text-align: center;
            }
            
            .header h1 {
                font-size: 28px;
                font-weight: 600;
                margin: 0;
                text-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            
            .content {
                padding: 40px 30px;
            }
            
            .greeting {
                font-size: 18px;
                margin-bottom: 25px;
                color: #2c3e50;
            }
            
            .description {
                font-size: 16px;
                margin-bottom: 30px;
                color: #555555;
                line-height: 1.7;
            }
            
            .credentials-section {
                background: linear-gradient(145deg, #f8f9fa, #e9ecef);
                border: 2px solid #dee2e6;
                border-radius: 10px;
                padding: 25px;
                margin: 25px 0;
                box-shadow: inset 0 1px 3px rgba(0,0,0,0.1);
            }
            
            .credentials-section h3 {
                color: #495057;
                margin-bottom: 20px;
                font-size: 18px;
                font-weight: 600;
            }
            
            .credential-item {
                display: flex;
                align-items: center;
                margin-bottom: 15px;
                padding: 12px;
                background-color: #ffffff;
                border-radius: 6px;
                border: 1px solid #e9ecef;
            }
            
            .label {
                font-weight: 600;
                color: #495057;
                min-width: 140px;
                margin-right: 15px;
            }
            
            .value {
                color: #2c3e50;
                font-weight: 500;
            }
            
            .password {
                background: linear-gradient(145deg, #fff3cd, #ffeaa7);
                color: #856404;
                padding: 8px 16px;
                border-radius: 6px;
                border: 1px solid #ffeaa7;
                font-family: 'Courier New', monospace;
                font-weight: bold;
                font-size: 14px;
                letter-spacing: 1px;
                box-shadow: inset 0 1px 2px rgba(0,0,0,0.1);
            }
            
            .consideration-section {
                background-color: #d1ecf1;
                border: 1px solid #bee5eb;
                border-left: 4px solid #17a2b8;
                border-radius: 6px;
                padding: 20px;
                margin: 25px 0;
            }
            
            .consideration-section h4 {
                color: #0c5460;
                margin-bottom: 10px;
                font-size: 16px;
                font-weight: 600;
            }
            
            .consideration-section p {
                color: #0c5460;
                margin: 0;
                line-height: 1.6;
            }
            
            .security-notice {
                background-color: #f8d7da;
                border: 1px solid #f5c6cb;
                border-left: 4px solid #dc3545;
                border-radius: 6px;
                padding: 20px;
                margin: 25px 0;
            }
            
            .security-notice h4 {
                color: #721c24;
                margin-bottom: 15px;
                font-size: 16px;
                font-weight: 600;
            }
            
            .security-notice ul {
                margin: 0;
                padding-left: 20px;
                color: #721c24;
            }
            
            .security-notice li {
                margin-bottom: 8px;
                line-height: 1.5;
            }
            
            .footer {
                background-color: #f8f9fa;
                border-top: 1px solid #dee2e6;
                padding: 25px 30px;
                text-align: center;
                color: #6c757d;
                font-size: 14px;
            }
            
            .footer p {
                margin: 8px 0;
                line-height: 1.5;
            }
            
            /* Responsive design */
            @media (max-width: 600px) {
                body {
                    padding: 10px;
                }
                
                .email-container {
                    border-radius: 8px;
                }
                
                .header {
                    padding: 30px 20px;
                }
                
                .header h1 {
                    font-size: 24px;
                }
                
                .content {
                    padding: 30px 20px;
                }
                
                .credentials-section {
                    padding: 20px;
                }
                
                .credential-item {
                    flex-direction: column;
                    align-items: flex-start;
                }
                
                .label {
                    min-width: auto;
                    margin-right: 0;
                    margin-bottom: 5px;
                }
                
                .footer {
                    padding: 20px;
                }
            }
            """;
    }
}