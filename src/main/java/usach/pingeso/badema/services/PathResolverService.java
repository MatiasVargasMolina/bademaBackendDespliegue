package usach.pingeso.badema.services;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.entities.*;
import usach.pingeso.badema.repositories.postgresql.*;

@Service
public class PathResolverService {

    private final PedidoRepository pedidoRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    private final RecepcionMaterialRepository recepcionMaterialRepository;

    public PathResolverService(
            PedidoRepository pedidoRepository,
            OrdenCompraRepository ordenCompraRepository,
            DetalleOrdenCompraRepository detalleOrdenCompraRepository,
            RecepcionMaterialRepository recepcionMaterialRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.ordenCompraRepository = ordenCompraRepository;
        this.detalleOrdenCompraRepository = detalleOrdenCompraRepository;
        this.recepcionMaterialRepository = recepcionMaterialRepository;
    }

    // Obra ya está, no es necesario colocarlo acá
    // uploads/obra/{id}

    // uploads/obra_{id]/pedido_{id}
    public String resolvePathForPedido(long idPedido) {
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return "obra_" + pedido.getResponsable().getObra().getId() + "/pedido_" + idPedido;
    }

    // uploads/obra_{id]/ordenCompra_{id}
    public String resolvePathForOrdenCompra(long idOrdenCompra) {
        OrdenCompraEntity orden = ordenCompraRepository.findById(idOrdenCompra)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return "obra_" + orden.getResponsable().getObra().getId() + "/ordencompra_" + idOrdenCompra;
    }

    // uploads/obra_{id]/ordenCompra_{id}/detalleOC_{id}
    public String resolvePathForDetalle(long idDetalle) {
        DetalleOrdenCompraEntity detalle = detalleOrdenCompraRepository.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
        OrdenCompraEntity orden = detalle.getOrdenCompra();

        return "obra_" + orden.getResponsable().getObra().getId()
                + "/ordencompra_" + orden.getId()
                + "/detalle_" + idDetalle;
    }

    // uploads/obra_{id]/ordenCompra_{id}/detalleOC_{id}/recepcion_material_{id}
    public String resolvePathForRecepcion(long idRecepcion) {
        RecepcionMaterialEntity recepcion = recepcionMaterialRepository.findById(idRecepcion)
                .orElseThrow(() -> new RuntimeException("Recepción no encontrada"));
        DetalleOrdenCompraEntity detalle = recepcion.getDetalleOrdenCompra();
        OrdenCompraEntity orden = detalle.getOrdenCompra();
        return "obra_" + orden.getResponsable().getObra().getId()
                + "/ordencompra_" + orden.getId()
                + "/detalle_" + detalle.getId()
                + "/recepcion_material_" + idRecepcion;
    }
}

