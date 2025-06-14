package com.bad.ctrlz.service;

import com.bad.ctrlz.model.TipoPregunta;
import com.bad.ctrlz.repository.TipoPreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoPreguntaService {
    
    @Autowired
    private TipoPreguntaRepository tipoPreguntaRepository;

    public List<TipoPregunta> listarTodos() {
    return tipoPreguntaRepository.findAll();
    }

    public TipoPregunta buscarPorNombre(String nombre) {
    return tipoPreguntaRepository.findByNombreTipo(nombre);
    }

    public TipoPregunta buscarPorId(Integer id) {
        return tipoPreguntaRepository.findById(id).orElse(null);
    }
}
