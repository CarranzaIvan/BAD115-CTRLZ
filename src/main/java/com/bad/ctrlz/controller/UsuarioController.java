package com.bad.ctrlz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bad.ctrlz.model.Usuario;
import com.bad.ctrlz.service.UsuarioService;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
public class UsuarioController {
  @Autowired
  private UsuarioService usuarioService;
  
  public void guardarUsuario(Usuario usuario) {
    usuarioService.guardarUsuario(usuario);
  }
}

/*
 
 */