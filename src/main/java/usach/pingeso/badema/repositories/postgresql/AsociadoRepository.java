package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.obra.AsociadosDTO;
import usach.pingeso.badema.dtos.obra.AsociadosListDTO;
import usach.pingeso.badema.entities.AsociadosEntity;

import java.util.List;

@Repository
public interface AsociadoRepository extends JpaRepository<AsociadosEntity, Long> {
    @Query("SELECT new usach.pingeso.badema.dtos.obra.AsociadosListDTO(a.id, a.nombre, a.apellidos, a.rut, a.email, a.telefono, a.rol) " +
            "FROM AsociadosEntity a WHERE a.obra.id = :idObra")
    List<AsociadosListDTO> getAsociadosListDTOByObraId(@Param("idObra") Long idObra);

    @Query("SELECT new usach.pingeso.badema.dtos.obra.AsociadosDTO( sc.nombre,  sc.apellidos, sc.rut, sc.email, sc.telefono, sc.rol, sc.obra.id) " +
            "FROM AsociadosEntity sc WHERE sc.id = :id")
    AsociadosDTO getAsociadoDetalleById(@Param("id") Long id);
}
