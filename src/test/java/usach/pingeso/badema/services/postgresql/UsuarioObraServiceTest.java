package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.auth.UsuarioDTO;
import usach.pingeso.badema.dtos.obra.UsuarioObraListDTO;
import usach.pingeso.badema.entities.ObraEntity;
import usach.pingeso.badema.entities.UsuarioEntity;
import usach.pingeso.badema.entities.UsuarioObraEntity;
import usach.pingeso.badema.repositories.postgresql.ObraRepository;
import usach.pingeso.badema.repositories.postgresql.UsuarioObraRepository;
import usach.pingeso.badema.repositories.postgresql.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioObraServiceTest {

    @Mock
    private UsuarioObraRepository usuarioObraRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ObraRepository obraRepository;

    @InjectMocks
    private UsuarioObraService usuarioObraService;

    private UsuarioObraEntity usuarioObra;
    private UsuarioEntity usuario;
    private ObraEntity obra;
    private UsuarioDTO usuarioDTO;
    private UsuarioObraListDTO usuarioObraListDTO;

    @BeforeEach
    void setUp() {
        //Se crea un usuario
        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setCorreo("javier@empresa.cl");
        usuario.setContrasena("secreta123");
        usuario.setNombre("Javier");
        usuario.setApellido("Martínez");
        usuario.setTelefono("+56912345678");
        usuario.setGerencia(false);

        //Se crea una obra
        obra = new ObraEntity();
        obra.setId(2L);
        obra.setNombre("Edificio Central");
        obra.setDireccion("Av. Principal 456");

        //Se crea la relación UsuarioObra
        usuarioObra = new UsuarioObraEntity();
        usuarioObra.setId(10L);
        usuarioObra.setRol("Administrador de obra");
        usuarioObra.setFechaAsignacion(LocalDate.of(2024, 5, 20));
        usuarioObra.setUsuario(usuario);
        usuarioObra.setObra(obra);

        //Se crea un DTO UsuarioObraListDTO
        usuarioObraListDTO = new UsuarioObraListDTO(
                10L, "Javier", "Martínez", "+56912345678",
                "Administrador de obra", LocalDate.of(2024, 5, 20)
        );

        //Se crea un DTO UsuarioDTO
        usuarioDTO = new UsuarioDTO("javier@empresa.cl", "secreta123", "Administrador de obra");
    }

    @Test
    void getUsuarioListDTOByObraId_deberiaRetornarListaDTO() {
        //Se coloca lo que debe retornar al utilizar getUsuariosObraListByObraId
        when(usuarioObraRepository.getUsuariosObraListByObraId(2L))
                .thenReturn(List.of(usuarioObraListDTO));

        //Se utiliza getUsuarioListDTOByObraId
        List<UsuarioObraListDTO> resultado = usuarioObraService.getUsuarioListDTOByObraId(2L);

        //Se verifica el resultado
        assertEquals(1, resultado.size());
        UsuarioObraListDTO dto = resultado.get(0);

        assertEquals(10L, dto.getId());
        assertEquals("Javier", dto.getNombre());
        assertEquals("Martínez", dto.getApellidos());
        assertEquals("+56912345678", dto.getTelefono());
        assertEquals("Administrador de obra", dto.getRol());
        assertEquals(LocalDate.of(2024, 5, 20), dto.getFechaAsignacion());

        verify(usuarioObraRepository).getUsuariosObraListByObraId(2L);
    }

    @Test
    void getUsuarioObraById_existente_deberiaRetornarEntidad() {
        //Se coloca lo que debe retornar al utilizar findById
        when(usuarioObraRepository.findById(10L)).thenReturn(Optional.of(usuarioObra));

        //Se utiliza getUsuarioObraById
        UsuarioObraEntity resultado = usuarioObraService.getUsuarioObraById(10L);

        //Se verifica los datos de prueba
        assertEquals(10L, resultado.getId());
        assertEquals("Administrador de obra", resultado.getRol());
        assertEquals(LocalDate.of(2024, 5, 20), resultado.getFechaAsignacion());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(obra, resultado.getObra());

        verify(usuarioObraRepository).findById(10L);
    }

    @Test
    void getUsuarioObraById_inexistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findById
        when(usuarioObraRepository.findById(99L)).thenReturn(Optional.empty());

        //Se verifica los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                usuarioObraService.getUsuarioObraById(99L));
        verify(usuarioObraRepository).findById(99L);
    }

    @Test
    void getUsuarioObraByObraAndUsuarioId_existente_deberiaRetornarEntidad() {
        //Se coloca lo que debe retornar al utilizar findUsuarioObraEntityByObraIdAndUsuarioId
        when(usuarioObraRepository.findUsuarioObraEntityByObraIdAndUsuarioId(2L, 1L))
                .thenReturn(Optional.of(usuarioObra));

        //Se utiliza getUsuarioObraByObraAndUsuarioId
        UsuarioObraEntity resultado = usuarioObraService.getUsuarioObraByObraAndUsuarioId(2L, 1L);

        //Se verifican los datos de prueba
        assertEquals("Administrador de obra", resultado.getRol());
        assertEquals(LocalDate.of(2024, 5, 20), resultado.getFechaAsignacion());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(obra, resultado.getObra());

        verify(usuarioObraRepository).findUsuarioObraEntityByObraIdAndUsuarioId(2L, 1L);
    }

    @Test
    void getUsuarioObraByObraAndUsuarioId_inexistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findUsuarioObraEntityByObraIdAndUsuarioId
        when(usuarioObraRepository.findUsuarioObraEntityByObraIdAndUsuarioId(2L, 99L))
                .thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                usuarioObraService.getUsuarioObraByObraAndUsuarioId(2L, 99L));
        verify(usuarioObraRepository).findUsuarioObraEntityByObraIdAndUsuarioId(2L, 99L);
    }

    @Test
    void getUsuarioYRolByObraIdAndUsuarioId_conObraMenosUno_deberiaRetornarDesdeUsuario() {
        //Se colocar lo que debe retornar
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        //Se utiliza getUsuarioYRolByObraIdAndUsuarioId
        UsuarioDTO resultado = usuarioObraService.getUsuarioYRolByObraIdAndUsuarioId(-1L, 1L);

        //Se verifican los datos de prueba
        assertEquals("javier@empresa.cl", resultado.getCorreo());
        assertEquals("secreta123", resultado.getContrasena());
        assertEquals("Usuario", resultado.getRol());

        verify(usuarioRepository).findById(1L);
    }

    @Test
    void getUsuarioYRolByObraIdAndUsuarioId_gerencia_deberiaRetornarGerencia() {
        //Se le cambia el valor de isGerencia a verdadero
        usuario.setGerencia(true);

        //Se coloca lo que debe retornar al utiliza findById
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        //Se utiliza getUsuarioYRolByObraIdAndUsuarioId
        UsuarioDTO resultado = usuarioObraService.getUsuarioYRolByObraIdAndUsuarioId(-1L, 1L);

        //Se revisa que el cambio retorne lo correcto
        assertEquals("Gerencia", resultado.getRol());
    }

    @Test
    void getUsuarioYRolByObraIdAndUsuarioId_obraNormal_existente_deberiaRetornarDTO() {
        //Se coloca lo que debe retornar al utilizar getUsuarioYRolByObraIdAndUsuarioId
        when(usuarioObraRepository.getUsuarioYRolByObraIdAndUsuarioId(2L, 1L))
                .thenReturn(Optional.of(usuarioDTO));

        //Se utiliza getUsuarioYRolByObraIdAndUsuarioId
        UsuarioDTO resultado = usuarioObraService.getUsuarioYRolByObraIdAndUsuarioId(2L, 1L);

        //Se verifican los datos de prueba
        assertEquals("javier@empresa.cl", resultado.getCorreo());
        assertEquals("secreta123", resultado.getContrasena());
        assertEquals("Administrador de obra", resultado.getRol());

        verify(usuarioObraRepository).getUsuarioYRolByObraIdAndUsuarioId(2L, 1L);
    }

    @Test
    void getUsuarioYRolByObraIdAndUsuarioId_inexistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar getUsuarioYRolByObraIdAndUsuarioId
        when(usuarioObraRepository.getUsuarioYRolByObraIdAndUsuarioId(2L, 5L))
                .thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                usuarioObraService.getUsuarioYRolByObraIdAndUsuarioId(2L, 5L));
    }

    @Test
    void saveUsuarioObra_existente_deberiaGuardar() {
        //Se coloca lo que debe retornar al utilizar los repositorios
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(obraRepository.findById(2L)).thenReturn(Optional.of(obra));

        //Se utiliza saveUsuarioObra
        usuarioObraService.saveUsuarioObra(2L, 1L, "Adquisiciones");

        //Se verifican los datos de prueba
        verify(usuarioObraRepository).save(any(UsuarioObraEntity.class));
    }

    @Test
    void saveUsuarioObra_usuarioOuObraNoExiste_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findById
        when(usuarioRepository.findById(5L)).thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                usuarioObraService.saveUsuarioObra(2L, 5L, "Adquisiciones"));

        verify(usuarioRepository).findById(5L);
    }

    @Test
    void deleteUsuarioObra_existente_deberiaEliminar() {
        //Se utiliza deleteUsuarioObra
        usuarioObraService.deleteUsuarioObra(10L);

        //Se verifican los datos de prueba
        verify(usuarioObraRepository).deleteById(10L);
    }

    @Test
    void deleteUsuarioObra_errorAlEliminar_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar deleteById
        doThrow(new RuntimeException("No se pudo eliminar el usuario obra")).when(usuarioObraRepository).deleteById(20L);

        //Se verifican los datos de prueba
        assertThrows(RuntimeException.class, () ->
                usuarioObraService.deleteUsuarioObra(20L));
    }

    @Test
    void updateRolUsuarioObra_existente_deberiaActualizarRol() {
        //Se coloca lo que debe retornar al utilizar findById
        when(usuarioObraRepository.findById(10L)).thenReturn(Optional.of(usuarioObra));

        //Se utiliza updateRolUsuarioObra
        usuarioObraService.updateRolUsuarioObra(10L, "Oficina tecnica");

        //Se verifican los datos de prueba
        assertEquals("Oficina tecnica", usuarioObra.getRol());
        verify(usuarioObraRepository).save(usuarioObra);
    }

    @Test
    void updateRolUsuarioObra_inexistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findById
        when(usuarioObraRepository.findById(99L)).thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                usuarioObraService.updateRolUsuarioObra(99L, "Oficina tecnica"));
    }
}
