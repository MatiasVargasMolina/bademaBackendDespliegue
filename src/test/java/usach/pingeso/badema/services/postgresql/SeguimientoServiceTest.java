package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;
import usach.pingeso.badema.dtos.seguimiento.ActualizarSeguimientoDetalleDTO;
import usach.pingeso.badema.dtos.seguimiento.OrdenCompraSeguimientoDTO;
import usach.pingeso.badema.dtos.seguimiento.SeguimientoDetalleOrdenDTO;
import usach.pingeso.badema.entities.*;
import usach.pingeso.badema.repositories.postgresql.DetalleOrdenCompraRepository;
import usach.pingeso.badema.services.mongodb.NuevasEspecificacionesService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeguimientoServiceTest {

    @Mock private OrdenCompraService ordenCompraService;
    @Mock private DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    @Mock private RecepcionMaterialService recepcionMaterialService;
    @Mock private InventarioService inventarioService;
    @Mock private NuevasEspecificacionesService nuevasEspecificacionesService;

    @InjectMocks private SeguimientoService seguimientoService;

    private OrdenCompraEntity orden;
    private DetalleOrdenCompraEntity detalle;
    private MaterialEntity material;
    private ProveedorEntity proveedor;
    private ProveedorMaterialEntity paridad;
    private InventarioEntity inventario;
    private UsuarioObraEntity responsable;

    @BeforeEach
    void setUp() {
        //Se crea un material
        material = new MaterialEntity();
        material.setId(1L);
        material.setNombre("Cemento");

        //Se crea un proveedor
        proveedor = new ProveedorEntity();
        proveedor.setNombreProveedor("Proveedor A");

        //Se realiza la paridad entre proveedor y material
        paridad = new ProveedorMaterialEntity();
        paridad.setId(1L);
        paridad.setProveedor(proveedor);
        paridad.setMaterial(material);

        //Se crea un usuario con un rol
        responsable = new UsuarioObraEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        responsable.setUsuario(usuario);
        responsable.setRol("Adquisiciones");

        //Se crea una orden de compra
        orden = new OrdenCompraEntity();
        orden.setId(10L);
        orden.setNumeroOrden("Orden de compra #10");
        orden.setFechaEmision(LocalDate.now());
        orden.setFechaEntrega(LocalDate.now().plusDays(5));
        orden.setEstado(0);
        orden.setResponsable(responsable);

        //Se crea el detalle de la orden de compra
        detalle = new DetalleOrdenCompraEntity();
        detalle.setId(100L);
        detalle.setOrdenCompra(orden);
        detalle.setParidadProveedor(paridad);
        detalle.setCantidad(10);
        detalle.setPrecioUnitario(1000);
        detalle.setObservacion("Urgente");

        //Se realiza union entre el detalle y la orden de compra
        orden.setDetalles(List.of(detalle));
    }

    @Test
    void getOrdenesSeguimiento_deberiaRetornarListaDTO() {
        //Se coloca lo que debe retornar al utilizar getOrdenesEntityByIdObra
        when(ordenCompraService.getOrdenesEntityByIdObra(1L)).thenReturn(List.of(orden));

        //Se utiliza getOrdenesSeguimiento
        List<OrdenCompraSeguimientoDTO> resultado = seguimientoService.getOrdenesSeguimiento(1L);

        //Se revisan los datos de prueba
        assertEquals(1, resultado.size());
        assertEquals("Orden de compra #10", resultado.get(0).getNumeroOrden());
        assertEquals("Adquisiciones", resultado.get(0).getResponsable().split("\\(")[1].replace(")", ""));
    }

    @Test
    void getDetalleOrdenSeguimiento_deberiaRetornarDetalleDTO() {
        //Se crea una recpcion y un inventario
        RecepcionMaterialEntity recepcion = new RecepcionMaterialEntity();
        recepcion.setCantidadRecibida(5);
        recepcion.setIncidencias("Se entregaron 5 unidades");
        recepcion.setFechaRecepcion(LocalDate.now());
        recepcion.setDetalleOrdenCompra(detalle);

        InventarioEntity inventario = new InventarioEntity();
        inventario.setCantidad(5);
        inventario.setCantidadInstalada(2);

        //Se enlazan las recepciones y el inventario al detalle de la orden
        detalle.setInventario(inventario);
        detalle.setRecepciones(List.of(recepcion));

        //Se coloca lo que debe devolver cuando se utilizan los metodos
        when(detalleOrdenCompraRepository.findDetalleByOrdenAndMaterial(10L, 1L)).thenReturn(Optional.of(detalle));
        when(nuevasEspecificacionesService.getNuevasEspecificacionesByProveedorMaterialId(1L)).thenReturn(new NuevasEspecificacionesDocument());

        //Se utiliza getDetalleOrdenSeguimiento
        SeguimientoDetalleOrdenDTO dto = seguimientoService.getDetalleOrdenSeguimiento(10L, 1L);

        //Se verifican los datos de prueba
        assertEquals("Cemento", dto.getNombreMaterial());
        assertEquals("Proveedor A", dto.getNombreProveedor());
        assertEquals(10000, dto.getPrecioTotal());
        assertEquals("Parcialmente entregada", dto.getEstado());
        assertEquals(5, dto.getCantidadEntregada());
        assertEquals(2, dto.getCantidadInstalada());
    }

    @Test
    void actualizarDetalleOrdenSeguimiento_deberiaActualizarEstadoYInventario() {
        //DTO ActualizarSeguimientoDetalleDTO con datos de prueba
        ActualizarSeguimientoDetalleDTO datos = new ActualizarSeguimientoDetalleDTO();
        datos.setCantidadEntregada(10);
        datos.setCantidadInstalada(5);
        datos.setIncidencias("Se entregaron todas las unidades solicitadas");

        //Configurar detalle con la cantidad total esperada
        detalle.setCantidad(10);
        detalle.setRecepciones(new ArrayList<>()); //Recepciones vacias
        detalle.setInventario(null); //Se crea el inventario
        orden.setDetalles(List.of(detalle)); //Se asocia el detalle a la orden

        //Se crea la recepcion total
        RecepcionMaterialEntity recepcionSimulada = new RecepcionMaterialEntity();
        recepcionSimulada.setCantidadRecibida(10);
        detalle.getRecepciones().add(recepcionSimulada);

        //Se crea el inventario nuevo
        InventarioEntity nuevoInventario = new InventarioEntity();
        nuevoInventario.setId(99L);
        nuevoInventario.setCantidad(0);
        nuevoInventario.setCantidadInstalada(0);

        //Se coloca lo que deben retornar los metodos a utilizar
        when(detalleOrdenCompraRepository.findDetalleByOrdenAndMaterial(10L, 1L))
                .thenReturn(Optional.of(detalle));
        when(inventarioService.saveInventario(any())).thenReturn(nuevoInventario);

        //Se utiliza actualizarDetalleOrdenSeguimiento
        seguimientoService.actualizarDetalleOrdenSeguimiento(10L, 1L, datos);

        //Se verifican los datos de prueba
        verify(recepcionMaterialService).saveRecepcion(eq(100L), any());
        verify(inventarioService).updateInventario(argThat(i ->
                i.getCantidad() == 10 && i.getCantidadInstalada() == 5
        ));
        verify(ordenCompraService).updateOrdenCompra(argThat(o ->
                o.getEstado() == 1
        ));
    }

    @Test
    void actualizarDetalleOrdenSeguimiento_noDeberiaActualizarEstadoSiNoSeCompleta() {
        //DTO ActualizarSeguimientoDetalleDTO con entrega parcial
        ActualizarSeguimientoDetalleDTO datos = new ActualizarSeguimientoDetalleDTO();
        datos.setCantidadEntregada(5);
        datos.setCantidadInstalada(2);
        datos.setIncidencias("Se entregan solo 5 unidades");

        //Se configura el detalle
        detalle.setCantidad(10); //
        detalle.setRecepciones(new ArrayList<>());
        detalle.setInventario(null); //Se crea un inventario
        orden.setDetalles(List.of(detalle));

        //Se crea el inventario
        InventarioEntity nuevoInventario = new InventarioEntity();
        nuevoInventario.setCantidad(0);
        nuevoInventario.setCantidadInstalada(0);
        when(detalleOrdenCompraRepository.findDetalleByOrdenAndMaterial(10L, 1L))
                .thenReturn(Optional.of(detalle));
        when(inventarioService.saveInventario(any())).thenReturn(nuevoInventario);

        //Se utiliza actualizarDetalleOrdenSeguimiento
        seguimientoService.actualizarDetalleOrdenSeguimiento(10L, 1L, datos);

        //Se verifican los datos de prueba
        verify(recepcionMaterialService).saveRecepcion(eq(100L), any());
        verify(inventarioService).updateInventario(any());
        verify(ordenCompraService, never()).updateOrdenCompra(any());
    }

    @Test
    void actualizarDetalleOrdenSeguimiento_conInventarioExistente_noCreaNuevoInventario() {
        //DTO ActualizarSeguimientoDetalleDTO
        ActualizarSeguimientoDetalleDTO datos = new ActualizarSeguimientoDetalleDTO();
        datos.setCantidadEntregada(3);
        datos.setCantidadInstalada(1);
        datos.setIncidencias("Entrega inicial");

        //Inventario ya existente
        InventarioEntity inventarioExistente = new InventarioEntity();
        inventarioExistente.setCantidad(2);
        inventarioExistente.setCantidadInstalada(1);

        //Se configura el detalle con inventario existente
        detalle.setInventario(inventarioExistente);
        detalle.setRecepciones(new ArrayList<>());
        detalle.setCantidad(10);
        orden.setDetalles(List.of(detalle));

        //Se coloca lo que debe retornar findDetalleByOrdenAndMaterial
        when(detalleOrdenCompraRepository.findDetalleByOrdenAndMaterial(10L, 1L))
                .thenReturn(Optional.of(detalle));

        //Se utiliza actualizarDetalleOrdenSeguimiento
        seguimientoService.actualizarDetalleOrdenSeguimiento(10L, 1L, datos);

        //Se verifican los datos de prueba
        verify(inventarioService, never()).saveInventario(any());
        verify(inventarioService).updateInventario(argThat(i ->
                i.getCantidad() == 5 && i.getCantidadInstalada() == 2
        ));
        verify(ordenCompraService, never()).updateOrdenCompra(any());
    }


    @Test
    void getDetalleOrdenSeguimiento_deberiaLanzarExcepcionSiNoExiste() {
        //Se coloca lo que debe retornar findDetalleByOrdenAndMaterial
        when(detalleOrdenCompraRepository.findDetalleByOrdenAndMaterial(99L, 99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                seguimientoService.getDetalleOrdenSeguimiento(99L, 99L));
    }
}
