package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Respuesta;
import com.bad.ctrlz.model.Pregunta;
import com.bad.ctrlz.model.RespuestaEncuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    List<Respuesta> findByPregunta(Pregunta pregunta);
    List<Respuesta> findByRespuestaEncuesta(RespuestaEncuesta respuestaEncuesta);

    // Agregamos el JOIN FETCH para evitar LazyInitializationException
    @Query("""
    SELECT DISTINCT r 
    FROM Respuesta r
    LEFT JOIN FETCH r.pregunta p
    LEFT JOIN FETCH p.tipoPregunta tp
    LEFT JOIN FETCH r.opcion o
    LEFT JOIN FETCH r.respuestaEncuesta re
    LEFT JOIN FETCH re.encuesta e
    LEFT JOIN FETCH re.usuario u
    """)
    List<Respuesta> findAllWithPreguntaAndOpcion();


}
