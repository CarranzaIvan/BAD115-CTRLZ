package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Opcion;
import com.bad.ctrlz.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Integer> {
//LISTA LAS OPCIONES
    List<Opcion> findByPreguntaIdPreguntaOrderByOrden(Integer idPregunta);

    @Query("SELECT o FROM Opcion o WHERE UPPER(TRIM(o.textoOpcion)) = UPPER(TRIM(:textoOpcion)) AND o.pregunta = :pregunta")
    Opcion buscarPorTextoYPregunta(@Param("textoOpcion") String textoOpcion, @Param("pregunta") Pregunta pregunta);
}
