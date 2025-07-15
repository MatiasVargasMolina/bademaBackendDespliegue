package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import usach.pingeso.badema.entities.DetalleOrdenCompraEntity;
import usach.pingeso.badema.entities.InventarioEntity;
import usach.pingeso.badema.entities.RecepcionMaterialEntity;
import usach.pingeso.badema.repositories.postgresql.DetalleOrdenCompraRepository;
import usach.pingeso.badema.repositories.postgresql.RecepcionMaterialRepository;

import java.util.List;

@Service
public class RecepcionMaterialService {
    private final RecepcionMaterialRepository recepcionRepository;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    private final InventarioService inventarioService;

    public RecepcionMaterialService(RecepcionMaterialRepository recepcionRepository,
                                    DetalleOrdenCompraRepository detalleOrdenCompraRepository,
                                    InventarioService inventarioService) {
        this.recepcionRepository = recepcionRepository;
        this.detalleOrdenCompraRepository = detalleOrdenCompraRepository;
        this.inventarioService = inventarioService;
    }

    public RecepcionMaterialEntity saveRecepcion(Long idDetalleOrdenCompra, RecepcionMaterialEntity recepcion) {
        //Buscar el detalle de la orden de compra
        DetalleOrdenCompraEntity detalle = detalleOrdenCompraRepository.findById(idDetalleOrdenCompra)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de orden de compra no encontrado con id: " + idDetalleOrdenCompra));

        //Validar inventario
        InventarioEntity inventario = detalle.getInventario();
        if (inventario == null) { //Si no cuenta con inventario
            inventario = new InventarioEntity();  //Se crea un inventario
            inventario = inventarioService.saveInventario(inventario); //Se crea con datos iniciales
            detalle.setInventario(inventario); //Se realiza la relación con el detalle
            detalleOrdenCompraRepository.save(detalle); //Se actualiza el detalle de la orden de compra
        }

        //Asociar recepción al detalle
        recepcion.setDetalleOrdenCompra(detalle);

        //Se guarda la recepcion para tener los datos
        RecepcionMaterialEntity saved = recepcionRepository.save(recepcion);

        //Actualizar inventario acumulando la cantidad recibida
        inventario.setCantidad(inventario.getCantidad() + recepcion.getCantidadRecibida());
        inventarioService.updateInventario(inventario);

        return saved;
    }

    public List<RecepcionMaterialEntity> getRecepcionesByDetalleOrden(Long idDetalleOrdenCompra) {
        return recepcionRepository.findAll().stream()
                .filter(r -> r.getDetalleOrdenCompra().getId().equals(idDetalleOrdenCompra))
                .toList();
    }
}
