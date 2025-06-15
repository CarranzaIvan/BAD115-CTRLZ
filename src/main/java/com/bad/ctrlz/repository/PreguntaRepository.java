package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {

    // Lista todas las preguntas asociadas a una encuesta específica
    List<Pregunta> findByEncuesta_IdEncuesta(Integer idEncuesta);

    @Query("SELECT p FROM Pregunta p JOIN FETCH p.tipoPregunta WHERE p.encuesta.idEncuesta = :idEncuesta")
    List<Pregunta> findByEncuestaConTipoPregunta(@Param("idEncuesta") Integer idEncuesta);

    List<Pregunta> findByEncuestaIdEncuesta(Integer idEncuesta);
}