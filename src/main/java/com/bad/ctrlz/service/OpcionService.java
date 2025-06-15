package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Opcion;
import com.bad.ctrlz.repository.OpcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OpcionService {

    @Autowired
    private OpcionRepository opcionRepository;

    public void guardar(Opcion opcion) {
        opcionRepository.save(opcion);
    }

    public void eliminar(Integer id) {
        opcionRepository.deleteById(id);
    }

    public List<Opcion> listarTodos() {
        return opcionRepository.findAll();
    }

    public Optional<Opcion> buscarPorId(Integer id) {
        return opcionRepository.findById(id);
    }
}
