package usach.pingeso.badema.services.mongodb;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;
import usach.pingeso.badema.documents.ValorEspecificacion;
import usach.pingeso.badema.repositories.mongodb.NuevasEspecificacionesArchivosRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NuevasEspecificacionesServiceTest {

    @Mock
    private NuevasEspecificacionesArchivosRepository nuevasEspecificacionesArchivosRepository;

    @InjectMocks
    private NuevasEspecificacionesService nuevasEspecificacionesService;

    @Captor
    private ArgumentCaptor<NuevasEspecificacionesDocument> captor;

    @Test
    void getNuevasEspecificacionesByProveedorMaterialId_devuelveDocumento() {
        NuevasEspecificacionesDocument doc = new NuevasEspecificacionesDocument();
        when(nuevasEspecificacionesArchivosRepository.findByIdProveedorMaterial(10L)).thenReturn(doc);

        var resultado = nuevasEspecificacionesService.getNuevasEspecificacionesByProveedorMaterialId(10L);

        assertEquals(doc, resultado);
        verify(nuevasEspecificacionesArchivosRepository).findByIdProveedorMaterial(10L);
    }

    @Test
    void getNuevasEspecificacionesByProveedorMaterialId_lanzaEntityNotFoundSiRepoFalla() {
        when(nuevasEspecificacionesArchivosRepository.findByIdProveedorMaterial(99L))
                .thenThrow(new RuntimeException("boom"));

        var ex = assertThrows(EntityNotFoundException.class, () ->
                nuevasEspecificacionesService.getNuevasEspecificacionesByProveedorMaterialId(99L));

        assertTrue(ex.getMessage().contains("Nuevas Especificaciones no encontradas"));
    }

    @Test
    void insertarNuevasEspecificaciones_exito() {
        Map<String, ValorEspecificacion> map = new HashMap<>();
        map.put("resistencia", new ValorEspecificacion("igual", true));

        ProveedorMaterialEntity proveedor = new ProveedorMaterialEntity();
        proveedor.setId(77L);

        boolean resultado = nuevasEspecificacionesService.insertarNuevasEspecificaciones(map, proveedor);

        assertTrue(resultado);
        verify(nuevasEspecificacionesArchivosRepository).save(captor.capture());
        NuevasEspecificacionesDocument capturado = captor.getValue();
        assertEquals(77L, capturado.getIdProveedorMaterial());
        assertTrue(capturado.getEspecificacionesNuevas().get("resistencia").getVigente());
    }

    @Test
    void insertarNuevasEspecificaciones_lanzaRuntimeSiSaveFalla() {
        Map<String, ValorEspecificacion> map = new HashMap<>();
        ProveedorMaterialEntity proveedor = new ProveedorMaterialEntity();
        proveedor.setId(55L);

        doThrow(new RuntimeException("boom"))
                .when(nuevasEspecificacionesArchivosRepository).save(any());

        var ex = assertThrows(RuntimeException.class, () ->
                nuevasEspecificacionesService.insertarNuevasEspecificaciones(map, proveedor));

        assertTrue(ex.getMessage().contains("No se pudo insertar la especificacion"));
    }
}