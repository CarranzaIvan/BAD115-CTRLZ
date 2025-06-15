package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Respuesta;
import com.bad.ctrlz.repository.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RespuestaService {

    private final RespuestaRepository respuestaRepository;

    @Autowired
    public RespuestaService(RespuestaRepository respuestaRepository) {
        this.respuestaRepository = respuestaRepository;
    }

    @Transactional
    public void guardar(Respuesta respuesta) {
        respuestaRepository.save(respuesta);
    }
}
