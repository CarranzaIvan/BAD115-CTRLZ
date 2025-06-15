package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.RespuestaEncuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaEncuestaRepository extends JpaRepository<RespuestaEncuesta, Integer> {
}
