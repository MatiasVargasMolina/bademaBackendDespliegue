package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorMaterialDTO;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.entities.ProveedorEntity;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;
import usach.pingeso.badema.repositories.postgresql.MaterialRepository;
import usach.pingeso.badema.repositories.postgresql.ProveedorMaterialRepository;
import usach.pingeso.badema.repositories.postgresql.ProveedorRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorMaterialServiceTest {

    @Mock
    private ProveedorMaterialRepository proveedorMaterialRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private ProveedorMaterialService service;

    private ProveedorEntity proveedor;
    private ProveedorEntity proveedor2;
    private MaterialEntity material;
    private MaterialEntity material2;
    private ProveedorMaterialEntity entidad;
    private ProveedorMaterialEntity entidad2;
    private ProveedorMaterialDTO dto;
    private ProveedorMaterialDTO dto2;

    @BeforeEach
    void setUp() {
        //Primera entidad proveedor-material
        proveedor = new ProveedorEntity();
        proveedor.setId(1L);
        proveedor.setNombreProveedor("Proveedor 1");

        material = new MaterialEntity();
        material.setId(2L);
        material.setNombre("Tablas 2X4 roble");

        entidad = new ProveedorMaterialEntity();
        entidad.setPrecio(1000);
        entidad.setComentarios("Primer comentario");
        entidad.setProveedor(proveedor);
        entidad.setMaterial(material);

        dto = new ProveedorMaterialDTO();
        dto.setIdProveedor(1L);
        dto.setIdMaterial(2L);
        dto.setPrecio(1000);
        dto.setComentarios("Primer comentario");

        //Segunda entidad proveedor-material
        proveedor2 = new ProveedorEntity();
        proveedor2.setId(2L);
        proveedor2.setNombreProveedor("Proveedor 2");

        material2 = new MaterialEntity();
        material2.setId(3L);
        material2.setNombre("Clavos 3 pulgadas");

        entidad2 = new ProveedorMaterialEntity();
        entidad2.setPrecio(500);
        entidad2.setComentarios("Segundo comentario");
        entidad2.setProveedor(proveedor2);
        entidad2.setMaterial(material2);

        dto2 = new ProveedorMaterialDTO();
        dto2.setIdProveedor(2L);
        dto2.setIdMaterial(3L);
        dto2.setPrecio(500);
        dto2.setComentarios("Segundo comentario");
    }

    @Test
    void getAllProveedorMaterial_deberiaRetornarListaDTO() {
        //Se coloca lo que debe retornar cuando se utilice findAll
        when(proveedorMaterialRepository.findAll()).thenReturn(List.of(entidad, entidad2));

        //Se realiza la accion de utilizar getAllProveedorMaterial
        List<ProveedorMaterialDTO> resultado = service.getAllProveedorMaterial();

        //Se verifica que retorne las dos uniones
        assertEquals(2, resultado.size());

        //Verificamos la primera relacion
        assertEquals(1L, resultado.get(0).getIdProveedor());
        assertEquals(2L, resultado.get(0).getIdMaterial());
        assertEquals("Proveedor 1", resultado.get(0).getNombreProveedor());
        assertEquals("Tablas 2X4 roble", resultado.get(0).getNombreMaterial());
        assertEquals(1000, resultado.get(0).getPrecio());
        assertEquals("Primer comentario", resultado.get(0).getComentarios());

        //Verificamos la segunda relacion
        assertEquals(2L, resultado.get(1).getIdProveedor());
        assertEquals(3L, resultado.get(1).getIdMaterial());
        assertEquals("Proveedor 2", resultado.get(1).getNombreProveedor());
        assertEquals("Clavos 3 pulgadas", resultado.get(1).getNombreMaterial());
        assertEquals(500, resultado.get(1).getPrecio());
        assertEquals("Segundo comentario", resultado.get(1).getComentarios());

        verify(proveedorMaterialRepository).findAll();
    }

    @Test
    void getProveedoresPorMaterialId_deberiaRetornarListaEntidades() {
        //Se coloca lo que debe retornar al utilizar findByMaterialId
        when(proveedorMaterialRepository.findByMaterialId(2L)).thenReturn(List.of(entidad));

        //Se utiliza getProveedoresPorMaterialId
        List<ProveedorMaterialEntity> resultado = service.getProveedoresPorMaterialId(2L);

        //Se verifican los datos de prueba
        assertEquals(1, resultado.size());
        assertEquals(1000, resultado.get(0).getPrecio());
        assertEquals("Primer comentario", resultado.get(0).getComentarios());

        verify(proveedorMaterialRepository).findByMaterialId(2L);
    }

    @Test
    void getMaterialesByProveedorId_deberiaRetornarListaEntidades() {
        //Se coloca lo que debe retornar al utilizar findByProveedorId
        when(proveedorMaterialRepository.findByProveedorId(1L)).thenReturn(List.of(entidad));

        //Se utiliza getMaterialesByProveedorId
        List<ProveedorMaterialEntity> resultado = service.getMaterialesByProveedorId(1L);

        //Se verifican los datos de prueba
        assertEquals(1, resultado.size());
        assertEquals(1000, resultado.get(0).getPrecio());
        assertEquals("Primer comentario", resultado.get(0).getComentarios());

        verify(proveedorMaterialRepository).findByProveedorId(1L);
    }

    @Test
    void getProveedorMaterialByMaterialId_deberiaRetornarListaDTO() {
        //Se coloca lo que debe retornar al utilizar findByMaterialId
        when(proveedorMaterialRepository.findByMaterialId(2L)).thenReturn(List.of(entidad));

        //Se utiliza getProveedorMaterialByMaterialId
        List<ProveedorMaterialDTO> resultado = service.getProveedorMaterialByMaterialId(2L);

        //Se verifican los datos de prueba
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdProveedor());
        assertEquals(2L, resultado.get(0).getIdMaterial());
        assertEquals("Proveedor 1", resultado.get(0).getNombreProveedor());
        assertEquals("Tablas 2X4 roble", resultado.get(0).getNombreMaterial());
        assertEquals(1000, resultado.get(0).getPrecio());
        assertEquals("Primer comentario", resultado.get(0).getComentarios());

        verify(proveedorMaterialRepository).findByMaterialId(2L);
    }

    @Test
    void getByProveedorAndMaterial_existente_deberiaRetornarEntidad() {
        //Se coloca lo que debe retornar al utilizar findByProveedorIdAndMaterialId
        when(proveedorMaterialRepository.findByProveedorIdAndMaterialId(1L, 2L))
                .thenReturn(Optional.of(entidad));

        //Se utiliza getByProveedorAndMaterial
        ProveedorMaterialEntity resultado = service.getByProveedorAndMaterial(1L, 2L);

        //Se verifican los datos de prueba
        assertEquals(1000, resultado.getPrecio());
        assertEquals("Primer comentario", resultado.getComentarios());
        verify(proveedorMaterialRepository).findByProveedorIdAndMaterialId(1L, 2L);
    }

    @Test
    void getByProveedorAndMaterial_inexistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findByProveedorIdAndMaterialId
        when(proveedorMaterialRepository.findByProveedorIdAndMaterialId(1L, 2L))
                .thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(RuntimeException.class, () ->
                service.getByProveedorAndMaterial(1L, 2L));
    }

    @Test
    void saveProveedorMaterial_deberiaGuardarRelacion() {
        //Se coloca lo que debe retornar al utilizar los repositorios
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(materialRepository.findById(2L)).thenReturn(Optional.of(material));
        when(proveedorMaterialRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        //Se utiliza saveProveedorMaterial
        ProveedorMaterialEntity resultado = service.saveProveedorMaterial(1L, 2L, dto);

        //Se verifican los datos de prueba
        assertEquals(1000, resultado.getPrecio());
        assertEquals("Primer comentario", resultado.getComentarios());
        verify(proveedorMaterialRepository).save(any());
    }

    @Test
    void updateProveedorMaterial_existente_deberiaActualizarYRetornarDTO() {
        //Se crea el DTO nuevo para poder actulizar la relacion
        ProveedorMaterialDTO nuevoDTO = new ProveedorMaterialDTO();
        nuevoDTO.setIdProveedor(1L);
        nuevoDTO.setIdMaterial(2L);
        nuevoDTO.setPrecio(2000);
        nuevoDTO.setComentarios("Nuevo comentario");

        //Se coloca lo que debe retornar al utilizar los repositorios
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(materialRepository.findById(2L)).thenReturn(Optional.of(material));
        when(proveedorMaterialRepository.findByProveedorIdAndMaterialId(1L, 2L)).thenReturn(Optional.of(entidad));
        when(proveedorMaterialRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        //Se utiliza updateProveedorMaterial
        ProveedorMaterialDTO resultado = service.updateProveedorMaterial(1L, 2L, nuevoDTO);

        //Se verifican los datos de prueba
        assertEquals(2000, resultado.getPrecio());
        assertEquals("Nuevo comentario", resultado.getComentarios());
    }

    @Test
    void updateProveedorMaterial_proveedorNoExiste_deberiaLanzarExcepcion() {
        //Se crea el DTO nuevo para poder actulizar la relacion
        ProveedorMaterialDTO nuevoDTO = new ProveedorMaterialDTO();
        nuevoDTO.setIdProveedor(5L);
        nuevoDTO.setIdMaterial(2L);
        nuevoDTO.setPrecio(2000);
        nuevoDTO.setComentarios("Nuevo comentario");

        //Se coloca lo que debe retornar al utilizar el repositorio
        when(proveedorRepository.findById(5L)).thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(RuntimeException.class, () ->
                service.updateProveedorMaterial(5L, 2L, nuevoDTO));
    }

    @Test
    void updateProveedorMaterial_materialNoExiste_deberiaLanzarExcepcion() {
        //Se crea el DTO nuevo para poder actulizar la relacion
        ProveedorMaterialDTO nuevoDTO = new ProveedorMaterialDTO();
        nuevoDTO.setIdProveedor(1L);
        nuevoDTO.setIdMaterial(5L);
        nuevoDTO.setPrecio(2000);
        nuevoDTO.setComentarios("Nuevo comentario");

        //Se coloca lo que debe retornar al utilizar los repositorios
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(materialRepository.findById(5L)).thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                service.updateProveedorMaterial(1L, 5L, nuevoDTO));
    }

    @Test
    void updateProveedorMaterial_relacionNoExiste_deberiaLanzarExcepcion() {
        //Se crea el DTO nuevo para poder actulizar la relacion
        ProveedorMaterialDTO nuevoDTO = new ProveedorMaterialDTO();
        nuevoDTO.setIdProveedor(1L);
        nuevoDTO.setIdMaterial(3L);
        nuevoDTO.setPrecio(2000);
        nuevoDTO.setComentarios("Nuevo comentario");

        //Se coloca lo que debe retornar al utilizar los repositorios
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor));
        when(materialRepository.findById(3L)).thenReturn(Optional.of(material));
        when(proveedorMaterialRepository.findByProveedorIdAndMaterialId(1L, 3L)).thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                service.updateProveedorMaterial(1L, 3L, nuevoDTO));
    }

    @Test
    void deleteProveedorMaterial_existente_deberiaEliminarRelacion() {
        //Se coloca lo que debe retornar al utilizar los repositorios
        when(proveedorMaterialRepository.findByProveedorIdAndMaterialId(1L, 2L))
                .thenReturn(Optional.of(entidad));

        //Se verifican los datos de prueba
        service.deleteProveedorMaterial(1L, 2L);

        verify(proveedorMaterialRepository).deleteByProveedorIdAndMaterialId(1L, 2L);
    }

    @Test
    void deleteProveedorMaterial_inexistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar los repositorios
        when(proveedorMaterialRepository.findByProveedorIdAndMaterialId(1L, 5L))
                .thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                service.deleteProveedorMaterial(1L, 5L));
    }
}
