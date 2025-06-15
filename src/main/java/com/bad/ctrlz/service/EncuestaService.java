package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.repository.EncuestaRepository;
import com.bad.ctrlz.repository.PreguntaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bad.ctrlz.dto.DashboardEncuestaDTO;
import com.bad.ctrlz.dto.PreguntaDetalleDTO;

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

    public List<DashboardEncuestaDTO> obtenerDashboardRespuestas() {
    List<Object[]> resultados = encuestaRepository.obtenerDashboardRespuestas();

    return resultados.stream()
        .map(obj -> {
            Encuesta encuesta = (Encuesta) obj[0];
            Long total = (Long) obj[1];
            return new DashboardEncuestaDTO(encuesta.getIdEncuesta().longValue(), encuesta.getTitulo(), total);
        })
        .toList();
    }

    @Autowired
    private PreguntaRepository preguntaRepository;
    
    public List<PreguntaDetalleDTO> obtenerPreguntasPorEncuesta(Long idEncuesta) {
    return preguntaRepository.obtenerPreguntasPorEncuesta(idEncuesta);
    }


}
