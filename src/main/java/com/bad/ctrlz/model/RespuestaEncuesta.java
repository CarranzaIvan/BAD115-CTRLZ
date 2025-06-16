package com.bad.ctrlz.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "respuesta_encuesta")
public class RespuestaEncuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta_encuesta")
    private Integer idRespuestaEncuesta;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encuesta", nullable = false)
    private Encuesta encuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encuestado", nullable = false)
    private Encuestado encuestado;

    @OneToMany(mappedBy = "respuestaEncuesta", fetch = FetchType.LAZY)
    private Set<Respuesta> respuestas = new HashSet<>();

    public RespuestaEncuesta() { }

    public RespuestaEncuesta(Integer idRespuestaEncuesta, LocalDateTime fechaEnvio, Encuesta encuesta, Encuestado encuestado) {
        this.idRespuestaEncuesta = idRespuestaEncuesta;
        this.fechaEnvio = fechaEnvio;
        this.encuesta = encuesta;
        this.encuestado = encuestado;
    }

    public Integer getIdRespuestaEncuesta() {
        return idRespuestaEncuesta;
    }

    public void setIdRespuestaEncuesta(Integer idRespuestaEncuesta) {
        this.idRespuestaEncuesta = idRespuestaEncuesta;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public Encuestado getEncuestado() {
        return encuestado;
    }

    public void setEncuestado(Encuestado encuestado) {
        this.encuestado = encuestado;
    }

    public Set<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Set<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
