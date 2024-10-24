package com.kinal.libreria_online.service;

import com.kinal.libreria_online.exceptions.DPIAlreadyUsedException;
import com.kinal.libreria_online.exceptions.EmailAlreadyUsedException;
import com.kinal.libreria_online.exceptions.RoleNotValidException;
import com.kinal.libreria_online.factory.UsuarioFactory;
import com.kinal.libreria_online.DTO.LoginRequest;
import com.kinal.libreria_online.model.Usuario;
import com.kinal.libreria_online.repository.RoleRepository;
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
import java.util.List;

@Service
public class UsuarioService {

    private static Usuario usuarioPorDefecto;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

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

        if(usuarioRepository.existsByEmail(usuario.getEmail())){

            throw new EmailAlreadyUsedException("El correo electrónico ya está en uso.");

        }

        if (usuarioRepository.existsByDPI(usuario.getDPI())) {
            throw new DPIAlreadyUsedException("El DPI ya está en uso.");
        }

        if (!roleRepository.existsByRoleName(usuario.getRole())) {
            throw new RoleNotValidException("El rol proporcionado no existe o no es válido.");
        }

        return guardarUsuario(usuario);

    }

    private Usuario guardarUsuario(Usuario usuario){

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

    public List<Usuario> listarUsuarios(){

        return usuarioRepository.findAll();

    }

    public String eliminarUsuario(BigInteger DPI, boolean autorizacion){

        if(!autorizacion){

            return "Operación Cancelada.";

        }

        System.out.println(DPI);

        Usuario usuario = obtenerUsuarioPorDPI(DPI);

        if(usuario == null){

            return "Usuario no encontrado.";

        }

        usuarioRepository.delete(usuario);

        return "Usuario eliminado.";

    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario obtenerUsuarioPorDPI(BigInteger DPI){

        return usuarioRepository.findByDPI(DPI);

    }

    public boolean existePorDPI(BigInteger DPI) {

        return usuarioRepository.existsByDPI(DPI);

    }


}
