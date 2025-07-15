package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.entities.RecepcionMaterialEntity;

@Repository
public interface RecepcionMaterialRepository extends JpaRepository<RecepcionMaterialEntity, Long> {
}
