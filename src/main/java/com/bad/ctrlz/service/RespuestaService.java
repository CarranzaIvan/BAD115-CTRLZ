package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Pregunta;
import com.bad.ctrlz.model.Respuesta;
import com.bad.ctrlz.model.RespuestaEncuesta;
import com.bad.ctrlz.repository.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    public List<Respuesta> obtenerRespuestasPorPregunta(Pregunta pregunta) {
        return respuestaRepository.findByPregunta(pregunta);
    }

    public List<Respuesta> obtenerRespuestasPorEncuesta(RespuestaEncuesta respuestaEncuesta) {
        return respuestaRepository.findByRespuestaEncuesta(respuestaEncuesta);
    }

    public List<Respuesta> obtenerTodasRespuestas() {
        return respuestaRepository.findAllWithPreguntaAndOpcion();
    }

}
