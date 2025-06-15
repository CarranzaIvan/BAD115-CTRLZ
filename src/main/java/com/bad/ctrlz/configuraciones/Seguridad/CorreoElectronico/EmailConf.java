package com.bad.ctrlz.configuraciones.Seguridad.CorreoElectronico;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.mail")
public class EmailConf {
    
    private String fromEmail = "ctrlzbad@gmail.com";
    private String fromName = "Sistema CTRLZ - Credenciales";
    private String supportEmail = "ctrlzbad@gmail.com";
    private String companyName = "CTRLZ";
    
    // Getters y setters
    public String getFromEmail() {
        return fromEmail;
    }
    
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }
    
    public String getFromName() {
        return fromName;
    }
    
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
    
    public String getSupportEmail() {
        return supportEmail;
    }
    
    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}