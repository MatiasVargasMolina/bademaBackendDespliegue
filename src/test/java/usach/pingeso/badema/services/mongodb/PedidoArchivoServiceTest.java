package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.PedidoDocument;
import usach.pingeso.badema.entities.PedidoEntity;
import usach.pingeso.badema.repositories.mongodb.PedidoArchivosRepository;
import usach.pingeso.badema.repositories.postgresql.PedidoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoArchivoServiceTest {

    @Mock
    private PedidoArchivosRepository pedidoArchivosRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PathResolverService pathResolverService;

    @InjectMocks
    private PedidoArchivoService pedidoArchivoService;

    @Mock
    private MultipartFile multipartFile;

    @Captor
    private ArgumentCaptor<PedidoDocument> captor;

    @Test
    void getArchivosByIdPedido_deberiaRetornarLista() {
        List<PedidoDocument> lista = List.of(new PedidoDocument());
        when(pedidoArchivosRepository.findByIdPedido(7L)).thenReturn(lista);

        List<PedidoDocument> resultado = pedidoArchivoService.getArchivosByIdPedido(7L);

        assertEquals(1, resultado.size());
        verify(pedidoArchivosRepository).findByIdPedido(7L);
    }

    @Test
    void guardarArchivo_deberiaGuardarArchivoConPathGenerado() throws IOException {
        // Arrange
        long idPedido = 10L;
        PedidoEntity pedidoEntity = new PedidoEntity();
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(pedidoEntity));
        when(pathResolverService.resolvePathForPedido(idPedido)).thenReturn("obra_1/pedido_10");

        PedidoDocument esperado = new PedidoDocument();
        esperado.setIdPedido(idPedido);
        when(pedidoArchivosRepository.save(any(PedidoDocument.class))).thenReturn(esperado);

        // Act
        PedidoDocument resultado = pedidoArchivoService.guardarArchivo(multipartFile, idPedido);

        // Assert
        assertEquals(idPedido, resultado.getIdPedido());
        verify(pathResolverService).resolvePathForPedido(idPedido);
        verify(pedidoArchivosRepository).save(any(PedidoDocument.class));
    }

    @Test
    void guardarArchivo_lanzaExcepcionSiPedidoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                pedidoArchivoService.guardarArchivo(multipartFile, 99L)
        );

        assertTrue(ex.getMessage().contains("Pedido no encontrado"));
        verify(pedidoRepository).findById(99L);
    }

    @Test
    void guardarArchivo_deberiaGuardarConPathGeneradoYConIdCorrecto() throws IOException {
        long idPedido = 10L;
        when(pedidoRepository.findById(idPedido)).thenReturn(Optional.of(new PedidoEntity()));
        when(pathResolverService.resolvePathForPedido(idPedido)).thenReturn("obra_1/pedido_10");

        pedidoArchivoService.guardarArchivo(multipartFile, idPedido);

        verify(pathResolverService).resolvePathForPedido(idPedido);
        verify(pedidoArchivosRepository).save(captor.capture());

        PedidoDocument capturado = captor.getValue();
        assertEquals(idPedido, capturado.getIdPedido());
    }

    @Test
    void guardarArchivo_lanzaExcepcionSiNoEncuentraPedido() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () ->
                pedidoArchivoService.guardarArchivo(multipartFile, 99L));
        assertTrue(ex.getMessage().contains("Pedido no encontrado"));
    }
}