package com.kinal.libreria_online.service;

import com.kinal.libreria_online.factory.UsuarioFactory;
import com.kinal.libreria_online.model.LoginRequest;
import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.repository.UsuarioRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.Key;
import java.util.Date;

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

    public String autenticarUsuario(LoginRequest loginRequest){

        Usuario usuario = obtenerUsuarioPorEmail(loginRequest.getEmail());

        if(usuario != null && passwordEncoder.matches(loginRequest.getPassword(), usuario.getClave())){

            return generarToken(usuario);

        }

        return null;

    }

    private String generarToken(Usuario usuario) {
        Key key = Keys.hmacShaKeyFor("mi_clave_secreta_para_jwt_que_debe_ser_lo_suficientemente_larga".getBytes());

        return Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("DPI", usuario.getDPI())
                .claim("nombres", usuario.getNombres())
                .claim("apellidos", usuario.getApellidos())
                .claim("edad", usuario.getEdad())
                .claim("clave", usuario.getClave())
                .claim("telefono", usuario.getTelefono())
                .claim("direccion", usuario.getDireccion())
                .claim("role", usuario.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}
