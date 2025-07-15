package usach.pingeso.badema.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.pedido.PedidoCreateDTO;
import usach.pingeso.badema.dtos.pedido.PedidoMaterialDTO;
import usach.pingeso.badema.entities.DetallePedidoEntity;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.entities.PedidoEntity;
import usach.pingeso.badema.services.mongodb.EspecificacionesMaterialService;
import usach.pingeso.badema.services.postgresql.DetallePedidoService;
import usach.pingeso.badema.services.postgresql.MaterialService;
import usach.pingeso.badema.services.postgresql.PedidoService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePedidoServiceTest {

    @Mock private PedidoService pedidoService;
    @Mock private DetallePedidoService detallePedidoService;
    @Mock private MaterialService materialService;
    @Mock private EspecificacionesMaterialService especificacionesMaterialService;

    @InjectMocks private CreatePedidoService createPedidoService;

    private PedidoCreateDTO pedidoCreateDTO;

    @BeforeEach
    void setUp() {
        PedidoMaterialDTO materialDTO = new PedidoMaterialDTO();
        materialDTO.setNombreMaterial("Cemento");
        materialDTO.setEspecificaciones(Map.of("tipo", "Portland"));
        pedidoCreateDTO = new PedidoCreateDTO();
        pedidoCreateDTO.setMateriales(List.of(materialDTO));
    }

    @Test
    void savePedidoWithEverything_exito() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);
        when(pedidoService.savePedido(any())).thenReturn(pedido);

        MaterialEntity material = new MaterialEntity();
        material.setId(10L);
        when(materialService.saveMaterial("Cemento")).thenReturn(material);

        when(especificacionesMaterialService.insertarEspecificaciones(eq(10L), any())).thenReturn(true);

        DetallePedidoEntity detalle = new DetallePedidoEntity();
        detalle.setId(100L);
        when(detallePedidoService.saveDetallePedidoWithIdPedidoAndIdMaterial(eq(1L), any(), eq(10L))).thenReturn(detalle);

        var resultado = createPedidoService.savePedidoWithEverything(pedidoCreateDTO);

        assertEquals(pedidoCreateDTO, resultado);
        verify(pedidoService).savePedido(pedidoCreateDTO);
        verify(materialService).saveMaterial("Cemento");
        verify(especificacionesMaterialService).insertarEspecificaciones(eq(10L), any());
        verify(detallePedidoService).saveDetallePedidoWithIdPedidoAndIdMaterial(eq(1L), any(), eq(10L));
    }

    @Test
    void savePedidoWithEverything_fallaAlGuardarMaterial() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);
        when(pedidoService.savePedido(any())).thenReturn(pedido);

        when(materialService.saveMaterial("Cemento")).thenReturn(null);

        var ex = assertThrows(RuntimeException.class, () ->
                createPedidoService.savePedidoWithEverything(pedidoCreateDTO));

        assertTrue(ex.getMessage().contains("No se pudo crear el material"));
    }

    @Test
    void savePedidoWithEverything_fallaAlGuardarEspecificaciones() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);
        when(pedidoService.savePedido(any())).thenReturn(pedido);

        MaterialEntity material = new MaterialEntity();
        material.setId(10L);
        when(materialService.saveMaterial("Cemento")).thenReturn(material);

        when(especificacionesMaterialService.insertarEspecificaciones(eq(10L), any())).thenReturn(false);

        var ex = assertThrows(RuntimeException.class, () ->
                createPedidoService.savePedidoWithEverything(pedidoCreateDTO));

        assertTrue(ex.getMessage().contains("No se pudieron guardar las especificaciones"));
    }

    @Test
    void savePedidoWithEverything_fallaAlGuardarDetalle() {
        PedidoEntity pedido = new PedidoEntity();
        pedido.setId(1L);
        when(pedidoService.savePedido(any())).thenReturn(pedido);

        MaterialEntity material = new MaterialEntity();
        material.setId(10L);
        when(materialService.saveMaterial("Cemento")).thenReturn(material);

        when(especificacionesMaterialService.insertarEspecificaciones(eq(10L), any())).thenReturn(true);

        when(detallePedidoService.saveDetallePedidoWithIdPedidoAndIdMaterial(eq(1L), any(), eq(10L)))
                .thenReturn(null);

        var ex = assertThrows(RuntimeException.class, () ->
                createPedidoService.savePedidoWithEverything(pedidoCreateDTO));

        assertTrue(ex.getMessage().contains("No se pudo crear el detalle del pedido"));
    }

    @Test
    void savePedidoWithEverything_fallaPedidoInicial() {
        when(pedidoService.savePedido(any())).thenReturn(null);

        var ex = assertThrows(RuntimeException.class, () ->
                createPedidoService.savePedidoWithEverything(pedidoCreateDTO));

        assertTrue(ex.getMessage().contains("No se pudo crear el pedido"));
    }
}
