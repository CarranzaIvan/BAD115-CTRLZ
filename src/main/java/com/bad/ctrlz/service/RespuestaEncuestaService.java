package com.bad.ctrlz.service;

import com.bad.ctrlz.model.RespuestaEncuesta;
import com.bad.ctrlz.repository.RespuestaEncuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RespuestaEncuestaService {

    private final RespuestaEncuestaRepository respuestaEncuestaRepository;

    @Autowired
    public RespuestaEncuestaService(RespuestaEncuestaRepository respuestaEncuestaRepository) {
        this.respuestaEncuestaRepository = respuestaEncuestaRepository;
    }

    @Transactional
    public void guardar(RespuestaEncuesta respuestaEncuesta) {
        respuestaEncuestaRepository.save(respuestaEncuesta);
    }
}
