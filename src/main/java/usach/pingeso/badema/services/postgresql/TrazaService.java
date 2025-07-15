package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.seguimiento.MaterialConOrdenDTO;
import usach.pingeso.badema.dtos.seguimiento.OrdenCompraTrazaDTO;
import usach.pingeso.badema.repositories.postgresql.DetalleOrdenCompraRepository;

import java.util.List;

@Service
public class TrazaService {
    private final MaterialService materialService;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;

    public TrazaService(MaterialService materialService, DetalleOrdenCompraRepository detalleOrdenCompraRepository) {
        this.materialService = materialService;
        this.detalleOrdenCompraRepository = detalleOrdenCompraRepository;
    }

    //Obtener lista de materiales con cantidad de órdenes asociadas
    public List<MaterialConOrdenDTO> getMaterialesConOrdenes(Long idObra) {
        return materialService.getMaterialesConOrdenes(idObra);
    }

    //Obtener la lista de órdenes para la parte derecha
    public List<OrdenCompraTrazaDTO> getOrdenesByMaterial(Long idObra, Long idMaterial) {
        List<OrdenCompraTrazaDTO> ordenes = detalleOrdenCompraRepository.getOrdenesByMaterial(idObra, idMaterial);

        ordenes.forEach(orden -> {
            String estado;
            if (orden.getCantidadEntregada() == 0) {
                estado = "Realizada";
            } else if (orden.getCantidadEntregada() < orden.getCantidadTotal()) {
                estado = "Parcialmente entregada";
            } else {
                estado = "Completada";
            }
            orden.setEstado(estado);
        });
        return ordenes;
    }
}