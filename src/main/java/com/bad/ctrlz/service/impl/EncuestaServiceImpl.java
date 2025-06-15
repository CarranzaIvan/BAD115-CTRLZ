package com.bad.ctrlz.service.impl;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.repository.EncuestaRepository;
import com.bad.ctrlz.repository.PreguntaRepository;
import com.bad.ctrlz.dto.DashboardEncuestaDTO;
import com.bad.ctrlz.dto.PreguntaDetalleDTO;
import com.bad.ctrlz.service.EncuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EncuestaServiceImpl implements EncuestaService {

    private final EncuestaRepository encuestaRepository;
    private final PreguntaRepository preguntaRepository;

    @Autowired
    public EncuestaServiceImpl(EncuestaRepository encuestaRepository, PreguntaRepository preguntaRepository) {
        this.encuestaRepository = encuestaRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public List<Encuesta> obtenerTodas() {
        return encuestaRepository.findAll();
    }

    @Override
    public Optional<Encuesta> buscarPorId(Long id) {
        return encuestaRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Long id) {
        encuestaRepository.deleteById(id);
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
    public void actualizar(Encuesta encuesta) {
        encuestaRepository.save(encuesta);
    }

    @Override
    public Optional<Encuesta> buscarPorLinkPublico(String linkPublico) {
        return encuestaRepository.findByLinkPublico(linkPublico);
    }

    @Override
    public List<PreguntaDetalleDTO> obtenerPreguntasPorEncuesta(Long idEncuesta) {
        return preguntaRepository.obtenerPreguntasPorEncuesta(idEncuesta);
    }

    @Override
    public List<DashboardEncuestaDTO> obtenerDashboardRespuestas() {
        List<Object[]> resultados = encuestaRepository.obtenerDashboardRespuestas();
        return resultados.stream()
                .map(obj -> {
                    Encuesta encuesta = (Encuesta) obj[0];
                    Long total = (Long) obj[1];
                    return new DashboardEncuestaDTO(
                            encuesta.getIdEncuesta().longValue(),
                            encuesta.getTitulo(),
                            total);

                })
                .toList();
    }
}
