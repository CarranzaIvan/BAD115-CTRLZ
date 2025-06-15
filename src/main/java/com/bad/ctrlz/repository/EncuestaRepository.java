package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface EncuestaRepository extends JpaRepository<Encuesta, Long> {

    @Query("""
        SELECT e, COUNT(DISTINCT re.usuario.id)
        FROM Encuesta e
        LEFT JOIN e.preguntas p
        LEFT JOIN p.respuestas r
        LEFT JOIN r.respuestaEncuesta re
        GROUP BY e
        """)
    List<Object[]> obtenerDashboardRespuestas();


}
