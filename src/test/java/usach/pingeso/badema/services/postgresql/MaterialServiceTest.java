package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.adquisiciones.MaterialListAdqDTO;
import usach.pingeso.badema.dtos.pedido.MaterialUpdateDTO;
import usach.pingeso.badema.dtos.seguimiento.MaterialConOrdenDTO;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.repositories.postgresql.MaterialRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    private MaterialEntity material;

    @BeforeEach
    void setUp() {
        material = new MaterialEntity();
        material.setId(1L);
        material.setNombre("Cemento");
    }

    @Test
    void getMaterialById_existente_deberiaRetornarEntidad() {
        //Se coloca lo que debe retornar al utilizar findById
        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        //Se utiliza getMaterialById
        MaterialEntity resultado = materialService.getMaterialById(1L);

        //Se verifican los datos prueba
        assertEquals(1L, resultado.getId());
        assertEquals("Cemento", resultado.getNombre());
        verify(materialRepository).findById(1L);
    }

    @Test
    void getMaterialById_inexistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findById
        when(materialRepository.findById(99L)).thenReturn(Optional.empty());

        //Se verifican los datos prueba
        assertThrows(EntityNotFoundException.class, () ->
                materialService.getMaterialById(99L));
        verify(materialRepository).findById(99L);
    }

    @Test
    void getMaterialByNombre_existente_deberiaRetornarDTO() {
        //Se coloca lo que debe retornar al utilizar findByNombre
        when(materialRepository.findByNombre("Cemento")).thenReturn(Optional.of(material));

        //Se utiliza getMaterialByNombre
        MaterialListAdqDTO resultado = materialService.getMaterialByNombre("Cemento");

        //Se verifican los datos de prueba
        assertNotNull(resultado);
        assertEquals("Cemento", resultado.getNombre());
        verify(materialRepository).findByNombre("Cemento");
    }

    @Test
    void getMaterialByNombre_inexistente_deberiaRetornarNull() {
        //Se coloca lo que debe retornar al utilizar findByNombre
        when(materialRepository.findByNombre("Ladrillo")).thenReturn(Optional.empty());

        //Se utiliza getMaterialByNombre
        MaterialListAdqDTO resultado = materialService.getMaterialByNombre("Ladrillo");

        //Se verifican los datos de prueba
        assertNull(resultado);
        verify(materialRepository).findByNombre("Ladrillo");
    }

    @Test
    void getMaterialesConOrdenes_deberiaRetornarListaDTO() {
        //Se crea el DTO MaterialConOrdenDTO
        MaterialConOrdenDTO dto = new MaterialConOrdenDTO(1L, "Cemento", 5L);

        //Se coloca lo que debe retornar cuando se utiliza getMaterialesConOrdenes
        when(materialRepository.getMaterialesConOrdenes(1L)).thenReturn(List.of(dto));

        //Se utiliza getMaterialesConOrdenes
        List<MaterialConOrdenDTO> resultado = materialService.getMaterialesConOrdenes(1L);

        //Se verifican lo datos prueba
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdMaterial());
        assertEquals("Cemento", resultado.get(0).getNombreMaterial());
        assertEquals(5L, resultado.get(0).getCantidadOrdenesAsociadas());
        verify(materialRepository).getMaterialesConOrdenes(1L);
    }

    @Test
    void saveMaterial_deberiaGuardarYRetornarEntidad() {
        //Se coloca lo que debe retornar al utilizar save
        when(materialRepository.save(any(MaterialEntity.class)))
                .thenAnswer(invocation -> {
                    MaterialEntity m = invocation.getArgument(0);
                    m.setId(2L);
                    return m;
                });

        //Se utiliza saveMaterial
        MaterialEntity resultado = materialService.saveMaterial("Hormigón");

        //Se verifican los datos de prueba
        assertEquals(2L, resultado.getId());
        assertEquals("Hormigón", resultado.getNombre());
        verify(materialRepository).save(any(MaterialEntity.class));
    }

    @Test
    void updateMaterial_existente_deberiaActualizarYRetornarDTO() {
        //Se coloca lo que debe retornar cuando se utilizan los repositorios
        when(materialRepository.findById(2L)).thenReturn(Optional.of(material));
        when(materialRepository.save(any(MaterialEntity.class))).thenReturn(material);

        //Se crea el DTO MaterialUpdateDTO
        MaterialUpdateDTO dto = new MaterialUpdateDTO();
        dto.setNombre("Hormigón");

        //Se utiliza updateMaterial
        MaterialUpdateDTO resultado = materialService.updateMaterial(dto, 2L);

        //Se verifican los datos de prueba
        assertEquals("Hormigón", resultado.getNombre());
        verify(materialRepository).findById(2L);
        verify(materialRepository).save(any(MaterialEntity.class));
    }

    @Test
    void updateMaterial_inexistente_deberiaRetornarNull() {
        //Se coloca lo que debe retornar al utilizar findById
        when(materialRepository.findById(3L)).thenReturn(Optional.empty());

        //Se crea el DTO MaterialUpdateDTO
        MaterialUpdateDTO dto = new MaterialUpdateDTO();
        dto.setNombre("Acero");

        //Se utiliza updateMaterial
        MaterialUpdateDTO resultado = materialService.updateMaterial(dto, 3L);

        //Se verifican los datos de prueba
        assertNull(resultado);
        verify(materialRepository).findById(3L);
        verify(materialRepository, never()).save(any());
    }
}
