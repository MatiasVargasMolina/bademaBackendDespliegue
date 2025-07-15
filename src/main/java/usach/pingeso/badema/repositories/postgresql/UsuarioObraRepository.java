package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.auth.UsuarioDTO;
import usach.pingeso.badema.dtos.obra.UsuarioObraListDTO;
import usach.pingeso.badema.entities.UsuarioObraEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioObraRepository extends JpaRepository<UsuarioObraEntity, Long> {

    @Query("SELECT new usach.pingeso.badema.dtos.obra.UsuarioObraListDTO(uo.id, u.nombre, u.apellido, u.telefono, uo.rol, uo.fechaAsignacion) " +
            "FROM UsuarioObraEntity uo JOIN UsuarioEntity u ON uo.usuario.id = u.id WHERE uo.obra.id = :idObra")
    List<UsuarioObraListDTO> getUsuariosObraListByObraId(@Param("idObra") Long idObra);

    @Query("SELECT new usach.pingeso.badema.dtos.auth.UsuarioDTO(uo.usuario.correo, uo.usuario.contrasena, uo.rol) " +
            "FROM UsuarioObraEntity uo WHERE uo.obra.id = :idObra AND uo.usuario.id = :idUsuario")
    Optional<UsuarioDTO> getUsuarioYRolByObraIdAndUsuarioId(Long idObra, Long idUsuario);

    Optional<UsuarioObraEntity> findUsuarioObraEntityByObraIdAndUsuarioId(Long obraId, Long usuarioId);
}
