package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.TipoPregunta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPreguntaRepository extends JpaRepository<TipoPregunta, Integer> {
    boolean existsByNombreTipo(String nombreTipo);
}