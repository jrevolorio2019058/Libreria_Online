package com.kinal.libreria_online.controller;

import com.kinal.libreria_online.model.LoginRequest;
import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario){

        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){

        String token = usuarioService.autenticarUsuario(loginRequest);

        if(token != null){

            Map<String, String> response = new HashMap<>();

            response.put("Estado | ", "Logeo Exitoso, su token es: " + token);

            return ResponseEntity.ok(response.toString());

        }else{

            return ResponseEntity.status(401).body("Credenciales inválidas");

        }

    }

}
