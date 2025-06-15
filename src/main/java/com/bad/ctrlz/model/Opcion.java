// Opcion.java
package com.bad.ctrlz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "opcion")
public class Opcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_opcion")
    private Integer idOpcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    private Pregunta pregunta;

    @Column(name = "texto_opcion", length = 1000)
    private String textoOpcion;

    @Column(name = "valor_escala")
    private Integer valorEscala;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "es_otro")
    private Boolean esOtro = false;

    public Opcion() {
    }

    public Opcion(Integer idOpcion, Pregunta pregunta, String textoOpcion, Integer valorEscala, Integer orden) {
        this.idOpcion = idOpcion;
        this.pregunta = pregunta;
        this.textoOpcion = textoOpcion;
        this.valorEscala = valorEscala;
        this.orden = orden;
    }

    public Integer getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(Integer idOpcion) {
        this.idOpcion = idOpcion;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public String getTextoOpcion() {
        return textoOpcion;
    }

    public void setTextoOpcion(String textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

    public Integer getValorEscala() {
        return valorEscala;
    }

    public void setValorEscala(Integer valorEscala) {
        this.valorEscala = valorEscala;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getEsOtro() {
        return esOtro != null ? esOtro : false;
    }

    public void setesOtro(boolean esOtro) {
        this.esOtro = esOtro;
    }
}
