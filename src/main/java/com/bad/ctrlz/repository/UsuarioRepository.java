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

    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Usuario encontrarPorCorreo(@Param("correo") String correo);

    // Buscar existe usuario por correo
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u WHERE u.correo = :correo")
    boolean existsByEmail(@Param("correo") String correo);

    // Verifica si existe un usuario por correo y que no esté bloqueado
    @Query("""
                SELECT COUNT(u) > 0
                FROM Usuario u
                WHERE u.correo = :correo
                  AND u.accountLocked = false
            """)
    boolean existsUnlockedUserByCorreo(@Param("correo") String correo);

    // Cuenta usuarios activos (habilitados y no bloqueados)
    @Query("""
                SELECT COUNT(u)
                FROM Usuario u
                WHERE u.enabled = true
                  AND u.accountLocked = false
            """)
    int contarUsuariosActivos();

    // Cuenta usuarios bloqueados y deshabilitados
    @Query("""
                SELECT COUNT(u)
                FROM Usuario u
                WHERE u.accountLocked = true
                  AND u.enabled = false
            """)
    int contarUsuariosBloqueados();

    // Cuenta usuarios desactivados (no bloqueados pero deshabilitados)
    @Query("""
                SELECT COUNT(u)
                FROM Usuario u
                WHERE u.accountLocked = false
                  AND u.enabled = false
            """)
    int contarUsuariosDesactivados();

}
