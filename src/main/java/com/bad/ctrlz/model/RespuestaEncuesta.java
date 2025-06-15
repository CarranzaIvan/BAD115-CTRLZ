// RespuestaEncuesta.java
package com.bad.ctrlz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "respuestaEncuesta", fetch = FetchType.LAZY)
    private Set<Respuesta> respuestas = new HashSet<>();

    public RespuestaEncuesta() { }

    public RespuestaEncuesta(Integer idRespuestaEncuesta, LocalDateTime fechaEnvio, Encuesta encuesta, Usuario usuario) {
        this.idRespuestaEncuesta = idRespuestaEncuesta;
        this.fechaEnvio           = fechaEnvio;
        this.encuesta             = encuesta;
        this.usuario              = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(Set<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
