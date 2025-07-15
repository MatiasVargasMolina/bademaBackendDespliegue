package usach.pingeso.badema.services.postgresql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import usach.pingeso.badema.documents.EspecificacionMaterialDocument;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;
import usach.pingeso.badema.dtos.adquisiciones.MaterialConProveedoresDTO;
import usach.pingeso.badema.dtos.adquisiciones.MaterialProveedorPedidoDTO;
import usach.pingeso.badema.dtos.adquisiciones.PedidoListAdqDTO;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorMaterialDetalleDTO;
import usach.pingeso.badema.entities.*;
import usach.pingeso.badema.services.PedidosAdqService;
import usach.pingeso.badema.services.mongodb.EspecificacionesMaterialService;
import usach.pingeso.badema.services.mongodb.NuevasEspecificacionesService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManejarAdquisicionesServiceTest {

    @Mock private PedidoService pedidoService;
    @Mock private PedidosAdqService pedidosAdqService;
    @Mock private EspecificacionesMaterialService especificacionesMaterialService;
    @Mock private ProveedorMaterialService proveedorMaterialService;
    @Mock private NuevasEspecificacionesService nuevasEspecificacionesService;

    @InjectMocks private ManejarAdquisicionesService manejarAdquisicionesService;

    private PedidoEntity pedido;
    private MaterialEntity material;
    private DetallePedidoEntity detalle;
    private ProveedorMaterialEntity proveedorMaterial;
    private ProveedorEntity proveedor;

    @BeforeEach
    void setUp() {
        //Se crea un material con datos de prueba
        material = new MaterialEntity();
        material.setId(1L);
        material.setNombre("Cemento");

        //Se crea un proveedor con datos de prueba
        proveedor = new ProveedorEntity();
        proveedor.setId(10L);
        proveedor.setNombreProveedor("Proveedor S.A.");

        //Se crea una paridad proveedor-material con datos de prueba
        proveedorMaterial = new ProveedorMaterialEntity();
        proveedorMaterial.setId(100L);
        proveedorMaterial.setMaterial(material);
        proveedorMaterial.setProveedor(proveedor);
        proveedorMaterial.setPrecio(2000);
        proveedorMaterial.setComentarios("Stock limitado");

        //Se crea un detalle de un pedido con datos de prueba
        detalle = new DetallePedidoEntity();
        detalle.setId(20L);
        detalle.setMaterial(material);
        detalle.setCantidad(15);

        //Se crea un pedido con datos de prueba
        pedido = new PedidoEntity();
        pedido.setId(5L);
        pedido.setDetallesPedido(List.of(detalle));
        detalle.setPedido(pedido);
    }

    @Test
    void obtenerMaterialDePedido_deberiaRetornarDTOCompleto() {
        //Se crean los documentos de la especificaciones del material y las especificaciones que provee el proveedor
        EspecificacionMaterialDocument especificacion = new EspecificacionMaterialDocument();
        NuevasEspecificacionesDocument nuevasEspecs = new NuevasEspecificacionesDocument();

        //Se coloca lo que debe retornar al utilizar los metodos
        when(pedidoService.getPedidoById(5L)).thenReturn(pedido);
        when(especificacionesMaterialService.getEspecificacionesByMaterialId(1L)).thenReturn(especificacion);
        when(proveedorMaterialService.getProveedoresPorMaterialId(1L)).thenReturn(List.of(proveedorMaterial));
        when(nuevasEspecificacionesService.getNuevasEspecificacionesByProveedorMaterialId(100L)).thenReturn(nuevasEspecs);

        //Se utiliza obtenerMaterialDePedido
        MaterialConProveedoresDTO dto = manejarAdquisicionesService.obtenerMaterialDePedido(5L, 1L);

        //Se verifican los datos de prueba
        assertEquals(1L, dto.getIdMaterial());
        assertEquals("Cemento", dto.getNombreMaterial());
        assertEquals(15, dto.getCantidad());
        assertEquals(1, dto.getProveedores().size());
        assertEquals("Proveedor S.A.", dto.getProveedores().get(0).getNombreProveedor());
    }

    @Test
    void obtenerPedidosPorObra_deberiaRetornarListaDePedidos() {
        //Se ceran dos pedidos con datos de prueba
        PedidoListAdqDTO pedido1 = new PedidoListAdqDTO();
        pedido1.setId(1L);
        pedido1.setNombre("Pedido 1");

        PedidoListAdqDTO pedido2 = new PedidoListAdqDTO();
        pedido2.setId(2L);
        pedido2.setNombre("Pedido 2");

        //Se guardan en una lista de pedidos
        List<PedidoListAdqDTO> listaMock = List.of(pedido1, pedido2);

        //Se coloca lo que debe retornar al utilizar getListPedidosAdqByObraId
        when(pedidosAdqService.getListPedidosAdqByObraId(123L)).thenReturn(listaMock);

        //Se utiliza obtenerPedidosPorObra
        List<PedidoListAdqDTO> resultado = manejarAdquisicionesService.obtenerPedidosPorObra(123L);

        //Se verifican los datos de prueba
        assertEquals(2, resultado.size());
        assertEquals("Pedido 1", resultado.get(0).getNombre());
        assertEquals("Pedido 2", resultado.get(1).getNombre());
        verify(pedidosAdqService).getListPedidosAdqByObraId(123L);
    }


    @Test
    void getMaterialesProveedorPorObra_deberiaRetornarMaterialesRelacionados() {
        //Se generan los datos de prueba
        material.setParidadProveedor(List.of(proveedorMaterial));
        pedido.setDetallesPedido(List.of(detalle));

        //Se coloca lo que debe retornar al utilizar getPedidosEntityConMaterialesByObraId
        when(pedidoService.getPedidosEntityConMaterialesByObraId(99L)).thenReturn(List.of(pedido));

        //Se utiliza getMaterialesProveedorPorObra
        List<MaterialProveedorPedidoDTO> resultado = manejarAdquisicionesService.getMaterialesProveedorPorObra(99L, 10L);

        //Se verifican los datos de prueba
        assertEquals(1, resultado.size());
        assertEquals("Cemento", resultado.get(0).getNombreMaterial());
        assertEquals(5L, resultado.get(0).getIdPedido());
        assertEquals(15, resultado.get(0).getCantidadRequerida());
    }

    @Test
    void getDetalleProveedorMaterial_deberiaRetornarDTO() {
        //Se crean las nuevas especificaciones con datos de prueba
        NuevasEspecificacionesDocument especs = new NuevasEspecificacionesDocument();

        //Se coloca lo que deben retornar al utilizar los metodos
        when(proveedorMaterialService.getByProveedorAndMaterial(10L, 1L)).thenReturn(proveedorMaterial);
        when(nuevasEspecificacionesService.getNuevasEspecificacionesByProveedorMaterialId(100L)).thenReturn(especs);

        //Se utiliza getDetalleProveedorMaterial
        ProveedorMaterialDetalleDTO dto = manejarAdquisicionesService.getDetalleProveedorMaterial(10L, 1L);

        //Se verifican los datos de prueba
        assertEquals(1L, dto.getIdMaterial());
        assertEquals("Cemento", dto.getNombreMaterial());
        assertEquals("Proveedor S.A.", dto.getNombreProveedor());
        assertEquals(2000, dto.getPrecio());
    }
}

