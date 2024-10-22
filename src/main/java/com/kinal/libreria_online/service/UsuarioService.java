package com.kinal.libreria_online.service;

import com.kinal.libreria_online.factory.UsuarioFactory;
import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;

@Service
public class UsuarioService {

    private static Usuario usuarioPorDefecto;

    public static Usuario getUsuarioPorDefecto() {

        if (usuarioPorDefecto == null) {

            usuarioPorDefecto = new Usuario();

            usuarioPorDefecto.setDPI(new BigInteger("260353127070101"));

            usuarioPorDefecto.setNombres("Jorge Abraham");

            usuarioPorDefecto.setApellidos("Revolorio Mazariegos");

            usuarioPorDefecto.setEdad(19);

            usuarioPorDefecto.setEmail("jorgeabraham@gmail.com");

            usuarioPorDefecto.setClave("admin");

            usuarioPorDefecto.setTelefono("49483571");

            usuarioPorDefecto.setDireccion("Zona 11 de Mixco, Guatemala");

            usuarioPorDefecto.setRole("admin");


        }

        return usuarioPorDefecto;

    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioFactory usuarioFactory;

    @PostConstruct
    public void init(){

        if(usuarioRepository.count() == 0) {

            Usuario usuarioPorDefecto = usuarioFactory.crearUsuarioDefecto();

            usuarioPorDefecto.setClave(passwordEncoder.encode(usuarioPorDefecto.getClave()));

            usuarioRepository.save(usuarioPorDefecto);

        }

    }

    public Usuario crearUsuario(Usuario usuario){

        usuario.setClave(passwordEncoder.encode(usuario.getClave()));

        return usuarioRepository.save(usuario);

    }

    public Usuario obtenerUsuarioPorEmail(String email){

        return usuarioRepository.findByEmail(email);

    }


}
