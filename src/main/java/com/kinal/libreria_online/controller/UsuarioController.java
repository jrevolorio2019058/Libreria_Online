package com.kinal.libreria_online.controller;

import com.kinal.libreria_online.model.LoginRequest;
import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.service.RoleService;
import com.kinal.libreria_online.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

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

    @PostMapping("/crearUsuario")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @AuthenticationPrincipal UserDetails userDetails) {

        try{

            String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

            if(!"admin".equals(roleAuth)){

                return ResponseEntity.status(403).body("Role no autorizado");

            }

            if(!roleService.existeRol(roleAuth)){

                return ResponseEntity.status(400).body("Role no existe: " + roleAuth);

            }

            if (usuarioService.existePorEmail(usuario.getEmail())) {
                return ResponseEntity.badRequest().body("El correo ya está en uso.");
            }

            if (usuarioService.existePorDPI(usuario.getDPI())) {
                return ResponseEntity.badRequest().body("El DPI ya está en uso.");
            }

            if (!roleService.existeRol(usuario.getRole())) {
                return ResponseEntity.badRequest().body("El rol no existe o no es válido.");
            }

            Usuario usuarioCreado = usuarioService.crearUsuario(usuario);

            return ResponseEntity.ok(usuarioCreado);

        }catch(Exception e){

            return ResponseEntity.status(500).body("Error | Usuario no creado: " + e.getMessage());

        }

    }

    @GetMapping("/")
    public ResponseEntity<?> listarUsuarios(@AuthenticationPrincipal UserDetails userDetails){

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        if(!"admin".equals(roleAuth)){

            return ResponseEntity.status(403).body("Role no autorizado");

        }

        if(!roleService.existeRol(roleAuth)){

            return ResponseEntity.status(400).body("Role no existe: " + roleAuth);

        }

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        if(usuarios.isEmpty()){

            return ResponseEntity.noContent().build();

        }

        return ResponseEntity.ok(usuarios);

    }


}
