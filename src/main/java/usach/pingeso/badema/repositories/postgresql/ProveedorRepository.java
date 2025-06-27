package usach.pingeso.badema.repositories.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.dtos.adquisiciones.ProveedorDetalleDTO;
import usach.pingeso.badema.entities.ProveedorEntity;

import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Long> {
    @Query("SELECT new usach.pingeso.badema.dtos.adquisiciones.ProveedorDetalleDTO(p.id, p.nombreProveedor, p.rutProveedor, p.telefonoProveedor, p.nombreVendedor, p.telefonoVendedor, p.emailVendedor, p.direccionProveedor, p.condiciones, p.restricciones) " +
            "FROM ProveedorEntity p JOIN ProveedorMaterialEntity pm ON p.id = pm.proveedor.id WHERE pm.material.id = :idMaterial")
    List<ProveedorDetalleDTO> getProveedoresByMaterialId(@Param("idMaterial") Long idMaterial);
}
