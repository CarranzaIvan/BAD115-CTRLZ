package com.bad.ctrlz.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Guarda un usuario en la base de datos.
     * 
     * @param usuario El usuario a guardar.
     * @return true si el usuario fue guardado exitosamente, false si ya existe.
     */
    public boolean guardarUsuario(Usuario usuario) {
        Optional <Usuario> existe = usuarioRepository.findByEmail(usuario.getCorreo());
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
}
