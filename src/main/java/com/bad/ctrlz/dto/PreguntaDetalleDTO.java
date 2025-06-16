package com.bad.ctrlz.dto;

public class PreguntaDetalleDTO {
    private Integer idPregunta;
    private String textoPregunta;
    private String tipoPregunta;

    public PreguntaDetalleDTO(Integer idPregunta, String textoPregunta, String tipoPregunta) {
        this.idPregunta = idPregunta;
        this.textoPregunta = textoPregunta;
        this.tipoPregunta = tipoPregunta;
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }
}