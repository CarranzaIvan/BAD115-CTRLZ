package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Long> {

    @Query("""
        SELECT e, COUNT(re)
        FROM Encuesta e
        LEFT JOIN e.preguntas p
        LEFT JOIN p.respuestas r
        LEFT JOIN r.respuestaEncuesta re
        GROUP BY e
    """)
    List<Object[]> obtenerDashboardRespuestas();

    Optional<Encuesta> findByLinkPublico(String linkPublico);
}
