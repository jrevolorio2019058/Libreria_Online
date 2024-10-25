package com.kinal.libreria_online.prestamo;

import com.kinal.libreria_online.model.Prestamo;
import com.kinal.libreria_online.repository.PrestamoRepository;
import com.kinal.libreria_online.service.PrestamoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PrestamoServiceTest {

    @InjectMocks
    private PrestamoService prestamoService;

    @Mock
    private PrestamoRepository prestamoRepository;

    public PrestamoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerPrestamoPorIsbn_Exitoso() {

        String isbn = "123456789";
        Prestamo prestamo = new Prestamo();
        prestamo.setIsbn(isbn);
        prestamo.setNombreLibro("Libro de Prueba");
        prestamo.setUsuario("Usuario Test");

        when(prestamoRepository.findByIsbn(isbn)).thenReturn(prestamo);

        Prestamo resultado = prestamoService.obtenerPrestamoPorIsbn(isbn);

        assertEquals(isbn, resultado.getIsbn());
        assertEquals("Libro de Prueba", resultado.getNombreLibro());
        assertEquals("Usuario Test", resultado.getUsuario());

        verify(prestamoRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    public void testObtenerPrestamoPorIsbn_NoEncontrado() {
        String isbn = "987654321";

        when(prestamoRepository.findByIsbn(isbn)).thenReturn(null);

        Prestamo resultado = prestamoService.obtenerPrestamoPorIsbn(isbn);

        assertTrue(resultado == null);
        
        verify(prestamoRepository, times(1)).findByIsbn(isbn);
    }
}
