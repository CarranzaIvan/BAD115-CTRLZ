package com.bad.ctrlz.dto;

public class GraficoPreguntaDTO {
    private String etiqueta;
    private Long cantidad;

    public GraficoPreguntaDTO(String etiqueta, Long cantidad) {
        this.etiqueta = etiqueta;
        this.cantidad = cantidad;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public Long getCantidad() {
        return cantidad;
    }
}