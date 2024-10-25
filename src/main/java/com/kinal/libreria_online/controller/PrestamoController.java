package com.kinal.libreria_online.controller;

import com.kinal.libreria_online.DTO.PrestamoRequest;
import com.kinal.libreria_online.model.Prestamo;
import com.kinal.libreria_online.service.LibroService;
import com.kinal.libreria_online.service.PrestamoService;
import com.kinal.libreria_online.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/prestamo")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private LibroService libroService;

    @PostMapping("/realizarPrestamo")
    public ResponseEntity<?> realizarPrestamo(@RequestBody PrestamoRequest request,
                                              @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("Entro");

        String roleAuth = usuarioService.obtenerUsuarioPorEmail(userDetails.getUsername()).getRole();

        ResponseEntity<String> roleCheck = usuarioController.verificacionRoleGeneral(roleAuth);

        if(roleCheck != null){

            return roleCheck;

        }

        try {

            String resultado = prestamoService.realizarPrestamo(request.getIsbn(), userDetails.getUsername());

            return ResponseEntity.ok(resultado);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/historialPrestamos")
    public ResponseEntity<List<Prestamo>> obtenerHistorialPrestamos(@AuthenticationPrincipal UserDetails userDetails) {
        List<Prestamo> prestamos = prestamoService.obtenerPrestamosPorUsuario(userDetails.getUsername());
        return ResponseEntity.ok(prestamos);
    }

    @PostMapping("/devolverLibro")
    public ResponseEntity<?> devolverLibro(@RequestBody PrestamoRequest request) {
        String isbn = request.getIsbn();
        try {
            Prestamo prestamo = prestamoService.obtenerPrestamoPorIsbn(isbn);
            if (prestamo == null) {
                return ResponseEntity.badRequest().body("Préstamo no encontrado para el ISBN proporcionado.");
            }

            prestamo.setFechaFinPrestamo(new Date());
            prestamoService.actualizarPrestamo(prestamo);
            
            prestamoService.cambiarEstadoLibro(isbn, false);

            return ResponseEntity.ok("Libro devuelto exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
