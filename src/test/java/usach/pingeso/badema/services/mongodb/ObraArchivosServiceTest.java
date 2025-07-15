package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.ObraDocument;
import usach.pingeso.badema.repositories.mongodb.ObraArchivosRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObraArchivosServiceTest {

    @Mock
    private ObraArchivosRepository obraArchivosRepository;

    @InjectMocks
    private ObraArchivosService obraArchivosService;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setup() {
        // Para evitar NullPointer
        lenient().when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
    }

    @Test
    void guardarArchivo_deberiaGuardarYRetornarDocumento() throws IOException {
        // Arrange
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        doAnswer(invocation -> {
            File f = invocation.getArgument(0);
            assertTrue(f.getPath().contains("obra_5"));
            return null;
        }).when(multipartFile).transferTo(any(File.class));

        ObraDocument saved = new ObraDocument();
        saved.setId("1");
        saved.setIdObra(5L);
        when(obraArchivosRepository.save(any(ObraDocument.class))).thenReturn(saved);

        // Act
        ObraDocument result = obraArchivosService.guardarArchivo(multipartFile, 5L);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals(5L, result.getIdObra());
        verify(obraArchivosRepository).save(any(ObraDocument.class));
    }

    @Test
    void obtenerArchivoPorId_deberiaRetornarDocumento() {
        ObraDocument doc = new ObraDocument("1", 10L);
        when(obraArchivosRepository.findById("1")).thenReturn(Optional.of(doc));

        ObraDocument result = obraArchivosService.obtenerArchivoPorId("1");

        assertEquals("1", result.getId());
        assertEquals(10L, result.getIdObra());
    }

    @Test
    void obtenerArchivoPorId_lanzaExcepcionSiNoExiste() {
        when(obraArchivosRepository.findById("nope")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                obraArchivosService.obtenerArchivoPorId("nope")
        );
        assertTrue(ex.getMessage().contains("Archivo no encontrado"));
    }

    @Test
    void obtenerArchivoComoResource_lanzaExcepcionSiFileNoExiste() {
        ObraDocument doc = new ObraDocument();
        doc.setRutaArchivo("ruta_inexistente.pdf");
        when(obraArchivosRepository.findById("id")).thenReturn(Optional.of(doc));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                obraArchivosService.obtenerArchivoComoResource("id")
        );
        assertTrue(ex.getMessage().contains("Archivo físico no encontrado"));
    }

    @Test
    void getArchivosByIdObra_deberiaLlamarRepoYRetornarLista() {
        List<ObraDocument> lista = List.of(new ObraDocument("1", 5L));
        when(obraArchivosRepository.findByIdObra(5L)).thenReturn(lista);

        List<ObraDocument> resultado = obraArchivosService.getArchivosByIdObra(5L);

        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.getFirst().getIdObra());
        verify(obraArchivosRepository).findByIdObra(5L);
    }
}

