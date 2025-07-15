package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.entities.InventarioEntity;
import usach.pingeso.badema.repositories.postgresql.InventarioRepository;

import java.time.LocalDate;

@Service
public class InventarioService {
    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public InventarioEntity saveInventario(InventarioEntity inventario) {
        inventario.setFechaIngreso(LocalDate.now());
        inventario.setCantidad(0);
        inventario.setCantidadInstalada(0);
        inventario.setFechaUltimaModificacion(LocalDate.now());
        return inventarioRepository.save(inventario);
    }

    public InventarioEntity updateInventario(InventarioEntity inventario) {
        inventario.setFechaUltimaModificacion(LocalDate.now());
        return inventarioRepository.save(inventario);
    }

    public InventarioEntity getInventarioById(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventario no encontrado con id: " + id));
    }
}
