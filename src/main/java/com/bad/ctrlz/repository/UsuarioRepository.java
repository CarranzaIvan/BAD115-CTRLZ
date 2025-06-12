package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Usuario;

// import java.util.List;
// import java.util.Optional;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Consultas hacia la base de datos - Tabla Usuario
}
