package com.kinal.libreria_online.controller;

import com.kinal.libreria_online.DTO.BuscarUsuarioRequest;
import com.kinal.libreria_online.DTO.EditarUsuarioRequest;
import com.kinal.libreria_online.DTO.EliminarUsuarioRequest;
import com.kinal.libreria_online.DTO.LoginRequest;
import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.repository.UsuarioRepository;
import com.kinal.libreria_online.service.RoleService;
import com.kinal.libreria_online.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
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
    @Autowired

    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

            ResponseEntity<String> roleCheck = verificacionRole(roleAuth);

            if(roleCheck != null){

                return roleCheck;

            }

            verificacionRole(roleAuth);

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

    @GetMapping("/listarUsuarios")
    public ResponseEntity<?> listarUsuarios(@AuthenticationPrincipal UserDetails userDetails){

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = verificacionRole(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        verificacionRole(roleAuth);

        List<Usuario> usuarios = usuarioService.listarUsuarios();

        if(usuarios.isEmpty()){

            return ResponseEntity.noContent().build();

        }

        return ResponseEntity.ok(usuarios);

    }

    @DeleteMapping("/eliminarUsuario")
    public ResponseEntity<?> eliminarUsuario(@RequestBody EliminarUsuarioRequest request, @AuthenticationPrincipal UserDetails userDetails){

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = verificacionRole(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        verificacionRole(roleAuth);

        String mensaje = usuarioService.eliminarUsuario(request.DPI, request.isAutorizacion());

        if(mensaje.equals("Operación cancelada") || mensaje.equals("Usuario no encontrado")){

            return ResponseEntity.badRequest().body(mensaje);

        }

        return ResponseEntity.ok(mensaje);

    }

    @GetMapping("/buscarUsuario")
    public ResponseEntity<?> buscarUsuario(@RequestBody BuscarUsuarioRequest request, @AuthenticationPrincipal UserDetails userDetails){

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = verificacionRole(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        verificacionRole(roleAuth);

        Usuario usuario = usuarioService.buscarPorDPI(request.getDPI());

        if (usuario == null) {
            return ResponseEntity.badRequest().body("Usuario no encontrado.");
        }

        return ResponseEntity.ok(usuario);

    }

    @PutMapping("/editarUsuario")
    public ResponseEntity<?> editarUsuario(@RequestBody EditarUsuarioRequest request, @AuthenticationPrincipal UserDetails userDetails){

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = verificacionRole(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        Usuario usuario = usuarioService.buscarPorDPI(request.getDPI());

        if(usuario == null){

            return ResponseEntity.badRequest().body("Usuario no encontrado");

        }

        if (request.getNombre() != null) {
            usuario.setNombres(request.getNombre());
        }

        if (request.getApellido() != null) {
            usuario.setApellidos(request.getApellido());
        }

        if (request.getEdad() != null) {
            usuario.setEdad(request.getEdad());
        }

        if (request.getClave() != null) {

            String claveCifrada = passwordEncoder.encode(request.getClave());

            usuario.setClave(claveCifrada);

        }

        if(request.getEmail() != null){

            usuario.setEmail(request.getEmail());

        }

        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }

        if (request.getDireccion() != null) {
            usuario.setDireccion(request.getDireccion());
        }

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuario actualizado exitosamente");

    }

    public ResponseEntity<String> verificacionRole(String roleAuth){

        if("admin".equals(roleAuth)){

            return null;

        }

        return ResponseEntity.status(403).body("Role no autorizado");

    }

    public ResponseEntity<String> verificacionRoleGeneral(String roleAuth){

        if("admin".equals(roleAuth) || "user".equals(roleAuth)){

            return null;

        }

        return ResponseEntity.status(403).body("Role no autorizado");

    }


}
