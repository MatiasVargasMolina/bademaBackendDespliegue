package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
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

@Service
public class UsuarioObraService {
    private final UsuarioObraRepository usuarioObraRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObraRepository obraRepository;

    public UsuarioObraService(UsuarioObraRepository usuarioObraRepository, UsuarioRepository usuarioRepository, ObraRepository obraRepository) {
        this.usuarioObraRepository = usuarioObraRepository;
        this.usuarioRepository = usuarioRepository;
        this.obraRepository = obraRepository;
    }

    public List<UsuarioObraListDTO> getUsuarioListDTOByObraId(Long obraId){
        return usuarioObraRepository.getUsuariosObraListByObraId(obraId);
    }

    public UsuarioObraEntity getUsuarioObraById(Long id){
        Optional<UsuarioObraEntity> responsable = usuarioObraRepository.findById(id);
        if (responsable.isPresent()) return responsable.get();
        else throw new EntityNotFoundException("Responsable no encontrado: "+ id);
    }

    public UsuarioObraEntity getUsuarioObraByObraAndUsuarioId(Long idObra, Long idUsuario){
        Optional<UsuarioObraEntity> responsable = usuarioObraRepository.findUsuarioObraEntityByObraIdAndUsuarioId(idObra, idUsuario);
        if (responsable.isPresent()) return responsable.get();
        else throw new EntityNotFoundException("Responsable no encontrado con obra: "+idObra+", usuario: "+idUsuario);
    }

    public UsuarioDTO getUsuarioYRolByObraIdAndUsuarioId(Long idObra, Long idUsuario){
        if (idObra == -1){
            Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(idUsuario);
            if (usuarioOpt.isPresent()) {
                UsuarioEntity usuarioEntity = usuarioOpt.get();
                String rol = usuarioEntity.isGerencia() ? "Gerencia" : "Usuario";
                return new UsuarioDTO(usuarioEntity.getCorreo(), usuarioEntity.getContrasena(), rol);
            } else {
                throw new EntityNotFoundException("Usuario no encontrado");
            }
        }
        Optional<UsuarioDTO> usuarioDTOOptional = usuarioObraRepository.getUsuarioYRolByObraIdAndUsuarioId(idObra, idUsuario);
        if (usuarioDTOOptional.isPresent()) return usuarioDTOOptional.get();
        else throw new EntityNotFoundException("Responsable no encontrado para la obra");
    }

    public void saveUsuarioObra(Long idObra, Long idUsuario, String rol) {
        Optional<UsuarioEntity> usuarioOptional = usuarioRepository.findById(idUsuario);
        Optional<ObraEntity> obraOptional = obraRepository.findById(idObra);

        if (usuarioOptional.isEmpty() || obraOptional.isEmpty()) {
            throw new EntityNotFoundException("Usuario u Obra no encontrados");
        }

        UsuarioObraEntity usuarioObra = new UsuarioObraEntity();
        usuarioObra.setUsuario(usuarioOptional.get());
        usuarioObra.setObra(obraOptional.get());
        usuarioObra.setRol(rol);
        usuarioObra.setFechaAsignacion(LocalDate.now());
        try{
            usuarioObraRepository.save(usuarioObra);
        }
        catch (Exception e){
            throw new RuntimeException("No se pudo guardar el usuario obra");
        }
    }

    public void deleteUsuarioObra(Long id){
        try{
            usuarioObraRepository.deleteById(id);
        }
        catch (Exception e){
            throw new RuntimeException("No se pudo eliminar el usuario obra");
        }
    }

    public void updateRolUsuarioObra(Long id, String rol){
        Optional<UsuarioObraEntity> usuarioObra = usuarioObraRepository.findById(id);
        if (usuarioObra.isPresent()) {
            UsuarioObraEntity usuarioObraEntity = usuarioObra.get();
            usuarioObraEntity.setRol(rol);
            usuarioObraRepository.save(usuarioObraEntity);
        }
        else{
            throw new EntityNotFoundException("Usuario no encontrado");
        }
    }
}
