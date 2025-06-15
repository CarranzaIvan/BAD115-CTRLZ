package com.bad.ctrlz.dto;

public class RespuestaIndividualDTO {
    private String nombreUsuario;
    private String respuesta;

    public RespuestaIndividualDTO(String nombreUsuario, String respuesta) {
        this.nombreUsuario = nombreUsuario;
        this.respuesta = respuesta;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
