package com.kinal.libreria_online.service;

import com.kinal.libreria_online.model.Libro;
import com.kinal.libreria_online.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public Libro crearLibro(Libro libro){

        libro.setIsbn(generarISBN());

        libro.setPrestado(false);

        return libroRepository.save(libro);

    }

    public List<Libro> listarLibros() {
        return libroRepository.findAll();
    }

    public Optional<Libro> buscarLibroPorISBN(String isbn) {
        return libroRepository.findByIsbn(isbn);
    }

    public Libro actualizarLibro(String isbn, Libro libroDetalles) {
        Optional<Libro> optionalLibro = libroRepository.findByIsbn(isbn);
        if (optionalLibro.isPresent()) {
            Libro libro = optionalLibro.get();
            libro.setTitulo(libroDetalles.getTitulo());
            libro.setAutor(libroDetalles.getAutor());
            libro.setEditorial(libroDetalles.getEditorial());
            libro.setGenero(libroDetalles.getGenero());
            libro.setIdioma(libroDetalles.getIdioma());
            libro.setPrestado(libroDetalles.isPrestado());
            return libroRepository.save(libro);
        }
        return null;
    }

    public String eliminarLibro(String isbn, boolean autorizacion) {

        if(autorizacion == false) {

            return "proceso cancelado";

        }

        Optional<Libro> optionalLibro = libroRepository.findByIsbn(isbn);

        if (optionalLibro.isPresent()) {
            libroRepository.delete(optionalLibro.get());
            return "Libro eliminado.";
        }
        return "Libro no encontrado.";
    }

    private String generarISBN() {
        return "ISBN-" + UUID.randomUUID().toString();
    }



}
