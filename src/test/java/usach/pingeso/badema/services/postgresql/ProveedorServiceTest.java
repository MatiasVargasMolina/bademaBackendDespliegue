package usach.pingeso.badema.services.postgresql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.adquisiciones.*;
import usach.pingeso.badema.entities.ProveedorEntity;
import usach.pingeso.badema.repositories.postgresql.ProveedorRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    private ProveedorEntity proveedor1;
    private ProveedorEntity proveedor2;
    private ProveedorCreateDTO createDTO;
    private ProveedorUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        //Se crea el primer proveedor con datos de prueba
        proveedor1 = new ProveedorEntity();
        proveedor1.setId(1L);
        proveedor1.setNombreProveedor("Maderas Chile Ltda.");
        proveedor1.setRutProveedor("76.123.456-7");
        proveedor1.setTelefonoProveedor("987654321");
        proveedor1.setNombreVendedor("Juan Pérez");
        proveedor1.setTelefonoVendedor("912345678");
        proveedor1.setEmailVendedor("juan.perez@maderas.cl");
        proveedor1.setDireccionProveedor("Av. Forestal 123");
        proveedor1.setCondiciones("Crédito 30 días");
        proveedor1.setRestricciones("No despacha a regiones");

        //Se crea un segundo proveedor con datos mínimos
        proveedor2 = new ProveedorEntity();
        proveedor2.setId(2L);
        proveedor2.setNombreProveedor("Ferretería Central");
        proveedor2.setRutProveedor("76.765.432-1");
        proveedor2.setTelefonoProveedor("998877665");
        proveedor2.setNombreVendedor("Pedro Ramírez");
        proveedor2.setTelefonoVendedor("911223344");
        proveedor2.setEmailVendedor("pedro@ferrecentral.cl");
        proveedor2.setDireccionProveedor("Calle Férrea 456");
        proveedor2.setCondiciones("Contado");
        proveedor2.setRestricciones("Sin restricciones");

        //Se prepara el DTO para guardar
        createDTO = new ProveedorCreateDTO();
        createDTO.setNombreProveedor("Nuevo Proveedor");
        createDTO.setRutProveedor("99.999.999-9");
        createDTO.setTelefonoProveedor("999999999");
        createDTO.setNombreVendedor("Carlos Soto");
        createDTO.setTelefonoVendedor("900112233");
        createDTO.setEmailVendedor("carlos@proveedor.cl");
        createDTO.setDireccionProveedor("Camino Nuevo 789");
        createDTO.setCondiciones("Pago anticipado");
        createDTO.setRestricciones("Solo región Metropolitana");

        //Se prepara el DTO para actualizar
        updateDTO = new ProveedorUpdateDTO();
        updateDTO.setNombreProveedor("Proveedor Actualizado");
        updateDTO.setRutProveedor("11.111.111-1");
        updateDTO.setTelefonoProveedor("123456789");
        updateDTO.setNombreVendedor("María López");
        updateDTO.setTelefonoVendedor("933445566");
        updateDTO.setEmailVendedor("maria@actualizado.cl");
        updateDTO.setDireccionProveedor("Pasaje Actual 321");
        updateDTO.setCondiciones("Crédito 60 días");
        updateDTO.setRestricciones("Sin deuda vigente");
    }

    @Test
    void getAllProveedores_deberiaRetornarListaDTO() {
        //Se coloca lo que debe retornar al utilizar findAll
        when(proveedorRepository.findAll()).thenReturn(List.of(proveedor1, proveedor2));

        //Se utiliza getAllProveedores
        List<ProveedorListDTO> resultado = proveedorService.getAllProveedores();

        //Se verifican los datos de prueba
        assertEquals(2, resultado.size());

        ProveedorListDTO dto1 = resultado.get(0);
        assertEquals(proveedor1.getId(), dto1.getId());
        assertEquals(proveedor1.getNombreProveedor(), dto1.getNombreProveedor());
        assertEquals(proveedor1.getRutProveedor(), dto1.getRutProveedor());
        assertEquals(proveedor1.getTelefonoProveedor(), dto1.getTelefonoProveedor());
        assertEquals(proveedor1.getNombreVendedor(), dto1.getNombreVendedor());
        assertEquals(proveedor1.getTelefonoVendedor(), dto1.getTelefonoVendedor());
        assertEquals(proveedor1.getEmailVendedor(), dto1.getEmailVendedor());
        assertEquals(proveedor1.getDireccionProveedor(), dto1.getDireccionProveedor());
        assertEquals(proveedor1.getCondiciones(), dto1.getCondiciones());
        assertEquals(proveedor1.getRestricciones(), dto1.getRestricciones());

        ProveedorListDTO dto2 = resultado.get(1);
        assertEquals(proveedor2.getId(), dto2.getId());
        assertEquals(proveedor2.getNombreProveedor(), dto2.getNombreProveedor());
        assertEquals(proveedor2.getRutProveedor(), dto2.getRutProveedor());
        assertEquals(proveedor2.getTelefonoProveedor(), dto2.getTelefonoProveedor());
        assertEquals(proveedor2.getNombreVendedor(), dto2.getNombreVendedor());
        assertEquals(proveedor2.getTelefonoVendedor(), dto2.getTelefonoVendedor());
        assertEquals(proveedor2.getEmailVendedor(), dto2.getEmailVendedor());
        assertEquals(proveedor2.getDireccionProveedor(), dto2.getDireccionProveedor());
        assertEquals(proveedor2.getCondiciones(), dto2.getCondiciones());
        assertEquals(proveedor2.getRestricciones(), dto2.getRestricciones());

        verify(proveedorRepository).findAll();
    }

    @Test
    void getProveedorById_existente_deberiaRetornarDTO() {
        //Se coloca lo que debe retornar al utilizar findById
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor1));

        //Se utiliza getProveedorById
        ProveedorListDTO dto = proveedorService.getProveedorById(1L);

        //Se verifican los datos de prueba
        assertNotNull(dto);
        assertEquals("Maderas Chile Ltda.", dto.getNombreProveedor());
        assertEquals("76.123.456-7", dto.getRutProveedor());
        assertEquals("987654321", dto.getTelefonoProveedor());
        assertEquals("Juan Pérez", dto.getNombreVendedor());
        assertEquals("912345678", dto.getTelefonoVendedor());
        assertEquals("juan.perez@maderas.cl", dto.getEmailVendedor());
        assertEquals("Av. Forestal 123", dto.getDireccionProveedor());
        assertEquals("Crédito 30 días", dto.getCondiciones());
        assertEquals("No despacha a regiones", dto.getRestricciones());
        verify(proveedorRepository).findById(1L);
    }

    @Test
    void getProveedorById_inexistente_deberiaRetornarNull() {
        //Se coloca lo que debe retornar al utilizar findById
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        //Se utiliza getProveedorById
        ProveedorListDTO resultado = proveedorService.getProveedorById(99L);

        //Se verifica que el resultado sea null
        assertNull(resultado);
        verify(proveedorRepository).findById(99L);
    }

    @Test
    void saveProveedor_deberiaRetornarDTOGuardado() {
        //Se coloca lo que debe retornar al guardar el proveedor
        when(proveedorRepository.save(any())).thenAnswer(inv -> {
            ProveedorEntity saved = inv.getArgument(0);
            saved.setId(3L);
            return saved;
        });

        //Se utiliza saveProveedor
        ProveedorCreateDTO resultado = proveedorService.saveProveedor(createDTO);

        //Se verifican los datos retornados
        assertEquals("Nuevo Proveedor", resultado.getNombreProveedor());
        assertEquals("99.999.999-9", resultado.getRutProveedor());
        assertEquals("999999999", resultado.getTelefonoProveedor());
        assertEquals("Carlos Soto", resultado.getNombreVendedor());
        assertEquals("900112233", resultado.getTelefonoVendedor());
        assertEquals("carlos@proveedor.cl", resultado.getEmailVendedor());
        assertEquals("Camino Nuevo 789", resultado.getDireccionProveedor());
        assertEquals("Pago anticipado", resultado.getCondiciones());
        assertEquals("Solo región Metropolitana", resultado.getRestricciones());
        verify(proveedorRepository).save(any());
    }

    @Test
    void updateProveedor_existente_deberiaRetornarDTOActualizado() {
        //Se coloca lo que debe retornar al buscar y guardar el proveedor
        when(proveedorRepository.findById(1L)).thenReturn(Optional.of(proveedor1));
        when(proveedorRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        //Se utiliza updateProveedor
        ProveedorUpdateDTO resultado = proveedorService.updateProveedor(updateDTO, 1L);

        //Se verifican los datos actualizados
        assertEquals("Proveedor Actualizado", resultado.getNombreProveedor());
        assertEquals("11.111.111-1", resultado.getRutProveedor());
        assertEquals("123456789", resultado.getTelefonoProveedor());
        assertEquals("María López", resultado.getNombreVendedor());
        assertEquals("933445566", resultado.getTelefonoVendedor());
        assertEquals("maria@actualizado.cl", resultado.getEmailVendedor());
        assertEquals("Pasaje Actual 321", resultado.getDireccionProveedor());
        assertEquals("Crédito 60 días", resultado.getCondiciones());
        assertEquals("Sin deuda vigente", resultado.getRestricciones());
        verify(proveedorRepository).save(any());
    }

    @Test
    void updateProveedor_inexistente_deberiaRetornarNull() {
        //Se coloca lo que debe retornar al buscar por el id inexistente
        when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

        //Se utiliza updateProveedor y se espera resultado null
        ProveedorUpdateDTO resultado = proveedorService.updateProveedor(updateDTO, 99L);

        //Se verifica el resultado
        assertNull(resultado);
        verify(proveedorRepository).findById(99L);
    }

    @Test
    void getProveedoresMaterialByMaterialId_deberiaRetornarListaDTOCompleta() {
        //Se crea un detalle DTO con datos de prueba
        ProveedorDetalleDTO detalle = new ProveedorDetalleDTO(
                1L, "Maderas Chile Ltda.", "76.123.456-7", "987654321",
                "Juan Pérez", "912345678", "juan.perez@maderas.cl", "Av. Forestal 123",
                "Crédito 30 días", "No despacha a regiones"
        );

        //Se coloca lo que debe retornar al ejecutar el query personalizado
        when(proveedorRepository.getProveedoresByMaterialId(10L)).thenReturn(List.of(detalle));

        //Se utiliza getProveedoresMaterialByMaterialId
        List<ProveedorDetalleDTO> resultado = proveedorService.getProveedoresMaterialByMaterialId(10L);

        //Se verifican los datos retornados
        assertEquals(1, resultado.size());
        ProveedorDetalleDTO dto = resultado.get(0);
        assertEquals("Maderas Chile Ltda.", dto.getNombreProveedor());
        assertEquals("76.123.456-7", dto.getRutProveedor());
        assertEquals("987654321", dto.getTelefonoProveedor());
        assertEquals("Juan Pérez", dto.getNombreVendedor());
        assertEquals("912345678", dto.getTelefonoVendedor());
        assertEquals("juan.perez@maderas.cl", dto.getEmailVendedor());
        assertEquals("Av. Forestal 123", dto.getDireccionProveedor());
        assertEquals("Crédito 30 días", dto.getCondiciones());
        assertEquals("No despacha a regiones", dto.getRestricciones());

        verify(proveedorRepository).getProveedoresByMaterialId(10L);
    }
}