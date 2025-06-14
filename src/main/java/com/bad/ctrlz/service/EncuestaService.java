package com.bad.ctrlz.service;

import com.bad.ctrlz.model.Encuesta;
import java.util.List;
import java.util.Optional;

public interface EncuestaService {
    List<Encuesta> obtenerTodas();
    Encuesta obtenerPorId(Integer id);
    Optional<Encuesta> buscarPorId(Integer id);
    void guardar(Encuesta encuesta);
    void actualizar(Encuesta encuesta);
    void eliminar(Encuesta encuesta);
    void eliminarPorId(Integer id);
    Optional<Encuesta> buscarPorLinkPublico(String linkPublico);
}
