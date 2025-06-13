package com.bad.ctrlz.seed;

import com.bad.ctrlz.model.TipoPregunta;
import com.bad.ctrlz.repository.TipoPreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private TipoPreguntaRepository tipoPreguntaRepository;

    @Override
    public void run(String... args) {
        System.out.println("Ejecutando DataSeeder...");

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
    }

    private void insertarSiNoExiste(String nombreTipo) {
        if (!tipoPreguntaRepository.existsByNombreTipo(nombreTipo)) {
            tipoPreguntaRepository.save(new TipoPregunta(null, nombreTipo));
            System.out.println("Tipo de pregunta insertado: " + nombreTipo);
        }
    }
}






