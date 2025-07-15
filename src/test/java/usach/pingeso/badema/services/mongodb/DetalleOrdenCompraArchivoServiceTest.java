package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.DetalleOrdenCompraDocument;
import usach.pingeso.badema.repositories.mongodb.DetalleOrdenCompraArchivoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetalleOrdenCompraArchivoServiceTest {

    @Mock
    private DetalleOrdenCompraArchivoRepository detalleOrdenCompraArchivoRepository;

    @Mock
    private PathResolverService pathResolverService;

    @InjectMocks
    private DetalleOrdenCompraArchivoService detalleOrdenCompraArchivoService;

    @Mock
    private MultipartFile multipartFile;

    @Captor
    private ArgumentCaptor<DetalleOrdenCompraDocument> captor;

    @Test
    void getArchivosByIdDetalleOrdenCompra_deberiaRetornarLista() {
        when(detalleOrdenCompraArchivoRepository.findByIdDetalleOrdenCompra(5L))
                .thenReturn(List.of(new DetalleOrdenCompraDocument()));

        var resultado = detalleOrdenCompraArchivoService.getArchivosByIdDetalleOrdenCompra(5L);

        assertEquals(1, resultado.size());
        verify(detalleOrdenCompraArchivoRepository).findByIdDetalleOrdenCompra(5L);
    }

    @Test
    void guardarArchivo_deberiaGenerarPathYGuardarConIdCorrecto() throws IOException {
        long idDetalle = 22L;
        when(pathResolverService.resolvePathForDetalle(idDetalle)).thenReturn("obra_1/ordencompra_2/detalle_22");

        detalleOrdenCompraArchivoService.guardarArchivo(multipartFile, idDetalle);

        verify(pathResolverService).resolvePathForDetalle(idDetalle);
        verify(detalleOrdenCompraArchivoRepository).save(captor.capture());

        DetalleOrdenCompraDocument capturado = captor.getValue();
        assertEquals(idDetalle, capturado.getIdDetalleOrdenCompra());
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 50L, 99L})
    void guardarArchivo_parametrizado_variosIds(long idDetalle) throws IOException {
        when(pathResolverService.resolvePathForDetalle(idDetalle))
                .thenReturn("obra_x/ordencompra_y/detalle_" + idDetalle);

        detalleOrdenCompraArchivoService.guardarArchivo(multipartFile, idDetalle);

        verify(pathResolverService).resolvePathForDetalle(idDetalle);
        verify(detalleOrdenCompraArchivoRepository).save(captor.capture());

        DetalleOrdenCompraDocument capturado = captor.getValue();
        assertEquals(idDetalle, capturado.getIdDetalleOrdenCompra());
    }
}
