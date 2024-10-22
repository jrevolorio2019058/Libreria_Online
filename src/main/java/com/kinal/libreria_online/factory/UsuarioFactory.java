package com.kinal.libreria_online.factory;

import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.service.UsuarioService;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {

    public Usuario crearUsuarioDefecto(){

        return UsuarioService.getUsuarioPorDefecto();

    }

}
