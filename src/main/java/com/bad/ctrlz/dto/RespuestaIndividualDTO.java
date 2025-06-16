package com.bad.ctrlz.dto;

public class RespuestaIndividualDTO {
    private String nombreUsuario;
    private String respuesta;

    public RespuestaIndividualDTO(String nombre, Object respuesta) {
        this.nombreUsuario = nombre;
        this.respuesta = respuesta != null ? respuesta.toString() : null;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRespuesta() {
        return respuesta;
    }
}
