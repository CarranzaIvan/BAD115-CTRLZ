package com.bad.ctrlz.dto;

public class DashboardEncuestaDTO {
    private Long idEncuesta;
    private String titulo;
    private Long totalRespuestas;

    public DashboardEncuestaDTO(Long idEncuesta, String titulo, Long totalRespuestas) {
        this.idEncuesta = idEncuesta;
        this.titulo = titulo;
        this.totalRespuestas = totalRespuestas;
    }

    public Long getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(Long idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getTotalRespuestas() {
        return totalRespuestas;
    }

    public void setTotalRespuestas(Long totalRespuestas) {
        this.totalRespuestas = totalRespuestas;
    }
}