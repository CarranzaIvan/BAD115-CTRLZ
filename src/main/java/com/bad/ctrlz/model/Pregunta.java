// Pregunta.java
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

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pregunta")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_encuesta", nullable = false)
    private Encuesta encuesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_pregunta", nullable = false)
    private TipoPregunta tipoPregunta;

    @Column(name = "texto_pregunta", nullable = false, length = 1000)
    private String textoPregunta;

    @Column(name = "obligatorio", nullable = false, length = 1)
    private String obligatorio = "N";

    @Column(name = "valor_inicio_escala")
    private Double valorInicioEscala;

    @Column(name = "valor_fin_escala")
    private Double valorFinEscala;

    @Column(name = "incremento_escala")
    private Double incrementoEscala;

    @OneToMany(mappedBy = "pregunta", fetch = FetchType.LAZY)
    private Set<Opcion> opciones = new HashSet<>();

    public Pregunta() { }

    public Pregunta(Integer idPregunta, Encuesta encuesta, TipoPregunta tipoPregunta,
                    String textoPregunta, String obligatorio,
                    Double valorInicioEscala, Double valorFinEscala, Double incrementoEscala) {
        this.idPregunta        = idPregunta;
        this.encuesta          = encuesta;
        this.tipoPregunta      = tipoPregunta;
        this.textoPregunta     = textoPregunta;
        this.obligatorio       = obligatorio;
        this.valorInicioEscala = valorInicioEscala;
        this.valorFinEscala    = valorFinEscala;
        this.incrementoEscala  = incrementoEscala;
    }

    public Integer getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(Integer idPregunta) {
        this.idPregunta = idPregunta;
    }

    public Encuesta getEncuesta() {
        return encuesta;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    public TipoPregunta getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(TipoPregunta tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public String getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(String obligatorio) {
        this.obligatorio = obligatorio;
    }

    public Double getValorInicioEscala() {
        return valorInicioEscala;
    }

    public void setValorInicioEscala(Double valorInicioEscala) {
        this.valorInicioEscala = valorInicioEscala;
    }

    public Double getValorFinEscala() {
        return valorFinEscala;
    }

    public void setValorFinEscala(Double valorFinEscala) {
        this.valorFinEscala = valorFinEscala;
    }

    public Double getIncrementoEscala() {
        return incrementoEscala;
    }

    public void setIncrementoEscala(Double incrementoEscala) {
        this.incrementoEscala = incrementoEscala;
    }

    public Set<Opcion> getOpciones() {
        return opciones;
    }

    public void setOpciones(Set<Opcion> opciones) {
        this.opciones = opciones;
    }
}
