package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RolRepository extends JpaRepository <Rol, Integer>{
    
    // Obtener rol de administrador
    @Query("SELECT r FROM Rol r WHERE r.nombreRol = 'ADMINISTRADOR'")
    Set<Rol> encontrarAdministrador();

    // Obtener rol de usuario
    @Query("SELECT r FROM Rol r WHERE r.nombreRol = 'USUARIO'")
    Rol encontrarUsuario();

    @Query("SELECT r FROM Rol r WHERE r.idRol = :idRol")
    Set<Rol> encontrarRol(@Param("idRol") Integer idRol);
    

}