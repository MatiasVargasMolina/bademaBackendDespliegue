package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.entities.DetallePedidoEntity;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedidoEntity, Long> {
}
