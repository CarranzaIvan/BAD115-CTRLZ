// Encuesta.java
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
@Table(name = "encuesta")
public class Encuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_encuesta")
    private Integer idEncuesta;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "objetivo", length = 1000)
    private String objetivo;

    @Column(name = "grupo_meta", length = 255)
    private String grupoMeta;

    @Column(name = "estado", nullable = false, length = 1)
    private String estado = "A";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "encuesta", fetch = FetchType.LAZY)
    private Set<Pregunta> preguntas = new HashSet<>();

    public Encuesta() { }

    public Encuesta(Integer idEncuesta, String titulo, String objetivo, String grupoMeta, String estado, Usuario usuario) {
        this.idEncuesta = idEncuesta;
        this.titulo     = titulo;
        this.objetivo   = objetivo;
        this.grupoMeta  = grupoMeta;
        this.estado     = estado;
        this.usuario    = usuario;
    }

    public Integer getIdEncuesta() {
        return idEncuesta;
    }

    public void setIdEncuesta(Integer idEncuesta) {
        this.idEncuesta = idEncuesta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getGrupoMeta() {
        return grupoMeta;
    }

    public void setGrupoMeta(String grupoMeta) {
        this.grupoMeta = grupoMeta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Set<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(Set<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
