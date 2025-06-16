package com.bad.ctrlz.repository;

import com.bad.ctrlz.dto.PreguntaDetalleDTO;
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

    @Query("""
    SELECT new com.bad.ctrlz.dto.PreguntaDetalleDTO(p.idPregunta, p.textoPregunta, tp.nombreTipo)
    FROM Pregunta p
    JOIN p.tipoPregunta tp
    WHERE p.encuesta.idEncuesta = :idEncuesta
    """)
    List<PreguntaDetalleDTO> obtenerPreguntasPorEncuesta(Long idEncuesta);
    //LISTA
    List<Pregunta> findByEncuestaIdEncuesta(Integer idEncuesta);
}