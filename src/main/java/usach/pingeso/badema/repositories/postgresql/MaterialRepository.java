package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.seguimiento.MaterialConOrdenDTO;
import usach.pingeso.badema.entities.MaterialEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<MaterialEntity, Long> {
    Optional<MaterialEntity> findByNombre(String nombre);

    @Query("""
        SELECT new usach.pingeso.badema.dtos.seguimiento.MaterialConOrdenDTO(
          m.id,
          m.nombre,
          COUNT(o.id)
        )
        FROM DetalleOrdenCompraEntity d
        JOIN d.paridadProveedor pm
        JOIN pm.material m
        JOIN d.ordenCompra o
        WHERE o.responsable.obra.id = :idObra
        GROUP BY m.id, m.nombre
        """)
    List<MaterialConOrdenDTO> getMaterialesConOrdenes(@Param("idObra") Long idObra);
}
