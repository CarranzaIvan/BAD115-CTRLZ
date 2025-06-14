package com.bad.ctrlz.seed;

import com.bad.ctrlz.model.TipoPregunta;
import com.bad.ctrlz.repository.TipoPreguntaRepository;
import com.bad.ctrlz.service.RolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private TipoPreguntaRepository tipoPreguntaRepository;
    @Autowired
    private RolService rolService; // Asumiendo que tienes un RolRepository

    @Override
    public void run(String... args) {
        System.out.println("Ejecutando DataSeeder...");

        // Inserción de roles si no existen
        insertarSiNoExiste("Abiertas");
        insertarSiNoExiste("Cerradas");
        insertarSiNoExiste("Mixtas");
        insertarSiNoExiste("Elección Única");
        insertarSiNoExiste("Elección Múltiple");
        insertarSiNoExiste("Ranking");
        insertarSiNoExiste("Escala");
        insertarSiNoExiste("Dicotómica");
        insertarSiNoExiste("Politómica");
        insertarSiNoExiste("Numérica");
        insertarSiNoExiste("Nominal");
        insertarSiNoExiste("Likert");

        // Inserción de roles si no existen
        insertarSiNoExisteRol("ADMINISTRADOR");
        insertarSiNoExisteRol("USUARIO");
    }

    // Insersión de tipos de pregunta si no existen
    /**
     * Inserta un tipo de pregunta en la base de datos si no existe.
     *
     * @param nombreTipo El nombre del tipo de pregunta a insertar.
     */
    private void insertarSiNoExiste(String nombreTipo) {
        if (!tipoPreguntaRepository.existsByNombreTipo(nombreTipo)) {
            tipoPreguntaRepository.save(new TipoPregunta(null, nombreTipo));
            System.out.println("Tipo de pregunta insertado: " + nombreTipo);
        }
    }

    // Insersion de roles si no existen
    /**
     * Inserta un tipo de pregunta en la base de datos si no existe.
     *
     * @param nombreRol El nombre del tipo de pregunta a insertar.
     */
    private void insertarSiNoExisteRol(String nombreRol) {
        if(!rolService.existeRol(nombreRol)) {
            rolService.insertarRol(nombreRol);
        } else {
            System.out.println("El rol ya existe: " + nombreRol);
        }
    }
}






