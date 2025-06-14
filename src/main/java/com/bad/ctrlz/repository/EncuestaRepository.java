package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Integer> {
    Optional<Encuesta> findByLinkPublico(String linkPublico);
}
