package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.obra.OrdenCompraListObraDTO;
import usach.pingeso.badema.entities.OrdenCompraEntity;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompraEntity, Long> {
    @Query("SELECT new usach.pingeso.badema.dtos.obra.OrdenCompraListObraDTO(o.id, o.numeroOrden, o.fechaEmision, o.estado) " +
            "FROM OrdenCompraEntity o WHERE o.responsable.obra.id = :idObra")
    List<OrdenCompraListObraDTO> getOrdenCompraByIdObra(Long idObra);

    @Query("""
        SELECT o FROM OrdenCompraEntity o
        WHERE o.responsable.obra.id = :idObra
    """)
    List<OrdenCompraEntity> getOrdenesEntityByIdObra(@Param("idObra") Long idObra);

}
