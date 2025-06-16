package com.bad.ctrlz.seed;

import com.bad.ctrlz.model.Rol;
import com.bad.ctrlz.model.TipoPregunta;
import com.bad.ctrlz.model.Usuario;
import com.bad.ctrlz.repository.TipoPreguntaRepository;
import com.bad.ctrlz.service.RolService;
import com.bad.ctrlz.service.UsuarioService;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private TipoPreguntaRepository tipoPreguntaRepository;
    @Autowired
    private RolService rolService; // Asumiendo que tienes un RolRepository
    @Autowired
    private UsuarioService usuarioService;

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

        // 5 usuarios bloqueados
        insertarUsuarioSiNoExiste(
                new Usuario("bloqueado1@correo.com", "admin123", "Bloqueado", "Uno", true, true, true));
        insertarUsuarioSiNoExiste(
                new Usuario("bloqueado2@correo.com", "admin123", "Bloqueado", "Dos", true, true, true));
        insertarUsuarioSiNoExiste(
                new Usuario("bloqueado3@correo.com", "admin123", "Bloqueado", "Tres", true, true, true));
        insertarUsuarioSiNoExiste(
                new Usuario("bloqueado4@correo.com", "admin123", "Bloqueado", "Cuatro", true, true, true));
        insertarUsuarioSiNoExiste(
                new Usuario("bloqueado5@correo.com", "admin123", "Bloqueado", "Cinco", true, true, true));

        // 5 solicitudes de desbloqueo (bloqueados y cuenta expirada)
        insertarUsuarioSiNoExiste(
                new Usuario("solicitud1@correo.com", "admin123", "Solicitud", "Uno", true, false, true));
        insertarUsuarioSiNoExiste(
                new Usuario("solicitud2@correo.com", "admin123", "Solicitud", "Dos", true, false, true));
        insertarUsuarioSiNoExiste(
                new Usuario("solicitud3@correo.com", "admin123", "Solicitud", "Tres", true, false, true));
        insertarUsuarioSiNoExiste(
                new Usuario("solicitud4@correo.com", "admin123", "Solicitud", "Cuatro", true, false, true));
        insertarUsuarioSiNoExiste(
                new Usuario("solicitud5@correo.com", "admin123", "Solicitud", "Cinco", true, false, true));

        // 5 usuarios activos
        insertarUsuarioSiNoExiste(new Usuario("activo1@correo.com", "admin123", "Activo", "Uno", false, true, true));
        insertarUsuarioSiNoExiste(new Usuario("activo2@correo.com", "admin123", "Activo", "Dos", false, true, true));
        insertarUsuarioSiNoExiste(new Usuario("activo3@correo.com", "admin123", "Activo", "Tres", false, true, true));
        insertarUsuarioSiNoExiste(new Usuario("activo4@correo.com", "admin123", "Activo", "Cuatro", false, true, true));
        insertarUsuarioSiNoExiste(new Usuario("activo5@correo.com", "admin123", "Activo", "Cinco", false, true, true));

        // 5 usuarios inactivos (enabled = false)
        insertarUsuarioSiNoExiste(
                new Usuario("inactivo1@correo.com", "admin123", "Inactivo", "Uno", false, true, false));
        insertarUsuarioSiNoExiste(
                new Usuario("inactivo2@correo.com", "admin123", "Inactivo", "Dos", false, true, false));
        insertarUsuarioSiNoExiste(
                new Usuario("inactivo3@correo.com", "admin123", "Inactivo", "Tres", false, true, false));
        insertarUsuarioSiNoExiste(
                new Usuario("inactivo4@correo.com", "admin123", "Inactivo", "Cuatro", false, true, false));
        insertarUsuarioSiNoExiste(
                new Usuario("inactivo5@correo.com", "admin123", "Inactivo", "Cinco", false, true, false));

        // 3 usuarios personalizados
        insertarUsuarioAdministrador(new Usuario("cs21024@ues.edu.sv", "admin123", "Ivan", "Carranza", false, true, true));
        insertarUsuarioAdministrador(new Usuario("lr21008@ues.edu.sv", "admin123", "Eduardo", "Lopez", false, true, true));
        insertarUsuarioAdministrador(
                new Usuario("vp20007@ues.edu.sv", "admin123", "Karla", "Valladares", false, true, true));

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
     * Inserta un rol en la base de datos si no existe.
     *
     * @param nombreRol El nombre del tipo de pregunta a insertar.
     */
    private void insertarSiNoExisteRol(String nombreRol) {
        if (!rolService.existeRol(nombreRol)) {
            rolService.insertarRol(nombreRol);
        } else {
            System.out.println("El rol ya existe: " + nombreRol);
        }
    }

    /*
     * INSERCION DE USUARIOS - ADMINISTRADORES
     * Insertar usuario en la base de datos si no existe.
     * 
     * @param usuario usuario a ser inertado
     */
    private void insertarUsuarioSiNoExiste(Usuario usuario) {
        Rol rol = rolService.obtenerRolUsuario();
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);

        if (!usuarioService.validarEmail(usuario.getCorreo())) {
            usuario.setRoles(roles);
            usuarioService.guardarUsuario(usuario);

        } else {
            System.out.println("El usuario ya existe: " + usuario.getCorreo());
        }
    }

    private void insertarUsuarioAdministrador(Usuario usuario) {
        Set<Rol> rol = rolService.obtenerRolAdministrador();
        if (!usuarioService.validarEmail(usuario.getCorreo())) {
            usuario.setRoles(rol);
            usuarioService.guardarUsuario(usuario);
        } else {
            System.out.println("El usuario ya existe: " + usuario.getCorreo());
        }
    }
}
