package com.bad.ctrlz.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ENCUESTADO")
public class Encuestado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ENCUESTADO")
    private Integer idEncuestado;

    @Column(name = "NOMBRES", nullable = false)
    private String nombres;

    @Column(name = "APELLIDOS", nullable = false)
    private String apellidos;

    @Column(name = "CORREO", nullable = false)
    private String correo;

    @Column(name = "GENERO", nullable = false)
    private String genero;

    @Column(name = "FECHA_NACIMIENTO")
    private LocalDate fechaNacimiento;

    // --- GETTERS Y SETTERS ---

    public Integer getIdEncuestado() {
        return idEncuestado;
    }

    public void setIdEncuestado(Integer idEncuestado) {
        this.idEncuestado = idEncuestado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
