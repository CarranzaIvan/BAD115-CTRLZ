package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.repository.EncuestaRepository;
import com.bad.ctrlz.repository.PreguntaRepository;
import com.bad.ctrlz.dto.DashboardEncuestaDTO;
import com.bad.ctrlz.dto.PreguntaDetalleDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncuestaServiceImpl implements EncuestaService {

    @Autowired
    private EncuestaRepository encuestaRepository;

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Override
    public void guardar(Encuesta encuesta) {
        encuestaRepository.save(encuesta);
    }

    @Override
    public void actualizar(Encuesta encuesta) {
        encuestaRepository.save(encuesta);
    }

    @Override
    public void eliminar(Encuesta encuesta) {
        encuestaRepository.delete(encuesta);
    }

    @Override
    public void eliminarPorId(Integer id) {
        encuestaRepository.deleteById(id.longValue());
    }

    @Override
    public List<Encuesta> obtenerTodas() {
        return encuestaRepository.findAll();
    }

    @Override
    public Encuesta obtenerPorId(Integer id) {
        return encuestaRepository.findById(id.longValue()).orElse(null);
    }

    @Override
    public Optional<Encuesta> buscarPorId(Integer id) {
        return encuestaRepository.findById(id.longValue());
    }

    @Override
    public Optional<Encuesta> buscarPorLinkPublico(String linkPublico) {
        return encuestaRepository.findByLinkPublico(linkPublico);
    }

    @Override
    public List<PreguntaDetalleDTO> obtenerPreguntasPorEncuesta(Long idEncuesta) {
        return preguntaRepository.obtenerPreguntasPorEncuesta(idEncuesta);
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
}

// Esta interfaz no es pública, pero puede usarse en el mismo paquete
interface EncuestaService {
    List<Encuesta> obtenerTodas();
    Encuesta obtenerPorId(Integer id);
    Optional<Encuesta> buscarPorId(Integer id);
    void guardar(Encuesta encuesta);
    void actualizar(Encuesta encuesta);
    void eliminar(Encuesta encuesta);
    void eliminarPorId(Integer id);
    Optional<Encuesta> buscarPorLinkPublico(String linkPublico);
    List<PreguntaDetalleDTO> obtenerPreguntasPorEncuesta(Long idEncuesta);
}
