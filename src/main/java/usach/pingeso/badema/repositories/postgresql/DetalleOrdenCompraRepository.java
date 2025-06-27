package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.entities.DetalleOrdenCompraEntity;

@Repository
public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompraEntity, Long> {
}
