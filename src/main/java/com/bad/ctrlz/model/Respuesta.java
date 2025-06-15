// Respuesta.java
package com.bad.ctrlz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "respuesta")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Integer idRespuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_respuesta_encuesta", nullable = false)
    private RespuestaEncuesta respuestaEncuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    private Pregunta pregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_opcion")
    private Opcion opcion;

    @Column(name = "texto_abierto", length = 255)
    private String textoAbierto;

    @Column(name = "valor_escala")
    private Integer valorEscala;

    @Column(name = "posicion_ranking")
    private Integer posicionRanking;

    public Respuesta() { }

    public Respuesta(Integer idRespuesta, RespuestaEncuesta respuestaEncuesta, Pregunta pregunta,
                     Opcion opcion, String textoAbierto, Integer valorEscala, Integer posicionRanking) {
        this.idRespuesta        = idRespuesta;
        this.respuestaEncuesta  = respuestaEncuesta;
        this.pregunta           = pregunta;
        this.opcion             = opcion;
        this.textoAbierto       = textoAbierto;
        this.valorEscala        = valorEscala;
        this.posicionRanking    = posicionRanking;
    }

    public Integer getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(Integer idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public RespuestaEncuesta getRespuestaEncuesta() {
        return respuestaEncuesta;
    }

    public void setRespuestaEncuesta(RespuestaEncuesta respuestaEncuesta) {
        this.respuestaEncuesta = respuestaEncuesta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public Opcion getOpcion() {
        return opcion;
    }

    public void setOpcion(Opcion opcion) {
        this.opcion = opcion;
    }

    public String getTextoAbierto() {
        return textoAbierto;
    }

    public void setTextoAbierto(String textoAbierto) {
        this.textoAbierto = textoAbierto;
    }

    public Integer getValorEscala() {
        return valorEscala;
    }

    public void setValorEscala(Integer valorEscala) {
        this.valorEscala = valorEscala;
    }

    public Integer getPosicionRanking() {
        return posicionRanking;
    }

    public void setPosicionRanking(Integer posicionRanking) {
        this.posicionRanking = posicionRanking;
    }
}
