package com.bad.ctrlz.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bad.ctrlz.configuraciones.Seguridad.PasswordGenerator;
import com.bad.ctrlz.configuraciones.Seguridad.CorreoElectronico.SendMail;
import com.bad.ctrlz.model.Rol;
import com.bad.ctrlz.model.Usuario;
import com.bad.ctrlz.repository.RolRepository;
import com.bad.ctrlz.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private SendMail emailService;

    /**
     * Guarda un usuario en la base de datos.
     * 
     * @param usuario El usuario a guardar.
     * @return true si el usuario fue guardado exitosamente, false si ya existe.
     */
    public boolean guardarUsuario(Usuario usuario) {
        Optional<Usuario> existe = usuarioRepository.findByEmail(usuario.getCorreo());
        if (existe.isPresent()) {
            return false; // El usuario ya existe
        }

        // Asignar rol de administrador al usuario
        Set<Rol> rolUsuario = new HashSet<>();
        rolUsuario.add(rolRepository.encontrarUsuario());
        usuario.setRoles(rolUsuario);

        // Aquí puedes agregar lógica adicional antes de guardar el usuario
        // String cotrasena = usuario.getPassword();
        //usuario.setPassword(passwordEncoder.encode(contrasena));
        usuarioRepository.save(usuario);

        return true; // Usuario guardado exitosamente
    }

    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param correo El correo electrónico del usuario a buscar.
     * @return Un Bolean que indica si el usuario existe o no.
     */
    public boolean validarEmail(String correo) {
        return usuarioRepository.existsByEmail(correo);
    }

    /**
     * Busca un usuario y verifica si esta bloqueado.
     * 
     * @param correo El correo electrónico del usuario a buscar.
     * @return Un Bolean que indica si el usuario existe o no.
     */
    public boolean validarBloqueo(String correo) {
        return usuarioRepository.existsUnlockedUserByCorreo(correo);
    }

    /*
     * Envio de correo electrónico para la recuperación de contraseña
     */
    public Boolean enviarCorreoRecuperacion(String correo) {
        // Informacion estatica
        String subject = "Recuperación de credenciales";
        String descripcion = "Hemos recibido su solicitud de recuperación de credenciales para acceder al sistema de encuestas CTRLZ.\r\n"
                + //
                "A continuación, encontrará la información necesaria para restablecer su acceso:\r\n" + //
                "";
        String consideracion = "Si no ha solicitado este cambio, le recomendamos ignorar este mensaje o comunicarse con nuestro equipo de soporte a través de ctrlzbad@gmail.com para mayor seguridad.\r\n";

        // Datos de usuario
        String nuevaContrasena = passwordGenerator.generateRandomPassword(8);
        Usuario usuario = usuarioRepository.encontrarPorCorreo(correo);
        String nombre = usuario.getNombre() + " " + usuario.getApellido();

        boolean enviado = false;
        enviado = emailService.sendEmail(subject, descripcion, correo, nuevaContrasena, nombre,
                consideracion);

        //usuario.getPassword(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
        return enviado;
    }

    /*
     * Contar número de usuarios activos
     */
    public Integer obtenerActivos() {
        Integer activos = usuarioRepository.contarUsuariosActivos();
        return activos;
    }

    /*
     * Contar número de usuarios bloqueados
     */
    public Integer obtenerBloqueados() {
        Integer bloqueados = usuarioRepository.contarUsuariosBloqueados();
        return bloqueados;
    }

    /*
     * Contar número de usuarios inactivos
     */
    public Integer obtenerInactivos() {
        Integer inactivos = usuarioRepository.contarUsuariosDesactivados();
        return inactivos;
    }

    /*
     * Contar número de peticiones de desbloqueo
     */
    public Integer obtenerSolicitud() {
        Integer solicitudes = usuarioRepository.contarSolicitudDesbloqueo();
        return solicitudes;
    }

    /*
     * Obtener listado de usuarios
     */
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    /*
     * Actualizar usuario
     */
    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    /**
     * Envía un correo para notificar el desbloqueo de un usuario y genera nueva
     * contraseña.
     * 
     * @param correo Correo electrónico del usuario a desbloquear
     * @return true si el correo fue enviado exitosamente, false si no
     */
    public Boolean enviarDesbloqueo(String correo) {
        // Plantilla del mensaje
        final String subject = "Desbloqueo de credenciales";
        final String descripcion = """
                Hemos recibido su solicitud de desbloqueo de credenciales para acceder al sistema de encuestas CTRLZ.
                A continuación, encontrará la información necesaria para acceder con su usuario desbloqueado:
                """;
        final String consideracion = """
                Si no ha solicitado este cambio, le recomendamos ignorar este mensaje o comunicarse con nuestro equipo de soporte a través de ctrlzbad@gmail.com para mayor seguridad.
                """;

        // Validar existencia del usuario
        Usuario usuario = usuarioRepository.encontrarPorCorreo(correo);
        if (usuario == null) {
            return false; // Usuario no encontrado
        }

        // Generar nueva contraseña y actualizarla si es necesario
        String nuevaContrasena = passwordGenerator.generateRandomPassword(8);
        String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();

        // Aquí puedes guardar la nueva contraseña en la base si aplica
        //usuario.setPassword(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);

        // Enviar correo
        return emailService.sendEmail(subject, descripcion, correo, nuevaContrasena, nombreCompleto, consideracion);
    }

    public Boolean enviarRechazoBloqueo(String correo) {
        // Plantilla del mensaje
        final String subject = "Rechazo de desbloqueo";
        final String descripcion = """
                Hemos recibido su solicitud de desbloqueo de credenciales, pero ha sido RECHAZADA para acceder al sistema de encuestas CTRLZ.
                """;
        final String consideracion = """
                Si ha solicitado este cambio, le recomendamos comunicarse con nuestro equipo de soporte a través de ctrlzbad@gmail.com para mayor seguridad.
                """;

        // Validar existencia del usuario
        Usuario usuario = usuarioRepository.encontrarPorCorreo(correo);
        if (usuario == null) {
            return false; // Usuario no encontrado
        }

        // Generar nueva contraseña y actualizarla si es necesario
        String nuevaContrasena = "-- RECHAZADO --";
        String nombreCompleto = usuario.getNombre() + " " + usuario.getApellido();
        // Enviar correo
        return emailService.sendEmail(subject, descripcion, correo, nuevaContrasena, nombreCompleto, consideracion);
    }

    /**
     * Retorna una lista de usuarios bloqueados.
     * 
     * @return lista de usuarios en estado bloqueado
     */
    public List<Usuario> obtenerListadoBloqueados() {
        return usuarioRepository.findBloqueados();
    }

    /**
     * Retorna una lista de usuarios que han solicitado desbloqueo.
     * 
     * @return lista de usuarios con solicitud de desbloqueo
     */
    public List<Usuario> obtenerListadoSolicitud() {
        return usuarioRepository.findSolicitud();
    }

    /*
     * Rechazar peticion de desbloqueo
     */
    public void rechazarSolicitud(Usuario usuario) {
        usuario.setAccountNoExpired(true);
        usuarioRepository.save(usuario);
    }

    /*
     * Aceptar peticion de desbloqueo
     */
    public void aceptarSolicitud(Usuario usuario) {
        usuario.setAccountLocked(false);
        usuario.setAccountNoExpired(true);
        usuarioRepository.save(usuario);
    }
    //Redireccionamiento a inicio
    public boolean validarCredenciales(String correo, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Validamos la contraseña
            return usuario.getPassword().equals(password);
        }

        return false;
    }

}
