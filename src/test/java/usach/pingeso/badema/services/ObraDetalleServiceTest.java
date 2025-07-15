package usach.pingeso.badema.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.documents.HitosObraDocument;
import usach.pingeso.badema.dtos.obra.*;
import usach.pingeso.badema.services.mongodb.HitosObraService;
import usach.pingeso.badema.services.postgresql.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObraDetalleServiceTest {

    @Mock private ObraService obraService;
    @Mock private HitosObraService hitosObraService;
    @Mock private PedidoService pedidoService;
    @Mock private UsuarioObraService usuarioObraService;
    @Mock private SubContratoService subContratoService;
    @Mock private AsociadoService asociadoService;
    @Mock private OrdenCompraService ordenCompraService;

    @InjectMocks private ObraDetalleService obraDetalleService;

    private Long idObra;

    @BeforeEach
    void setUp() {
        idObra = 1L;
    }

    @Test
    void obtenerDetalle_conObraExistente_devuelveDTOCompleto() {
        // Mock obra
        ObraListDTO obraMini = new ObraListDTO();
        obraMini.setId(idObra);
        obraMini.setNombre("Obra Piloto");
        obraMini.setEmpresaContratista("Constructora XYZ");
        obraMini.setEsPublico(true);
        obraMini.setEstado("En ejecución");
        obraMini.setMetrosCuadrados(1000);
        obraMini.setFechaInicio(LocalDate.of(2023,1,1));
        obraMini.setFechaTermino(LocalDate.of(2024,1,1));
        when(obraService.getObraById(idObra)).thenReturn(obraMini);

        // Mock hitos
        HitosObraDocument hitos = new HitosObraDocument();
        when(hitosObraService.findHitosByObraId(idObra)).thenReturn(hitos);

        // Mock pedidos
        when(pedidoService.getPedidosATrabajar(idObra)).thenReturn(List.of(new PedidosATrabajarListDTO()));

        // Mock ordenes compra
        when(ordenCompraService.getOrdenesCompraByIdObra(idObra)).thenReturn(List.of(new OrdenCompraListObraDTO()));

        // Mock administrativos
        when(usuarioObraService.getUsuarioListDTOByObraId(idObra)).thenReturn(List.of(new UsuarioObraListDTO()));

        // Mock subcontratos
        when(subContratoService.getSubContratosByObraId(idObra)).thenReturn(List.of(new SubContratoListDTO()));

        // Mock asociados
        when(asociadoService.getAsociadosListDTOByIdObra(idObra)).thenReturn(List.of(new AsociadosListDTO()));

        // Ejecutar
        ObraDetalleDTO resultado = obraDetalleService.obtenerDetalle(idObra);

        // Validar
        assertNotNull(resultado);
        assertEquals(idObra, resultado.getId());
        assertEquals("Obra Piloto", resultado.getNombre());
        assertEquals("Constructora XYZ", resultado.getEmpresaContratista());
        assertTrue(resultado.isEsPublico());
        assertEquals("En ejecución", resultado.getEstado());
        assertEquals(1000, resultado.getMetrosCuadrados());

        assertNotNull(resultado.getHitos());
        assertEquals(1, resultado.getPedidosATrabajar().size());
        assertEquals(1, resultado.getOrdenesCompra().size());
        assertEquals(1, resultado.getAdministrativos().size());
        assertEquals(1, resultado.getSubContratados().size());
        assertEquals(1, resultado.getAsociados().size());

        // Verify que llamaste a los servicios dependientes
        verify(obraService).getObraById(idObra);
        verify(hitosObraService).findHitosByObraId(idObra);
        verify(pedidoService).getPedidosATrabajar(idObra);
        verify(ordenCompraService).getOrdenesCompraByIdObra(idObra);
        verify(usuarioObraService).getUsuarioListDTOByObraId(idObra);
        verify(subContratoService).getSubContratosByObraId(idObra);
        verify(asociadoService).getAsociadosListDTOByIdObra(idObra);
    }

    @Test
    void obtenerDetalle_conObraNoExistente_devuelveNull() {
        when(obraService.getObraById(idObra)).thenReturn(null);

        ObraDetalleDTO resultado = obraDetalleService.obtenerDetalle(idObra);

        assertNull(resultado);

        // Debe consultar solo obraService y nada más
        verify(obraService).getObraById(idObra);
        verifyNoInteractions(hitosObraService, pedidoService, ordenCompraService,
                usuarioObraService, subContratoService, asociadoService);
    }
}

