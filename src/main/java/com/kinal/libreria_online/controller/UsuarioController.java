package com.kinal.libreria_online.controller;

import com.kinal.libreria_online.model.LoginRequest;
import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario){

        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));

    }

    @GetMapping("/")
    public ResponseEntity<Usuario> obtenerUsuarioTokenActivo(@AuthenticationPrincipal UserDetails userDetails){

        Usuario usuario = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername());

        if(usuario != null){

            return ResponseEntity.ok(usuario);

        }

        return ResponseEntity.status(404).body(null);

    }


}
