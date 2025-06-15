package com.bad.ctrlz.service.impl;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.repository.EncuestaRepository;
import com.bad.ctrlz.service.EncuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncuestaServiceImpl implements EncuestaService {

    @Autowired
    private final EncuestaRepository encuestaRepository;

    public EncuestaServiceImpl(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    @Override
    public List<Encuesta> obtenerTodas() {
        return encuestaRepository.findAll();
    }

    @Override
    public Encuesta obtenerPorId(Integer id) {
        Optional<Encuesta> encuesta = encuestaRepository.findById(id);
        return encuesta.orElse(null);
    }


    @Override
    public void eliminar(Encuesta encuesta) {
        encuestaRepository.delete(encuesta);
    }

    @Override
    public void guardar(Encuesta encuesta) {
        encuestaRepository.save(encuesta);
    }

    @Override
    public void eliminarPorId(Integer id) {
        encuestaRepository.deleteById(id);
    }

   @Override
    public Optional<Encuesta> buscarPorId(Integer id) {
        return encuestaRepository.findById(id);
    }

    @Override
    public void actualizar(Encuesta encuesta) {
        encuestaRepository.save(encuesta);
    }
    @Override
    public Optional<Encuesta> buscarPorLinkPublico(String linkPublico) {
        return encuestaRepository.findByLinkPublico(linkPublico);
    }
}
