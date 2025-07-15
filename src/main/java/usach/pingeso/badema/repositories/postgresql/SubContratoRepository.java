package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.obra.SubContratoDTO;
import usach.pingeso.badema.dtos.obra.SubContratoListDTO;
import usach.pingeso.badema.entities.SubContratoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubContratoRepository extends JpaRepository<SubContratoEntity, Long> {
    @Query("SELECT new usach.pingeso.badema.dtos.obra.SubContratoListDTO(sc.id, sc.nombre, sc.apellidos, sc.rut, sc.email, sc.telefono, sc.areaTrabajo) " +
            "FROM SubContratoEntity sc WHERE sc.obra.id = :idObra")
    List<SubContratoListDTO> getSubContratoEntitiesByObraId(@Param("idObra") Long idObra);

    @Query("SELECT new usach.pingeso.badema.dtos.obra.SubContratoDTO( sc.nombre, sc.apellidos, sc.rut, sc.email, sc.telefono, sc.areaTrabajo, sc.obra.id) " +
            "FROM SubContratoEntity sc WHERE sc.id = :id")
    SubContratoDTO getSubContratoDetalleById(@Param("id") Long id);

    Optional<SubContratoEntity> findByRut(String rut);
}
