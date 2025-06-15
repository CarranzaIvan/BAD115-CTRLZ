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
        System.out.println(usuario.toString());

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
     * Obtener listado de usuarios
     */
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

}
