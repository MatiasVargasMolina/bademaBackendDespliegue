package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.adquisiciones.PedidoMaterialAdqDTO;
import usach.pingeso.badema.dtos.obra.PedidosATrabajarListDTO;
import usach.pingeso.badema.entities.PedidoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long> {
    @Query("SELECT new usach.pingeso.badema.dtos.obra.PedidosATrabajarListDTO(p.id, p.nombre, p.estado, p.fechaPedido) "
            +
            "FROM PedidoEntity p WHERE p.responsable.obra.id = :idObra")
    List<PedidosATrabajarListDTO> getPedidosAAtrabajar(Long idObra);

    @Query("""
                SELECT new usach.pingeso.badema.dtos.adquisiciones.PedidoMaterialAdqDTO(
                    p.id,
                    p.nombre,
                    p.responsable.usuario.nombre,
                    p.responsable.usuario.apellido,
                    p.fechaEstimadaLlegada,
                    m.id,
                    m.nombre,
                    COUNT(pm.proveedor.id)
                )
                FROM PedidoEntity p
                JOIN p.detallesPedido d
                JOIN d.material m
                LEFT JOIN m.paridadProveedor pm
                WHERE p.responsable.obra.id = :idObra
                GROUP BY
                    p.id,
                    p.nombre,
                    p.responsable.usuario.nombre,
                    p.responsable.usuario.apellido,
                    p.fechaEstimadaLlegada,
                    m.id,
                    m.nombre
            """)
    List<PedidoMaterialAdqDTO> getPedidosConMaterialesByObraId(@Param("idObra") Long idObra);

    @Query("""
        SELECT DISTINCT p
        FROM PedidoEntity p
        WHERE p.responsable.obra.id = :idObra
    """)
    List<PedidoEntity> findPedidosByObraId(@Param("idObra") Long idObra);

    Optional<PedidoEntity> findByNombre(String nombre);
}
