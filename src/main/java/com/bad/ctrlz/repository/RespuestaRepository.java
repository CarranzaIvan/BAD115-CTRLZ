package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    // Puedes agregar consultas personalizadas aquí si las necesitas luego
}
