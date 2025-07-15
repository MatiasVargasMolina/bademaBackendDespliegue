package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProveedorMaterialRepository extends JpaRepository<ProveedorMaterialEntity, Long> {
    Optional<ProveedorMaterialEntity> findByProveedorIdAndMaterialId(Long proveedorId, Long materialId);
    List<ProveedorMaterialEntity> findByMaterialId(Long idMaterial);
    List<ProveedorMaterialEntity> findByProveedorId(Long idProveedor);
    void deleteByProveedorIdAndMaterialId(Long idProveedor, Long idMaterial);
}