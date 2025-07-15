package usach.pingeso.badema.services.postgresql;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;
import usach.pingeso.badema.dtos.ordencompra.ItemOrdenCompraDTO;
import usach.pingeso.badema.dtos.seguimiento.ActualizarSeguimientoDetalleDTO;
import usach.pingeso.badema.dtos.seguimiento.OrdenCompraSeguimientoDTO;
import usach.pingeso.badema.dtos.seguimiento.SeguimientoDetalleOrdenDTO;
import usach.pingeso.badema.entities.*;
import usach.pingeso.badema.repositories.postgresql.DetalleOrdenCompraRepository;
import usach.pingeso.badema.services.mongodb.NuevasEspecificacionesService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeguimientoService {
    private final OrdenCompraService ordenCompraService;
    private final DetalleOrdenCompraRepository detalleOrdenCompraRepository;
    private final RecepcionMaterialService recepcionMaterialService;
    private final InventarioService inventarioService;
    private final NuevasEspecificacionesService nuevasEspecificacionesService;

    public SeguimientoService(OrdenCompraService ordenCompraService,
                              DetalleOrdenCompraRepository detalleOrdenCompraRepository,
                              RecepcionMaterialService recepcionMaterialService,
                              InventarioService inventarioService,
                              NuevasEspecificacionesService nuevasEspecificacionesService) {
        this.ordenCompraService = ordenCompraService;
        this.detalleOrdenCompraRepository = detalleOrdenCompraRepository;
        this.recepcionMaterialService = recepcionMaterialService;
        this.inventarioService = inventarioService;
        this.nuevasEspecificacionesService = nuevasEspecificacionesService;
    }

    //Obtener la lista de ordenes de compra en la parte izquierda del seguimiento
    public List<OrdenCompraSeguimientoDTO> getOrdenesSeguimiento(Long idObra) {
        List<OrdenCompraEntity> ordenes = ordenCompraService.getOrdenesEntityByIdObra(idObra);
        List<OrdenCompraSeguimientoDTO> resultado = new ArrayList<>();

        for (OrdenCompraEntity orden : ordenes) {
            OrdenCompraSeguimientoDTO dto = new OrdenCompraSeguimientoDTO();
            dto.setId(orden.getId());
            dto.setNumeroOrden(orden.getNumeroOrden());
            dto.setFechaEmision(orden.getFechaEmision());
            dto.setEstado(orden.getEstadoDescripcion());

            String responsable = orden.getResponsable().getUsuario().getNombre() + " "
                    + orden.getResponsable().getUsuario().getApellido()
                    + " (" + orden.getResponsable().getRol() + ")";
            dto.setResponsable(responsable);

            if (!orden.getDetalles().isEmpty()) {
                dto.setNombreProveedor(
                        orden.getDetalles().get(0).getParidadProveedor().getProveedor().getNombreProveedor()
                );
            } else {
                dto.setNombreProveedor("Error");
            }

            List<ItemOrdenCompraDTO> items = new ArrayList<>();
            for (DetalleOrdenCompraEntity detalle : orden.getDetalles()) {
                ItemOrdenCompraDTO item = new ItemOrdenCompraDTO();
                item.setIdMaterial(detalle.getParidadProveedor().getMaterial().getId());
                item.setNombreMaterial(detalle.getParidadProveedor().getMaterial().getNombre());
                item.setCantidad(detalle.getCantidad());
                item.setTotal(detalle.getCantidad() * detalle.getPrecioUnitario());
                item.setObservaciones(detalle.getObservacion());
                items.add(item);
            }
            dto.setItems(items);

            resultado.add(dto);
        }

        return resultado;
    }

    //Obtener el detalle de seguimiento de un item de orden de compra para la vista derecha
    public SeguimientoDetalleOrdenDTO getDetalleOrdenSeguimiento(Long idOrdenCompra, Long idMaterial) {
        //Se obtiene el detalle de la orden
        DetalleOrdenCompraEntity detalle = detalleOrdenCompraRepository
                .findDetalleByOrdenAndMaterial(idOrdenCompra, idMaterial)
                .orElseThrow(() -> new EntityNotFoundException("Detalle no encontrado"));

        //Se obtiene la paridad material-proveedor, el material y el proveedor
        ProveedorMaterialEntity paridad = detalle.getParidadProveedor();
        MaterialEntity material = paridad.getMaterial();
        ProveedorEntity proveedor = paridad.getProveedor();

        //Total entregado (sumar recepciones)
        int entregado = detalle.getRecepciones() != null
                ? detalle.getRecepciones().stream()
                .mapToInt(RecepcionMaterialEntity::getCantidadRecibida)
                .sum()
                : 0;

        //Total instalado (se revisa del inventario)
        InventarioEntity inventario = detalle.getInventario();
        int instalado = (inventario != null) ? inventario.getCantidadInstalada() : 0;

        //Estado
        String estado;
        if (entregado == 0) {
            estado = "Realizada";
        } else if (entregado < detalle.getCantidad()) {
            estado = "Parcialmente entregada";
        } else if (entregado == detalle.getCantidad()) {
            estado = "Completada";
        } else {
            estado = "Exceso recibido";
        }

        //Nuevas especificaciones del proveedor con el material
        NuevasEspecificacionesDocument esp = nuevasEspecificacionesService
                .getNuevasEspecificacionesByProveedorMaterialId(paridad.getId());

        SeguimientoDetalleOrdenDTO dto = new SeguimientoDetalleOrdenDTO();
        dto.setIdDetalleOrden(detalle.getId());
        dto.setNombreMaterial(material.getNombre());
        dto.setNombreProveedor(proveedor.getNombreProveedor());
        dto.setCantidadOrdenada(detalle.getCantidad());
        dto.setPrecioTotal(detalle.getCantidad() * detalle.getPrecioUnitario());
        dto.setEstado(estado);
        dto.setFechaCompra(detalle.getOrdenCompra().getFechaEmision());
        dto.setFechaEstimadaEntrega(detalle.getOrdenCompra().getFechaEntrega());
        dto.setCantidadEntregada(entregado);
        dto.setCantidadInstalada(instalado);
        dto.setEspecificaciones(esp);

        return dto;
    }

    //Actualizar (o registrar) una nueva recepción en el seguimiento
    @Transactional
    public void actualizarDetalleOrdenSeguimiento(Long idOrdenCompra, Long idMaterial, ActualizarSeguimientoDetalleDTO datos) {
        //Se obtiene el detalle de la orden
        DetalleOrdenCompraEntity detalle = detalleOrdenCompraRepository
                .findDetalleByOrdenAndMaterial(idOrdenCompra, idMaterial)
                .orElseThrow(() -> new EntityNotFoundException("Detalle no encontrado"));

        //Buscar o crear inventario
        InventarioEntity inventario = detalle.getInventario();
        if (inventario == null) {
            inventario = inventarioService.saveInventario(new InventarioEntity());
            detalle.setInventario(inventario);
            detalleOrdenCompraRepository.save(detalle);
        }

        //Registrar la recepción
        RecepcionMaterialEntity recepcion = new RecepcionMaterialEntity();
        recepcion.setDetalleOrdenCompra(detalle);
        recepcion.setFechaRecepcion(LocalDate.now());
        recepcion.setCantidadRecibida(datos.getCantidadEntregada());
        recepcion.setIncidencias(datos.getIncidencias());
        recepcionMaterialService.saveRecepcion(detalle.getId(),recepcion);

        //Actualizar inventario acumulado
        inventario.setCantidad(inventario.getCantidad() + datos.getCantidadEntregada());
        inventario.setCantidadInstalada(inventario.getCantidadInstalada() + datos.getCantidadInstalada());
        inventarioService.updateInventario(inventario);

        //Verificar si todos los detalles de la orden ya están completos
        OrdenCompraEntity orden = detalle.getOrdenCompra();
        boolean todosCompletos = orden.getDetalles().stream()
                .allMatch(d -> {
                    int cantidadEntregada = d.getRecepciones() != null
                            ? d.getRecepciones().stream().mapToInt(RecepcionMaterialEntity::getCantidadRecibida).sum()
                            : 0;
                    return cantidadEntregada == d.getCantidad();
                });

        if (todosCompletos) {
            orden.setEstado(1); //Completada
            ordenCompraService.updateOrdenCompra(orden);
        }
    }

}
