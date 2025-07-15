package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.obra.UsuarioListDTO;
import usach.pingeso.badema.entities.UsuarioEntity;
import usach.pingeso.badema.repositories.postgresql.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioEntity findByCorreo(String correo){
        Optional<UsuarioEntity> userOptional = usuarioRepository.findByCorreo(correo);
        if (userOptional.isPresent()) return userOptional.get();
        else throw new EntityNotFoundException("Usuario no encontrado");
    }

    public void createCliente(UsuarioEntity usuario){
        // Verificar si ya existe un usuario con ese correo
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con este correo");
        }
        usuarioRepository.save(usuario);
    }

    public UsuarioEntity findById(Long idUsuario) {
        Optional<UsuarioEntity> userOptional = usuarioRepository.findById(idUsuario);
        if (userOptional.isPresent()) return userOptional.get();
        else throw new EntityNotFoundException("Usuario no encontrado");
    }

    public List<UsuarioListDTO> getUsuariosNotInObraId(Long idObra){
        return usuarioRepository.findUsuariosNoRegistradosEnObra(idObra);
    }
}
