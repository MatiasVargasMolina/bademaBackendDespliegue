package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usach.pingeso.badema.dtos.obra.UsuarioListDTO;
import usach.pingeso.badema.entities.UsuarioEntity;
import usach.pingeso.badema.repositories.postgresql.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void findByCorreo_existente_deberiaRetornarUsuario() {
        //Se crea el usuario con datos de prueba
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("usuario@ejemplo.com");

        //Se coloca lo que debe retornar al utilizar findByCorreo
        when(usuarioRepository.findByCorreo("usuario@ejemplo.com"))
                .thenReturn(Optional.of(usuario));

        //Se realiza la accion de utilizar findByCorreo
        UsuarioEntity resultado = usuarioService.findByCorreo("usuario@ejemplo.com");

        //Se verifican los datos de prueba
        assertEquals("usuario@ejemplo.com", resultado.getCorreo());
        verify(usuarioRepository).findByCorreo("usuario@ejemplo.com");
    }

    @Test
    void findByCorreo_noExistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar al utilizar findByCorreo
        when(usuarioRepository.findByCorreo("no@existe.com"))
                .thenReturn(Optional.empty());

        //Se verifica los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                usuarioService.findByCorreo("no@existe.com"));
        verify(usuarioRepository).findByCorreo("no@existe.com");
    }

    @Test
    void findById_existente_deberiaRetornarUsuario() {
        //Se crea el usuario con datos de prueba
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);

        //Se coloca que debe retornar al utilizar findById
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        //Se realiza la accion de utilizar findById
        UsuarioEntity resultado = usuarioService.findById(1L);

        //Se verifican los datos de prueba
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void findById_noExistente_deberiaLanzarExcepcion() {
        //Se coloca lo que debe retornar cuando se utiliza findById
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        //Se verifican los datos de prueba
        assertThrows(EntityNotFoundException.class, () ->
                usuarioService.findById(99L));
        verify(usuarioRepository).findById(99L);
    }

    @Test
    void createCliente_deberiaGuardarUsuario() {
        //Se crea un usuario con datos de prueba
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("cliente.nuevo@mail.com");
        usuario.setContrasena("hola123");
        usuario.setNombre("Cliente");
        usuario.setApellido("Nuevo");
        usuario.setTelefono("+56912345678");
        usuario.setGerencia(false);

        //Se utiliza realiza la accion de utilizar createCliente
        usuarioService.createCliente(usuario);

        //Se verifican los datos de prueba
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void createCliente_deberiaLanzarExcepcionSiCorreoYaExiste() {
        // Arrange
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCorreo("cliente.nuevo@mail.com");
        usuario.setContrasena("hola123");
        usuario.setNombre("Cliente");
        usuario.setApellido("Nuevo");
        usuario.setTelefono("+56912345678");
        usuario.setGerencia(false);

        // Se guarda el usuario una primera vez
        usuarioService.createCliente(usuario);

        // Creamos otro con el mismo correo
        UsuarioEntity usuarioDuplicado = new UsuarioEntity();
        usuarioDuplicado.setCorreo("cliente.nuevo@mail.com");
        usuarioDuplicado.setContrasena("otraPass");
        usuarioDuplicado.setNombre("Cliente");
        usuarioDuplicado.setApellido("Duplicado");
        usuarioDuplicado.setTelefono("+56987654321");
        usuarioDuplicado.setGerencia(false);

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.createCliente(usuarioDuplicado),
                "Se esperaba que lanzara IllegalArgumentException al registrar correo duplicado"
        );

        assertEquals("Ya existe un usuario registrado con este correo", thrown.getMessage());

        // Verifica que NO se haya intentado guardar el duplicado
        verify(usuarioRepository, times(0)).save(usuarioDuplicado);
    }

    @Test
    void getUsuariosNotInObraId_deberiaRetornarListaDTO() {
        //Se crean los usuarios con datos de prueba
        UsuarioListDTO dto1 = new UsuarioListDTO(1L, "Juan", "Pérez",
                "juan@ejemplo.com", "+56912345678");
        UsuarioListDTO dto2 = new UsuarioListDTO(2L, "Ana", "Díaz",
                "ana@ejemplo.com", "+56987654321");

        //Se coloca lo que debe retornar al utilizar findUsuariosNoRegistradosEnObra
        when(usuarioRepository.findUsuariosNoRegistradosEnObra(5L)).thenReturn(List.of(dto1, dto2));

        //Se realiza la accion de utilizar getUsuariosNotInObraId
        List<UsuarioListDTO> resultado = usuarioService.getUsuariosNotInObraId(5L);

        //se verifican los datos de prueba
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.getFirst().getNombre());
        assertEquals("Pérez", resultado.getFirst().getApellido());
        assertEquals("juan@ejemplo.com",resultado.getFirst().getCorreo());
        assertEquals("+56912345678",resultado.getFirst().getTelefono());

        assertEquals("Ana", resultado.get(1).getNombre());
        assertEquals("Díaz", resultado.get(1).getApellido());
        assertEquals("ana@ejemplo.com",resultado.get(1).getCorreo());
        assertEquals("+56987654321",resultado.get(1).getTelefono());

        verify(usuarioRepository).findUsuariosNoRegistradosEnObra(5L);
    }
}
