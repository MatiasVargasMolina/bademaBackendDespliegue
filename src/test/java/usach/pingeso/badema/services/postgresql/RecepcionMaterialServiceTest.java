package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.entities.DetalleOrdenCompraEntity;
import usach.pingeso.badema.entities.InventarioEntity;
import usach.pingeso.badema.entities.RecepcionMaterialEntity;
import usach.pingeso.badema.repositories.postgresql.DetalleOrdenCompraRepository;
import usach.pingeso.badema.repositories.postgresql.RecepcionMaterialRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecepcionMaterialServiceTest {

    @Mock
    private RecepcionMaterialRepository recepcionRepository;

    @Mock
    private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private RecepcionMaterialService recepcionMaterialService;

    private DetalleOrdenCompraEntity detalleConInventario;
    private DetalleOrdenCompraEntity detalleSinInventario;

    @BeforeEach
    void setUp() {
        //Se crea un inventario para realizar pruebas
        InventarioEntity inventario = new InventarioEntity();
        inventario.setCantidad(10);
        inventario.setCantidadInstalada(0);

        //Se crea un detalle de orden con un inventario ya existente
        detalleConInventario = new DetalleOrdenCompraEntity();
        detalleConInventario.setId(1L);
        detalleConInventario.setInventario(inventario);

        //Se crea un detalle de orden sin inventario
        detalleSinInventario = new DetalleOrdenCompraEntity();
        detalleSinInventario.setId(2L);
        detalleSinInventario.setInventario(null);
    }

    @Test
    void saveRecepcion_conInventarioExistente_deberiaActualizarInventario() {
        //Se crea la recepcion del material
        RecepcionMaterialEntity recepcion = new RecepcionMaterialEntity();
        recepcion.setFechaRecepcion(LocalDate.now());
        recepcion.setCantidadRecibida(5);
        recepcion.setIncidencias("Llegaron 5 unidades en buen estado");

        //Se asigna lo que debe retornar cuando se realice cierta accion
        when(detalleOrdenCompraRepository.findById(1L)).thenReturn(Optional.of(detalleConInventario));
        when(recepcionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        //Se realiza la accion de llamar al metodo saveRecepcion
        RecepcionMaterialEntity resultado = recepcionMaterialService.saveRecepcion(1L, recepcion);

        //Se revisa que los datos concuerden con los datos de prueba
        assertEquals(5, resultado.getCantidadRecibida());
        assertEquals("Llegaron 5 unidades en buen estado", resultado.getIncidencias());
        verify(inventarioService).updateInventario(argThat(inv -> inv.getCantidad() == 15));
        verify(recepcionRepository).save(recepcion);
    }

    @Test
    void saveRecepcion_sinInventario_deberiaCrearYAsociarInventario() {
        //Se crea la recepcion del material
        RecepcionMaterialEntity recepcion = new RecepcionMaterialEntity();
        recepcion.setFechaRecepcion(LocalDate.now());
        recepcion.setCantidadRecibida(7);
        recepcion.setIncidencias("Llegaron 7 unidades en buen estado");

        //Se crea el nuevo inventario para realizar la verificacion
        InventarioEntity nuevoInventario = new InventarioEntity();
        nuevoInventario.setCantidad(0);
        nuevoInventario.setCantidadInstalada(0);

        //Se asigna lo que debe retornar cuando se realice cierta accion
        when(detalleOrdenCompraRepository.findById(2L)).thenReturn(Optional.of(detalleSinInventario));
        when(inventarioService.saveInventario(any())).thenReturn(nuevoInventario);
        when(recepcionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        //Se realiza la accion de llamar al metodo saveRecepcion
        RecepcionMaterialEntity resultado = recepcionMaterialService.saveRecepcion(2L, recepcion);

        //Se revisa que los datos concuerden con los datos de prueba
        assertEquals(7, resultado.getCantidadRecibida());
        assertEquals("Llegaron 7 unidades en buen estado", resultado.getIncidencias());
        verify(inventarioService).saveInventario(any());
        verify(detalleOrdenCompraRepository).save(detalleSinInventario);
        verify(inventarioService).updateInventario(argThat(inv -> inv.getCantidad() == 7));
    }

    @Test
    void saveRecepcion_detalleNoEncontrado_deberiaLanzarExcepcion() {
        //Se asigna lo que debe retornar cuando se realice cierta accion
        when(detalleOrdenCompraRepository.findById(99L)).thenReturn(Optional.empty());

        //Se crea la recepcion del material
        RecepcionMaterialEntity recepcion = new RecepcionMaterialEntity();
        recepcion.setFechaRecepcion(LocalDate.now());
        recepcion.setCantidadRecibida(10);
        recepcion.setIncidencias("Llegaron 10 unidades en buen estado");

        //Se revisa que retorne lo correcto
        assertThrows(EntityNotFoundException.class, () ->
                recepcionMaterialService.saveRecepcion(99L, recepcion));
    }

    @Test
    void getRecepcionesByDetalleOrden_deberiaRetornarSoloLasCoincidentes() {
        //Se crea un detalle con ID 1L
        DetalleOrdenCompraEntity detalle1 = new DetalleOrdenCompraEntity();
        detalle1.setId(1L);

        //Se crea otro detalle con ID 2L
        DetalleOrdenCompraEntity detalle2 = new DetalleOrdenCompraEntity();
        detalle2.setId(2L);

        //Se crean 3 recepciones, 2 para el detalle 1 y 1 para el 2
        RecepcionMaterialEntity r1 = new RecepcionMaterialEntity();
        r1.setCantidadRecibida(3);
        r1.setIncidencias("Llegaron 3 unidades en buen estado");
        r1.setDetalleOrdenCompra(detalle1);

        RecepcionMaterialEntity r2 = new RecepcionMaterialEntity();
        r2.setCantidadRecibida(10);
        r2.setIncidencias("Llegaron 10 unidades en buen estado");
        r2.setDetalleOrdenCompra(detalle1);

        RecepcionMaterialEntity r3 = new RecepcionMaterialEntity();
        r3.setCantidadRecibida(5);
        r3.setIncidencias("Llegaron 5 unidades en buen estado");
        r3.setDetalleOrdenCompra(detalle2);

        //Se asigna lo que debe retornar cuando se realice cierta accion
        when(recepcionRepository.findAll()).thenReturn(List.of(r1, r2, r3));

        //Se realiza la accion de llamar al metodo getRecepcionesByDetalleOrden
        List<RecepcionMaterialEntity> resultado = recepcionMaterialService.getRecepcionesByDetalleOrden(1L);

        //Verificamos que solo se devuelven las recepciones del detalle con ID 1L
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(r -> r.getDetalleOrdenCompra().getId().equals(1L)));

        verify(recepcionRepository).findAll();
    }

}
