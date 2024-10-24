package com.kinal.libreria_online.controller;

import com.kinal.libreria_online.DTO.BuscarLibroDTORequest;
import com.kinal.libreria_online.model.Libro;
import com.kinal.libreria_online.service.LibroService;
import com.kinal.libreria_online.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioController usuarioController;

    @Autowired UsuarioService usuarioService;

    @PostMapping("/crearLibro")
    public ResponseEntity<?> crearLibro(@RequestBody Libro libro, @AuthenticationPrincipal UserDetails userDetails) throws ParseException {

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = usuarioController.verificacionRole(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        Libro nuevoLibro = libroService.crearLibro(libro);

        return ResponseEntity.ok("Libro creado exitosamente con ISBN: " + nuevoLibro.getIsbn());

    }

    @GetMapping("/listarLibros")
    public ResponseEntity<?> listarLibros(@AuthenticationPrincipal UserDetails userDetails) {

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = usuarioController.verificacionRole(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        List<Libro> libros = libroService.listarLibros();

        return ResponseEntity.ok(libros);

    }

    @GetMapping("/buscarLibro")
    public ResponseEntity<?> buscarLibroPorISBN(@RequestBody BuscarLibroDTORequest buscarLibroDTO, @AuthenticationPrincipal UserDetails userDetails) {

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = usuarioController.verificacionRoleGeneral(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        if (buscarLibroDTO.getIsbn() == null || buscarLibroDTO.getIsbn().isEmpty()) {
            return ResponseEntity.badRequest().body("El ISBN no puede estar vacío.");
        }

        Optional<Libro> libro = libroService.buscarLibroPorISBN(buscarLibroDTO.getIsbn());

        if (libro.isPresent()) {
            return ResponseEntity.ok(libro.get());
        } else {
            return ResponseEntity.badRequest().body("Libro no encontrado.");
        }

    }

    @PutMapping("/actualizar/{isbn}")
    public ResponseEntity<?> actualizarLibro(@PathVariable String isbn, @RequestBody Libro libroDetalles) {
        Libro libroActualizado = libroService.actualizarLibro(isbn, libroDetalles);
        if (libroActualizado == null) {
            return ResponseEntity.badRequest().body("Libro no encontrado.");
        }
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/eliminar/{isbn}")
    public ResponseEntity<String> eliminarLibro(@PathVariable String isbn) {
        String resultado = libroService.eliminarLibro(isbn);
        if (resultado.equals("Libro no encontrado.")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }

}
