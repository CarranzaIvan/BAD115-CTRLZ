package com.bad.ctrlz.service;

import org.springframework.stereotype.Service;  
import com.bad.ctrlz.model.Encuestado;

@Service
public interface EncuestadoService {
    Encuestado guardar(Encuestado encuestado);
}
