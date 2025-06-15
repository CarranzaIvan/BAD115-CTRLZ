package com.bad.ctrlz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bad.ctrlz.dto.GraficoPreguntaDTO;
import com.bad.ctrlz.dto.RespuestaIndividualDTO;
import com.bad.ctrlz.model.Pregunta;
import com.bad.ctrlz.model.Respuesta;
import com.bad.ctrlz.model.RespuestaEncuesta;

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

    @Query("""
        SELECT new com.bad.ctrlz.dto.RespuestaIndividualDTO(
            u.nombre,
            COALESCE(
                r.textoAbierto,
                TO_CHAR(r.valorEscala),
                o.textoOpcion
            )
        )
        FROM Respuesta r
        LEFT JOIN r.opcion o
        JOIN r.respuestaEncuesta re
        JOIN re.usuario u
        WHERE r.pregunta.idPregunta = :idPregunta
        """)
    List<RespuestaIndividualDTO> obtenerRespuestasPorPregunta(Integer idPregunta);

    @Query("""
    SELECT new com.bad.ctrlz.dto.GraficoPreguntaDTO(
        COALESCE(CAST(r.valorEscala AS string), o.textoOpcion),
        COUNT(r)
        )
        FROM Respuesta r
        LEFT JOIN r.opcion o
        WHERE r.pregunta.idPregunta = :idPregunta
        GROUP BY COALESCE(CAST(r.valorEscala AS string), o.textoOpcion)
        """)
    List<GraficoPreguntaDTO> obtenerConteoPorRespuesta(Integer idPregunta);

}
