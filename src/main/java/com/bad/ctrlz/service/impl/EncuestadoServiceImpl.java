package com.bad.ctrlz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bad.ctrlz.model.Encuestado;
import com.bad.ctrlz.repository.EncuestadoRepository;
import com.bad.ctrlz.service.EncuestadoService;

@Service
public class EncuestadoServiceImpl implements EncuestadoService {

    @Autowired
    private EncuestadoRepository encuestadoRepository;

    @Override
    public Encuestado guardar(Encuestado encuestado) {
        return encuestadoRepository.save(encuestado);
    }
}
