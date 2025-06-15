package com.bad.ctrlz.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RespuestaEncuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRespuestaEncuesta;

    @ManyToOne
    @JoinColumn(name = "id_encuesta", nullable = false)
    private Encuesta encuesta;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    // Eliminamos el campo usuario

    public RespuestaEncuesta() {
        this.fechaEnvio = LocalDateTime.now();
    }

    public Integer getIdRespuestaEncuesta() {
        return idRespuestaEncuesta;
    }

    public void setIdRespuestaEncuesta(Integer idRespuestaEncuesta) {
        this.idRespuestaEncuesta = idRespuestaEncuesta;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
