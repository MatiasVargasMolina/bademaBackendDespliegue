package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.seguimiento.OrdenCompraTrazaDTO;
import usach.pingeso.badema.entities.DetalleOrdenCompraEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompraEntity, Long> {
    @Query("""
        SELECT new usach.pingeso.badema.dtos.seguimiento.OrdenCompraTrazaDTO(
            o.id,
            o.numeroOrden,
            p.nombreProveedor,
            p.direccionProveedor,
            d.cantidad,
            d.precioUnitario,
            COALESCE(SUM(r.cantidadRecibida), 0),
            COALESCE(i.cantidadInstalada, 0),
            o.fechaEmision,
            o.fechaEntrega
        )
        FROM DetalleOrdenCompraEntity d
        JOIN d.paridadProveedor pm
        JOIN pm.proveedor p
        JOIN d.ordenCompra o
        LEFT JOIN d.recepciones r
        LEFT JOIN d.inventario i
        WHERE pm.material.id = :idMaterial
          AND o.responsable.obra.id = :idObra
        GROUP BY o.id, o.numeroOrden, p.nombreProveedor, p.direccionProveedor,
                 d.cantidad, d.precioUnitario, o.fechaEmision, o.fechaEntrega, i.cantidadInstalada
    """)
    List<OrdenCompraTrazaDTO> getOrdenesByMaterial(@Param("idObra") Long idObra, @Param("idMaterial") Long idMaterial);

    @Query("""
        SELECT d
        FROM DetalleOrdenCompraEntity d
        WHERE d.ordenCompra.id = :idOrdenCompra
          AND d.paridadProveedor.material.id = :idMaterial
    """)
    Optional<DetalleOrdenCompraEntity> findDetalleByOrdenAndMaterial(
            @Param("idOrdenCompra") Long idOrdenCompra,
            @Param("idMaterial") Long idMaterial
    );

}