package com.bad.ctrlz.model;
import java.util.HashSet;

import org.hibernate.annotations.ManyToAny;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;

@Entity
@Table(name = "Rol")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idRol; // Identificador de Rol

    @Column(unique = true, nullable = false) // Debe ser único y nunca nulo
    String nombreRol; // Nombre de Rol tipo Enum

    @ManyToAny
    @JoinTable(name="rol_permiso", joinColumns = @JoinColumn(name = "id_rol"), inverseJoinColumns = @JoinColumn(name = "id_permiso"))
    Set<Permiso> permisos = new HashSet<>(); // Relación de rol → permiso

    public Rol(){}

    public Rol (String nombreRol){
        this.nombreRol =  nombreRol.toUpperCase();
    }
    
    public Rol(Long idRol, String nombreRol, Set<Permiso> permisos) {
        this.idRol = idRol;
        this.nombreRol = nombreRol.toUpperCase();
        this.permisos = permisos;
    }

    public Set<Permiso> getPermisos() {
        return permisos;
    }

    public void setUsuarioPermiso(Set<Permiso> permisos) {
        this.permisos = permisos;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol.toUpperCase();
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol.toUpperCase();
    }



}