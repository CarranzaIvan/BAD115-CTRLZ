package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Set;

public interface RolRepository extends JpaRepository <Rol, Integer>{
    
    // Obtener rol de administrador
    @Query("SELECT r FROM Rol r WHERE r.nombreRol = 'ADMINISTRADOR'")
    Set<Rol> encontrarAdministrador();

    // Obtener rol de usuario
    @Query("SELECT r FROM Rol r WHERE r.nombreRol = 'USUARIO'")
    Rol encontrarUsuario();

    // Verificar si existe un rol por nombre
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rol r WHERE r.nombreRol = :nombreRol")
    boolean existsByNombreRol(String nombreRol);

}