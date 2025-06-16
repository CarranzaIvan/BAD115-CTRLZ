package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {

    

    @Query("SELECT p FROM Pregunta p JOIN FETCH p.tipoPregunta WHERE p.encuesta.idEncuesta = :idEncuesta")
    List<Pregunta> findByEncuestaConTipoPregunta(@Param("idEncuesta") Integer idEncuesta);
    
    // Lista todas las preguntas asociadas a una encuesta específica
    List<Pregunta> findByEncuestaIdEncuesta(Integer idEncuesta);

    @Query("SELECT p FROM Pregunta p LEFT JOIN FETCH p.opciones WHERE p.idPregunta = :id")
    Optional<Pregunta> buscarPreguntaConOpciones(@Param("id") Integer idPregunta);

    @Query("""
    SELECT p FROM Pregunta p
    JOIN FETCH p.tipoPregunta
    LEFT JOIN FETCH p.opciones
    WHERE p.idPregunta = :id
    """)
    Optional<Pregunta> buscarConTipoYOpcionesPorId(@Param("id") Integer id);


}