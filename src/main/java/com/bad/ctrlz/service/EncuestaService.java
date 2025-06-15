package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Encuesta;
import com.bad.ctrlz.dto.DashboardEncuestaDTO;
import com.bad.ctrlz.dto.PreguntaDetalleDTO;

import java.util.List;
import java.util.Optional;

public interface EncuestaService {

    List<Encuesta> obtenerTodas();
    Optional<Encuesta> buscarPorId(Long id);
    void eliminarPorId(Long id);
    void eliminar(Encuesta encuesta);
    void guardar(Encuesta encuesta);
    void actualizar(Encuesta encuesta);
    Optional<Encuesta> buscarPorLinkPublico(String linkPublico);
    List<PreguntaDetalleDTO> obtenerPreguntasPorEncuesta(Long idEncuesta);
    List<DashboardEncuestaDTO> obtenerDashboardRespuestas();
}
