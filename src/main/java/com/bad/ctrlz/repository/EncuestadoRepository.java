package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Encuestado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EncuestadoRepository extends JpaRepository<Encuestado, Integer> {
    // Por ahora no necesitamos métodos adicionales
}
