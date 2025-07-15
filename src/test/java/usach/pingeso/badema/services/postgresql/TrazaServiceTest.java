package usach.pingeso.badema.services.postgresql;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.seguimiento.MaterialConOrdenDTO;
import usach.pingeso.badema.dtos.seguimiento.OrdenCompraTrazaDTO;
import usach.pingeso.badema.repositories.postgresql.DetalleOrdenCompraRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrazaServiceTest {

    @Mock
    private MaterialService materialService;

    @Mock
    private DetalleOrdenCompraRepository detalleOrdenCompraRepository;

    @InjectMocks
    private TrazaService trazaService;

    @Test
    void getMaterialesConOrdenes_deberiaRetornarListaCorrecta() {
        //Materiales con diferentes datos de prueba
        List<MaterialConOrdenDTO> mockLista = List.of(
                new MaterialConOrdenDTO(1L, "Arena", 3L),
                new MaterialConOrdenDTO(2L, "Cemento", 2L)
        );

        when(materialService.getMaterialesConOrdenes(10L)).thenReturn(mockLista);

        //Se obtiene la lista de materiales por la obra 10
        List<MaterialConOrdenDTO> resultado = trazaService.getMaterialesConOrdenes(10L);

        //Se revisa si es que cumple o aciertan los datos
        assertEquals(2, resultado.size());
        assertEquals("Arena", resultado.get(0).getNombreMaterial());
        assertEquals(3L, resultado.get(0).getCantidadOrdenesAsociadas());
        assertEquals("Cemento", resultado.get(1).getNombreMaterial());
        assertEquals(2L, resultado.get(1).getCantidadOrdenesAsociadas());

        verify(materialService).getMaterialesConOrdenes(10L);
    }

    @Test
    void getOrdenesByMaterial_deberiaAsignarEstadosCorrectos() {
        //Ordenes con diferentes datos de prueba
        OrdenCompraTrazaDTO orden1 = new OrdenCompraTrazaDTO(
                1L, "Orden de compra #1", "Proveedor A", "Dirección 123",
                10, 1000, 0L, 0, LocalDate.now(),
                LocalDate.now().plusDays(5)
        );
        OrdenCompraTrazaDTO orden2 = new OrdenCompraTrazaDTO(
                2L, "Orden de compra #2", "Proveedor B", "Dirección 456",
                20, 2000, 10L, 5, LocalDate.now(),
                LocalDate.now().plusDays(7)
        );
        OrdenCompraTrazaDTO orden3 = new OrdenCompraTrazaDTO(
                3L, "Orden de compra #3", "Proveedor C", "Dirección 789",
                30, 1500, 30L, 15, LocalDate.now(),
                LocalDate.now().plusDays(3)
        );


        when(detalleOrdenCompraRepository.getOrdenesByMaterial(1L, 2L))
                .thenReturn(List.of(orden1, orden2, orden3));

        //Se obtienen las ordenes asociadas al material con id = 2 de la obra 1
        List<OrdenCompraTrazaDTO> resultado = trazaService.getOrdenesByMaterial(1L, 2L);

        //Se revisa si es que cumple o aciertan los datos
        assertEquals("Realizada", resultado.get(0).getEstado());
        assertEquals("Parcialmente entregada", resultado.get(1).getEstado());
        assertEquals("Completada", resultado.get(2).getEstado());

        verify(detalleOrdenCompraRepository).getOrdenesByMaterial(1L, 2L);
    }
}
