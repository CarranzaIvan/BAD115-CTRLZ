package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.repository.EncuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncuestaService {

    @Autowired
    private EncuestaRepository encuestaRepository;

    public void guardar(Encuesta encuesta) {
        encuestaRepository.save(encuesta);
    }

    public void eliminar(Encuesta encuesta) {
        encuestaRepository.delete(encuesta);
    }

    public void eliminarPorId(Integer id) {
        encuestaRepository.deleteById(id.longValue());
    }

    public List<Encuesta> obtenerTodas() {
        return encuestaRepository.findAll();
    }

    public Optional<Encuesta> buscarPorId(Integer id) {
        return encuestaRepository.findById(id.longValue());
    }

}
