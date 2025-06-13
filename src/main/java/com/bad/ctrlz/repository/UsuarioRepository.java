package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Usuario;

import java.util.Optional;

// import java.util.List;
// import java.util.Optional;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Consultas hacia la base de datos - Tabla Usuario

    // Buscar usuario por correo
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Optional<Usuario> findByEmail(@Param("correo") String correo);

    // Buscar existe usuario por correo
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.correo = :correo")
    boolean existsByEmail(@Param("correo") String correo);
}
