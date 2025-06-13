// TipoPregunta.java
package com.bad.ctrlz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tipo_pregunta", uniqueConstraints = @UniqueConstraint(columnNames = "nombre_tipo"))
public class TipoPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_pregunta")
    private Integer idTipoPregunta;

    @Column(name = "nombre_tipo", nullable = false, length = 255)
    private String nombreTipo;

    public TipoPregunta() { }

    public TipoPregunta(Integer idTipoPregunta, String nombreTipo) {
        this.idTipoPregunta = idTipoPregunta;
        this.nombreTipo     = nombreTipo;
    }

    public Integer getIdTipoPregunta() {
        return idTipoPregunta;
    }

    public void setIdTipoPregunta(Integer idTipoPregunta) {
        this.idTipoPregunta = idTipoPregunta;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public void setNombreTipo(String nombreTipo) {
        this.nombreTipo = nombreTipo;
    }
}
