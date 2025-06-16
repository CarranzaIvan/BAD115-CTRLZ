package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Pregunta;
import com.bad.ctrlz.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreguntaService {
    @Autowired
    private PreguntaRepository preguntaRepository;

    // Guardar o actualizar una pregunta
    public Pregunta guardar(Pregunta pregunta) {
        return preguntaRepository.save(pregunta);
    }

    // Listar preguntas por encuesta
    public List<Pregunta> listarPorEncuesta(Integer idEncuesta) {
        return preguntaRepository.findByEncuestaConTipoPregunta(idEncuesta);
    }

    // Buscar una pregunta por ID
    public Pregunta buscarPorId(Integer idPregunta) {
        return preguntaRepository.findById(idPregunta).orElse(null);
    }

    // Eliminar pregunta por ID
    public void eliminar(Integer idPregunta) {
        preguntaRepository.deleteById(idPregunta);
    }

    // Listar todas las preguntas (opcional)
    public List<Pregunta> listarTodas() {
        return preguntaRepository.findAll();
    } 

    public List<Pregunta> obtenerPreguntasPorEncuesta(Integer idEncuesta) {
        return preguntaRepository.findByEncuestaIdEncuesta(idEncuesta);
    }

}
