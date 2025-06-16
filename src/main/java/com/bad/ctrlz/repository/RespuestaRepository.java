package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Respuesta;
import com.bad.ctrlz.model.Pregunta;
import com.bad.ctrlz.dto.RespuestaIndividualDTO;
import com.bad.ctrlz.dto.GraficoPreguntaDTO;
import com.bad.ctrlz.model.RespuestaEncuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {

    List<Respuesta> findByPregunta(Pregunta pregunta);

    List<Respuesta> findByRespuestaEncuesta(RespuestaEncuesta respuestaEncuesta);

    @Query("""
                SELECT DISTINCT r
                FROM Respuesta r
                LEFT JOIN FETCH r.pregunta p
                LEFT JOIN FETCH p.tipoPregunta tp
                LEFT JOIN FETCH r.opcion o
                LEFT JOIN FETCH r.respuestaEncuesta re
                LEFT JOIN FETCH re.encuesta e
                LEFT JOIN FETCH re.encuestado en
            """)
    List<Respuesta> findAllWithPreguntaAndOpcion();

    @Query("""
                SELECT new com.bad.ctrlz.dto.RespuestaIndividualDTO(
                    e.nombres,
                    COALESCE(
                        FUNCTION('DBMS_LOB.SUBSTR', r.textoAbierto, 4000, 1),
                        TO_CHAR(r.valorEscala),
                        o.textoOpcion
                    )
                )
                FROM Respuesta r
                LEFT JOIN r.opcion o
                JOIN r.respuestaEncuesta re
                JOIN re.encuestado e
                WHERE r.pregunta.idPregunta = :idPregunta
            """)
    List<RespuestaIndividualDTO> obtenerRespuestasPorPregunta(Integer idPregunta);

    @Query("""
                SELECT new com.bad.ctrlz.dto.GraficoPreguntaDTO(
                    COALESCE(
                        TO_CHAR(r.valorEscala),
                        o.textoOpcion
                    ),
                    COUNT(r)
                )
                FROM Respuesta r
                LEFT JOIN r.opcion o
                WHERE r.pregunta.idPregunta = :idPregunta
                GROUP BY COALESCE(
                    TO_CHAR(r.valorEscala),
                    o.textoOpcion
                )
            """)
    List<GraficoPreguntaDTO> obtenerConteoPorRespuesta(Integer idPregunta);
}
