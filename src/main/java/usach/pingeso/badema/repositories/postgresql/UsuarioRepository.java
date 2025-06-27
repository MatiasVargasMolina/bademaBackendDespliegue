package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.obra.UsuarioListDTO;
import usach.pingeso.badema.entities.UsuarioEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByCorreo(String email);
    @Query("SELECT new usach.pingeso.badema.dtos.obra.UsuarioListDTO(u.id, u.nombre, u.apellido, u.correo, u.telefono) " +
            "FROM UsuarioEntity u "+
            "LEFT JOIN UsuarioObraEntity uo ON uo.usuario = u AND uo.obra.id = :idObra " +
            "WHERE uo.id IS NULL")
    List<UsuarioListDTO> findUsuariosNoRegistradosEnObra(@Param("idObra") Long idObra);
}
