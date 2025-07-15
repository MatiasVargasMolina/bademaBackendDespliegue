package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.OrdenCompraDocument;
import usach.pingeso.badema.repositories.mongodb.OrdenCompraArchivoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenCompraArchivoServiceTest {

    @Mock
    private OrdenCompraArchivoRepository ordenCompraRepository;

    @Mock
    private PathResolverService pathResolverService;

    @InjectMocks
    private OrdenCompraArchivoService ordenCompraArchivoService;

    @Mock
    private MultipartFile multipartFile;

    @Captor
    private ArgumentCaptor<OrdenCompraDocument> captor;

    @Test
    void getArchivosByIdOrdenCompra_deberiaRetornarLista() {
        List<OrdenCompraDocument> lista = List.of(new OrdenCompraDocument());
        when(ordenCompraRepository.findByIdOrdenCompra(3L)).thenReturn(lista);

        List<OrdenCompraDocument> resultado = ordenCompraArchivoService.getArchivosByIdOrdenCompra(3L);

        assertEquals(1, resultado.size());
        verify(ordenCompraRepository).findByIdOrdenCompra(3L);
    }

    @Test
    void guardarArchivo_deberiaGuardarConPathGenerado() throws IOException {
        long idOrdenCompra = 12L;
        when(pathResolverService.resolvePathForOrdenCompra(idOrdenCompra)).thenReturn("obra_1/ordencompra_12");

        OrdenCompraDocument esperado = new OrdenCompraDocument();
        esperado.setIdOrdenCompra(idOrdenCompra);

        when(ordenCompraRepository.save(any(OrdenCompraDocument.class))).thenReturn(esperado);

        OrdenCompraDocument resultado = ordenCompraArchivoService.guardarArchivo(multipartFile, idOrdenCompra);

        assertNotNull(resultado);
        assertEquals(idOrdenCompra, resultado.getIdOrdenCompra());
        verify(pathResolverService).resolvePathForOrdenCompra(idOrdenCompra);
        verify(ordenCompraRepository).save(any(OrdenCompraDocument.class));
    }

    @Test
    void guardarArchivo_conPathYIdCorrecto() throws IOException {
        long idOrdenCompra = 12L;
        when(pathResolverService.resolvePathForOrdenCompra(idOrdenCompra)).thenReturn("obra_1/ordencompra_12");

        ordenCompraArchivoService.guardarArchivo(multipartFile, idOrdenCompra);

        verify(pathResolverService).resolvePathForOrdenCompra(idOrdenCompra);
        verify(ordenCompraRepository).save(captor.capture());

        OrdenCompraDocument capturado = captor.getValue();
        assertEquals(idOrdenCompra, capturado.getIdOrdenCompra());
    }
}