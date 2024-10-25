package com.kinal.libreria_online.service;

import com.kinal.libreria_online.model.Libro;
import com.kinal.libreria_online.model.Prestamo;
import com.kinal.libreria_online.repository.LibroRepository;
import com.kinal.libreria_online.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private LibroService libroService;

    public String realizarPrestamo(String isbn, String username){

        Optional<Libro> optionalLibro = libroRepository.findByIsbn(isbn);

        if(optionalLibro.isEmpty()){

            throw new RuntimeException("Libro no encontrado");

        }

        Libro libro = optionalLibro.get();

        if(libro.isPrestado()){

            throw new RuntimeException("El libro ya esta prestado");

        }

        Prestamo prestamo = new Prestamo();

        prestamo.setIsbn(libro.getIsbn());

        prestamo.setNombreLibro(libro.getTitulo());

        prestamo.setUsuario(username);

        prestamo.setFechaInicioPrestamo(new Date());

        prestamo.setFechaFinPrestamo(null);

        prestamoRepository.save(prestamo);

        libro.setPrestado(true);

        libroRepository.save(libro);

        return "Prestamo realizado con éxito";


    }

    public List<Prestamo> obtenerPrestamosPorUsuario(String usuario) {
        return prestamoRepository.findByUsuario(usuario);
    }

    public Prestamo obtenerPrestamoPorIsbn(String isbn) {

        return prestamoRepository.findByIsbn(isbn);

    }

    public Prestamo actualizarPrestamo(Prestamo prestamo) {
        return prestamoRepository.save(prestamo);
    }

    public void cambiarEstadoLibro(String isbn, boolean prestado) {

        Optional<Libro> optionalLibro = libroRepository.findByIsbn(isbn);

        if (optionalLibro.isPresent()) {

            Libro libro = optionalLibro.get();

            libro.setPrestado(prestado);

            libroRepository.save(libro);

        } else {
            throw new RuntimeException("Libro no encontrado.");
        }
    }

}
