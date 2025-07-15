package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import usach.pingeso.badema.documents.HitosObraDocument;
import usach.pingeso.badema.dtos.obra.HitoDTO;
import usach.pingeso.badema.dtos.obra.ObraCreateDTO;
import usach.pingeso.badema.repositories.mongodb.HitosObraRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HitosObraServiceTest {

    private AutoCloseable closeable;

    @Mock
    private HitosObraRepository hitosObraRepository;

    @InjectMocks
    private HitosObraService hitosObraService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void insertarHitos_deberiaGuardarDocumento() {
        // Arrange
        ObraCreateDTO obraDTO = new ObraCreateDTO();
        HitoDTO hitoDTO = new HitoDTO();
        Map<String, LocalDate> hitosMap = new HashMap<>();
        hitosMap.put("Inicio", LocalDate.now());
        hitoDTO.setHitos(hitosMap);
        obraDTO.setHitos(hitoDTO);

        HitosObraDocument esperado = new HitosObraDocument();
        esperado.setIdObra(1L);
        esperado.setHitos(hitosMap);

        when(hitosObraRepository.save(any(HitosObraDocument.class)))
                .thenReturn(esperado);

        // Act
        HitosObraDocument resultado = hitosObraService.insertarHitos(1L, obraDTO);

        // Assert
        assertEquals(1L, resultado.getIdObra());
        assertEquals(hitosMap, resultado.getHitos());
        verify(hitosObraRepository).save(any(HitosObraDocument.class));
    }

    @Test
    void findHitosByObraId_deberiaRetornarDocumento() {
        // Arrange
        HitosObraDocument doc = new HitosObraDocument();
        doc.setIdObra(5L);

        when(hitosObraRepository.findByIdObra(5L)).thenReturn(doc);

        // Act
        HitosObraDocument resultado = hitosObraService.findHitosByObraId(5L);

        // Assert
        assertNotNull(resultado);
        assertEquals(5L, resultado.getIdObra());
        verify(hitosObraRepository).findByIdObra(5L);
    }

    @Test
    void agregarOActualizarHito_deberiaAgregarHitoYGuardar() {
        // Arrange
        String idHito = "abc123";
        HitosObraDocument doc = new HitosObraDocument();
        doc.setHitos(new HashMap<>());

        when(hitosObraRepository.findById(idHito)).thenReturn(Optional.of(doc));

        // Act
        hitosObraService.agregarOActualizarHito(idHito, "NuevaEtapa", LocalDate.now());

        // Assert
        assertTrue(doc.getHitos().containsKey("NuevaEtapa"));
        verify(hitosObraRepository).save(doc);
    }

    @Test
    void agregarOActualizarHito_lanzaExcepcionSiNoExiste() {
        // Arrange
        when(hitosObraRepository.findById("noexiste")).thenReturn(Optional.empty());

        // Act & Assert
        Exception ex = assertThrows(NoSuchElementException.class, () ->
                hitosObraService.agregarOActualizarHito("noexiste", "Etapa", LocalDate.now())
        );

        assertTrue(ex.getMessage().contains("Hito no encontrado"));
    }

    @Test
    void eliminarHito_eliminaHitoExistente() {
        // Arrange
        String idHito = "hito123";
        Map<String, LocalDate> hitos = new HashMap<>();
        hitos.put("Etapa1", LocalDate.now());

        HitosObraDocument doc = new HitosObraDocument();
        doc.setHitos(hitos);

        when(hitosObraRepository.findById(idHito)).thenReturn(Optional.of(doc));

        // Act
        hitosObraService.eliminarHito(idHito, "Etapa1");

        // Assert
        assertFalse(doc.getHitos().containsKey("Etapa1"));
        verify(hitosObraRepository).save(doc);
    }

    @Test
    void eliminarHito_lanzaExcepcionSiNoExisteHito() {
        // Arrange
        HitosObraDocument doc = new HitosObraDocument();
        doc.setHitos(new HashMap<>());

        when(hitosObraRepository.findById("id")).thenReturn(Optional.of(doc));

        // Act & Assert
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                hitosObraService.eliminarHito("id", "Inexistente")
        );

        assertTrue(ex.getMessage().contains("No existe el hito"));
    }

    @Test
    void eliminarHito_lanzaExcepcionSiNoEncuentraDocumento() {
        when(hitosObraRepository.findById("id")).thenReturn(Optional.empty());

        Exception ex = assertThrows(NoSuchElementException.class, () ->
                hitosObraService.eliminarHito("id", "Etapa")
        );

        assertTrue(ex.getMessage().contains("Hito no encontrado"));
    }
}

