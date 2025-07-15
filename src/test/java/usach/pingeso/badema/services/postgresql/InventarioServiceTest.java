package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.entities.InventarioEntity;
import usach.pingeso.badema.repositories.postgresql.InventarioRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private InventarioEntity inventario;

    @BeforeEach
    void setUp() {
        //Se crea inventario para realizar pruebas
        inventario = new InventarioEntity();
    }

    @Test
    void saveInventario_deberiaInicializarCamposYGuardar() {
        //Se coloca lo que debe retornar cuando se realiza la accion save
        when(inventarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        //Se realiza la accion de utilizar saveInventario
        InventarioEntity resultado = inventarioService.saveInventario(inventario);

        //Se verifica que cumpla con los datos de prueba
        assertNotNull(resultado.getFechaIngreso());
        assertEquals(0, resultado.getCantidad());
        assertEquals(0, resultado.getCantidadInstalada());
        assertNotNull(resultado.getFechaUltimaModificacion());

        verify(inventarioRepository).save(inventario);
    }

    @Test
    void updateInventario_deberiaActualizarFechaYGuardar() {
        //Se le coloca una fecha de prueba
        LocalDate fechaAnterior = LocalDate.of(2025, 6, 1);
        inventario.setFechaUltimaModificacion(fechaAnterior);

        //Se coloca lo que debe retornar cuando se realiza la accion save
        when(inventarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        //Se realiza la accion de utilizar updateInventario
        InventarioEntity resultado = inventarioService.updateInventario(inventario);

        //Se verifica que cumpla con los datos de prueba
        assertNotNull(resultado.getFechaUltimaModificacion());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void getInventarioById_existente_deberiaRetornarInventario() {
        //Se le asigna id 1 al inventario
        inventario.setId(1L);

        //Se coloca lo que debe retornar cuando se realiza la accion de buscar por id
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        //Se realiza la accion de utilizar getInventarioById
        InventarioEntity resultado = inventarioService.getInventarioById(1L);

        //Se verifica que cumpla con los datos de prueba
        assertEquals(1L, resultado.getId());
        verify(inventarioRepository).findById(1L);
    }

    @Test
    void getInventarioById_noExistente_deberiaLanzarExcepcion() {
        //Se colocar lo que debe retornar cuando se realiza la accion de buscar por id
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        //Se verifica que cumpla con los datos de prueba
        assertThrows(EntityNotFoundException.class, () -> inventarioService.getInventarioById(99L));
        verify(inventarioRepository).findById(99L);
    }
}