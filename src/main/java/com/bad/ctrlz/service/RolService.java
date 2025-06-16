package com.bad.ctrlz.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bad.ctrlz.model.Rol;
import com.bad.ctrlz.repository.RolRepository;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    /**
     * Inserta un rol en la base de datos si no existe.
     *
     * @param nombreRol El nombre del rol a insertar.
     */
    public boolean existeRol(String nombreRol) {
        return rolRepository.existsByNombreRol(nombreRol);
    }

    /**
     * Inserta un rol en la base de datos si no existe.
     *
     * @param nombreRol El nombre del rol a insertar.
     */
    public void insertarRol(String nombreRol) {
        Rol nuevoRol = new Rol();
        nuevoRol.setNombreRol(nombreRol);
        rolRepository.save(nuevoRol);
    }

    /*
     * Obtener un rol por nombre
     */
    public Set<Rol> obtenerRolAdministrador() {
        Set<Rol> rol = rolRepository.encontrarAdministrador();
        return rol;
    }

    /*
     * Obtener un rol por nombre
     */
    public Rol obtenerRolUsuario() {
        Rol rol = rolRepository.encontrarUsuario();
        return rol;
    }
}
