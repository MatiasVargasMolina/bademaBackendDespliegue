package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.repositories.mongodb.EspecificacionMaterialRepository;
import usach.pingeso.badema.services.postgresql.MaterialService;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EspecificacionesMaterialServiceTest {

    @Mock
    private EspecificacionMaterialRepository especificacionMaterialRepository;

    @Mock
    private MaterialService materialService;

    @InjectMocks
    private EspecificacionesMaterialService especificacionesMaterialService;

    @Captor
    private ArgumentCaptor<EspecificacionMaterialDocument> captor;

    @Test
    void getEspecificacionesByMaterialId_devuelveDocumento() {
        EspecificacionMaterialDocument doc = new EspecificacionMaterialDocument();
        when(especificacionMaterialRepository.findByMaterialId(7L)).thenReturn(doc);

        var result = especificacionesMaterialService.getEspecificacionesByMaterialId(7L);

        assertEquals(doc, result);
    }

    @Test
    void getEspecificacionesByMaterialId_lanzaEntityNotFoundSiFalla() {
        when(especificacionMaterialRepository.findByMaterialId(99L))
                .thenThrow(new RuntimeException("boom"));

        var ex = assertThrows(EntityNotFoundException.class, () ->
                especificacionesMaterialService.getEspecificacionesByMaterialId(99L));
        assertTrue(ex.getMessage().contains("Especificaciones no encontradas"));
    }

    @Test
    void agregarOActualizarEspecificacion_exito() {
        EspecificacionMaterialDocument doc = new EspecificacionMaterialDocument();
        doc.setEspecificaciones(new HashMap<>());
        when(especificacionMaterialRepository.findById("abc")).thenReturn(Optional.of(doc));

        boolean resultado = especificacionesMaterialService.agregarOActualizarEspecificacion("abc", "peso", "20kg");

        assertTrue(resultado);
        assertEquals("20kg", doc.getEspecificaciones().get("peso"));
        verify(especificacionMaterialRepository).save(doc);
    }

    @Test
    void agregarOActualizarEspecificacion_retornaFalseSiExcepcion() {
        when(especificacionMaterialRepository.findById("x"))
                .thenThrow(new RuntimeException("boom"));

        assertFalse(especificacionesMaterialService.agregarOActualizarEspecificacion("x", "tipo", "valor"));
    }

    @Test
    void eliminarEspecificacion_exito() {
        EspecificacionMaterialDocument doc = new EspecificacionMaterialDocument();
        doc.getEspecificaciones().put("color", "rojo");
        when(especificacionMaterialRepository.findById("xyz"))
                .thenReturn(Optional.of(doc));

        especificacionesMaterialService.eliminarEspecificacion("xyz", "color");

        assertFalse(doc.getEspecificaciones().containsKey("color"));
        verify(especificacionMaterialRepository).save(doc);
    }

    @Test
    void eliminarEspecificacion_lanzaExcepcionSiNoExisteTipo() {
        EspecificacionMaterialDocument doc = new EspecificacionMaterialDocument();
        doc.getEspecificaciones().put("peso", "10kg");
        when(especificacionMaterialRepository.findById("id"))
                .thenReturn(Optional.of(doc));

        var ex = assertThrows(IllegalArgumentException.class, () ->
                especificacionesMaterialService.eliminarEspecificacion("id", "noexiste"));

        assertTrue(ex.getMessage().contains("No existe la especificación de tipo"));
        verify(especificacionMaterialRepository, never()).save(any());
    }

    @Test
    void insertarEspecificaciones_exito() {
        Long idMaterial = 55L;
        Map<String, String> especificaciones = Map.of("densidad", "0.9");

        when(materialService.getMaterialById(idMaterial)).thenReturn(new MaterialEntity());

        boolean result = especificacionesMaterialService.insertarEspecificaciones(idMaterial, especificaciones);

        assertTrue(result);
        verify(especificacionMaterialRepository).save(captor.capture());
        var capturado = captor.getValue();
        assertEquals(idMaterial, capturado.getMaterialId());
        assertEquals("0.9", capturado.getEspecificaciones().get("densidad"));
    }

    @Test
    void insertarEspecificaciones_retornaFalseSiMaterialNull() {
        Long idMaterial = 77L;
        when(materialService.getMaterialById(idMaterial)).thenReturn(null);

        assertFalse(especificacionesMaterialService.insertarEspecificaciones(idMaterial, Map.of("prop","val")));
        verify(especificacionMaterialRepository, never()).save(any());
    }

    @Test
    void insertarEspecificaciones_retornaFalseSiExcepcion() {
        Long idMaterial = 88L;
        when(materialService.getMaterialById(idMaterial)).thenThrow(new RuntimeException("boom"));

        assertFalse(especificacionesMaterialService.insertarEspecificaciones(idMaterial, Map.of("a","b")));
        verify(especificacionMaterialRepository, never()).save(any());
    }
}
