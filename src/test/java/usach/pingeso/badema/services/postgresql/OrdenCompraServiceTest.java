package usach.pingeso.badema.services.postgresql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorListDTO;
import usach.pingeso.badema.dtos.ordencompra.CrearOrdenCompraDTO;
import usach.pingeso.badema.dtos.ordencompra.DocumentoOrdenCompraDTO;
import usach.pingeso.badema.dtos.ordencompra.ItemOrdenCompraDTO;
import usach.pingeso.badema.dtos.obra.OrdenCompraListObraDTO;
import usach.pingeso.badema.dtos.ordencompra.OrdenCompraDTO;
import usach.pingeso.badema.entities.*;
import usach.pingeso.badema.repositories.postgresql.OrdenCompraRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenCompraServiceTest {

    @Mock
    private OrdenCompraRepository ordenCompraRepository;

    @Mock
    private UsuarioObraService usuarioObraService;

    @Mock
    private ProveedorService proveedorService;

    @Mock
    private ProveedorMaterialService proveedorMaterialService;

    @InjectMocks
    private OrdenCompraService ordenCompraService;

    private CrearOrdenCompraDTO crearOrden;
    private UsuarioObraEntity usuarioObra;
    private ProveedorListDTO proveedor;
    private ProveedorMaterialEntity proveedorMaterial;

    @BeforeEach
    void setUp() {
        //Se crea un item de orden de compra
        ItemOrdenCompraDTO item = new ItemOrdenCompraDTO();
        item.setIdMaterial(1L);
        item.setNombreMaterial("Cemento");
        item.setCantidad(10);
        item.setTotal(5000);
        item.setObservaciones("Urgente");

        crearOrden = new CrearOrdenCompraDTO();
        crearOrden.setIdProveedor(1L);
        crearOrden.setIdResponsable(2L);
        crearOrden.setFechaEntrega(LocalDate.now().plusDays(5));
        crearOrden.setItems(List.of(item));

        usuarioObra = new UsuarioObraEntity();
        usuarioObra.setId(2L);

        proveedor = new ProveedorListDTO();
        proveedor.setId(1L);

        proveedorMaterial = new ProveedorMaterialEntity();
    }

    @Test
    void saveOrden_deberiaGuardarYRetornarDTO() {
        //Se coloca lo que debe retornar al utilizar los servicios
        when(proveedorService.getProveedorById(1L)).thenReturn(proveedor);
        when(usuarioObraService.getUsuarioObraById(2L)).thenReturn(usuarioObra);
        when(proveedorMaterialService.getByProveedorAndMaterial(1L, 1L)).thenReturn(proveedorMaterial);

        //Se crea una orden de compra
        OrdenCompraEntity guardada = new OrdenCompraEntity();
        guardada.setId(100L);
        guardada.setFechaEmision(LocalDate.now());
        guardada.setFechaEntrega(crearOrden.getFechaEntrega());
        guardada.setEstado(0);
        guardada.setResponsable(usuarioObra);
        when(ordenCompraRepository.save(any(OrdenCompraEntity.class))).thenReturn(guardada);

        //Se utiliza saveOrden
        OrdenCompraDTO resultado = ordenCompraService.saveOrden(crearOrden);

        //Se revisan los datos de prueba
        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals("Orden de compra #100", resultado.getNumeroOrden());
        assertEquals(crearOrden.getFechaEntrega(), resultado.getFechaEntrega());
        assertEquals(5000, resultado.getTotal());
        assertEquals(0, resultado.getEstado());
        assertEquals(1L, resultado.getIdProveedor());
        assertEquals(2L, resultado.getIdResponsable());
        assertEquals(1, resultado.getItems().size());

        verify(proveedorService).getProveedorById(1L);
        verify(usuarioObraService).getUsuarioObraById(2L);
        verify(proveedorMaterialService).getByProveedorAndMaterial(1L, 1L);
        verify(ordenCompraRepository, times(2)).save(any(OrdenCompraEntity.class));
    }

    @Test
    void saveOrden_conCantidadCero_deberiaPonerPrecioUnitarioEnCero() {
        //Se crea un item de orden de compra con cantidad ordenada = 0
        ItemOrdenCompraDTO item = new ItemOrdenCompraDTO();
        item.setIdMaterial(1L);
        item.setNombreMaterial("Cemento");
        item.setCantidad(0);
        item.setTotal(5000);
        item.setObservaciones("Sin cantidad");

        //Se crea la orden con ese item
        CrearOrdenCompraDTO orden = new CrearOrdenCompraDTO();
        orden.setIdProveedor(1L);
        orden.setIdResponsable(2L);
        orden.setFechaEntrega(LocalDate.now().plusDays(5));
        orden.setItems(List.of(item));

        //Se coloca lo que debe retornar cuando se utilizan estos metodos
        when(proveedorService.getProveedorById(1L)).thenReturn(proveedor);
        when(usuarioObraService.getUsuarioObraById(2L)).thenReturn(usuarioObra);
        when(proveedorMaterialService.getByProveedorAndMaterial(1L, 1L)).thenReturn(proveedorMaterial);
        when(ordenCompraRepository.save(any(OrdenCompraEntity.class))).thenAnswer(invocation -> {
            OrdenCompraEntity ordenCreada = invocation.getArgument(0);
            ordenCreada.setId(200L);
            return ordenCreada;
        });

        //Se utiliza saveOrden
        OrdenCompraDTO resultado = ordenCompraService.saveOrden(orden);

        //Se revisan los datos de prueba
        assertNotNull(resultado);
        assertEquals(200L, resultado.getId());
        assertEquals("Orden de compra #200", resultado.getNumeroOrden());
        assertEquals(5000, resultado.getTotal());
        assertEquals(0, resultado.getEstado());
        assertEquals(1L, resultado.getIdProveedor());
        assertEquals(2L, resultado.getIdResponsable());
    }

    @Test
    void updateOrdenCompra_deberiaGuardarOrden() {
        //Se crea una orden de compra
        OrdenCompraEntity orden = new OrdenCompraEntity();
        orden.setId(123L);

        //Se utiliza updateOrdenCompra
        ordenCompraService.updateOrdenCompra(orden);

        //Se verifican los datos de prueba
        verify(ordenCompraRepository).save(orden);
    }

    @Test
    void getOrdenesCompraByIdObra_deberiaRetornarListaDTO() {
        //Se crea DTO OrdenCompraListObraDTO
        OrdenCompraListObraDTO dto = new OrdenCompraListObraDTO(1L, "Orden 1", LocalDate.now(), 0);

        //Se coloca lo que debe retornar cuando dse utiliza getOrdenCompraByIdObra
        when(ordenCompraRepository.getOrdenCompraByIdObra(5L)).thenReturn(List.of(dto));

        //Se utiliza getOrdenesCompraByIdObra
        List<OrdenCompraListObraDTO> resultado = ordenCompraService.getOrdenesCompraByIdObra(5L);

        //Se verifican los datos de prueba
        assertEquals(1, resultado.size());
        assertEquals("Orden 1", resultado.get(0).getNumeroOrden());
        verify(ordenCompraRepository).getOrdenCompraByIdObra(5L);
    }

    @Test
    void getOrdenesEntityByIdObra_deberiaRetornarListaEntidades() {
        //Se crea una orden de compra de prueba
        OrdenCompraEntity orden = new OrdenCompraEntity();
        orden.setId(20L);

        //Se coloca lo que debe retornar al utilizar getOrdenesEntityByIdObra
        when(ordenCompraRepository.getOrdenesEntityByIdObra(10L)).thenReturn(List.of(orden));

        //Se utiliza getOrdenesEntityByIdObra
        List<OrdenCompraEntity> resultado = ordenCompraService.getOrdenesEntityByIdObra(10L);

        //Se verifican los datos de prueba
        assertEquals(1, resultado.size());
        assertEquals(20L, resultado.get(0).getId());
        verify(ordenCompraRepository).getOrdenesEntityByIdObra(10L);
    }

    @Test
    void generarDocumentoOrdenCompra_deberiaRetornarDocumentoCorrecto() {
        //Se crea un material con datos de prueba
        MaterialEntity material = new MaterialEntity();
        material.setId(1L);
        material.setNombre("Cemento");

        //Se crea un proveedor con datos de prueba
        ProveedorEntity proveedor = new ProveedorEntity();
        proveedor.setId(10L);
        proveedor.setNombreProveedor("Proveedor Uno");
        proveedor.setRutProveedor("12.345.678-9");
        proveedor.setDireccionProveedor("Av. Ejemplo 123");
        proveedor.setNombreVendedor("Pedro Martínez");
        proveedor.setTelefonoVendedor("987654321");
        proveedor.setEmailVendedor("pedro@martinez.com");
        proveedor.setCondiciones("Pago 30 días");

        //Se crea la paridad proveedor-material
        ProveedorMaterialEntity paridad = new ProveedorMaterialEntity();
        paridad.setProveedor(proveedor);
        paridad.setMaterial(material);

        //Se crea un detalle de orden de compra con datos de prueba
        DetalleOrdenCompraEntity detalle = new DetalleOrdenCompraEntity();
        detalle.setObservacion("Entrega urgente");
        detalle.setCantidad(10);
        detalle.setPrecioUnitario(500);
        detalle.setParidadProveedor(paridad);

        //Se crea una obra con datos de prueba
        ObraEntity obra = new ObraEntity();
        obra.setId(5L);
        obra.setNombre("Obra Central");

        //Se crea un usuario obra (responsable) con datos de prueba
        UsuarioObraEntity usuarioObra = new UsuarioObraEntity();
        usuarioObra.setObra(obra);

        //Se crea una orden de compra con datos de prueba
        OrdenCompraEntity orden = new OrdenCompraEntity();
        orden.setId(100L);
        orden.setFechaEmision(LocalDate.of(2025, 5, 13));
        orden.setResponsable(usuarioObra);
        orden.setDetalles(List.of(detalle));

        //Se coloca lo que debe retornar cuando se utiliza findById
        when(ordenCompraRepository.findById(100L)).thenReturn(Optional.of(orden));

        //Se utiliza generarDocumentoOrdenCompra
        DocumentoOrdenCompraDTO doc = ordenCompraService.generarDocumentoOrdenCompra(100L);

        //Se verifican los datos de prueba
        assertEquals("100 - 2025", doc.getNumeroOrden());
        assertEquals(LocalDate.of(2025, 5, 13), doc.getFechaEmision());
        assertEquals(10L, doc.getIdProveedor());
        assertEquals("Proveedor Uno", doc.getNombreProveedor());
        assertEquals("12.345.678-9", doc.getRutProveedor());
        assertEquals("Av. Ejemplo 123", doc.getDireccionProveedor());
        assertEquals("Pedro Martínez", doc.getNombreVendedor());
        assertEquals("987654321", doc.getTelefonoVendedor());
        assertEquals("pedro@martinez.com", doc.getEmailVendedor());
        assertEquals("Pago 30 días", doc.getCondiciones());
        assertEquals(5L, doc.getIdObra());
        assertEquals("Obra Central", doc.getObraNombre());
        assertEquals(1, doc.getItems().size());
        assertEquals(5000, doc.getTotalNeto());
        assertEquals(950, doc.getIva());
        assertEquals(5950, doc.getTotalGlobal());
    }

    @Test
    void generarDocumentoOrdenCompra_ordenNoEncontrada_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findById
        when(ordenCompraRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenCompraService.generarDocumentoOrdenCompra(999L);
        });

        //Se revisan los datos de prueba
        assertEquals("Orden no encontrada", ex.getMessage());
    }

    @Test
    void generarDocumentoOrdenCompra_sinProveedorEnDetalle_deberiaLanzarExcepcion() {
        //Se crean datos de prueba (detalle de orden de compra sin paridad)
        DetalleOrdenCompraEntity detalle = new DetalleOrdenCompraEntity();
        OrdenCompraEntity orden = new OrdenCompraEntity();
        orden.setId(101L);
        orden.setResponsable(new UsuarioObraEntity());
        orden.setDetalles(List.of(detalle));

        //Se coloca lo que debe retornar al utilizar findById
        when(ordenCompraRepository.findById(101L)).thenReturn(Optional.of(orden));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ordenCompraService.generarDocumentoOrdenCompra(101L);
        });

        //Se verifican los datos de prueba
        assertEquals("Proveedor no encontrado para esta orden.", ex.getMessage());
    }
}