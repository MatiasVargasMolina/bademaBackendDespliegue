package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;
import usach.pingeso.badema.dtos.adquisiciones.*;
import usach.pingeso.badema.dtos.ordencompra.ItemOrdenCompraDTO;
import usach.pingeso.badema.entities.DetallePedidoEntity;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.entities.PedidoEntity;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;
import usach.pingeso.badema.services.PedidosAdqService;
import usach.pingeso.badema.services.mongodb.EspecificacionesMaterialService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManejarAdquisicionesService {

    private final PedidoService pedidoService;
    private final PedidosAdqService pedidosAdqService;
    private final EspecificacionesMaterialService especificacionesMaterialService;
    private final ProveedorMaterialService proveedorMaterialService;

    public ManejarAdquisicionesService(PedidoService pedidoService,
                                       PedidosAdqService pedidosAdqService,
                                       EspecificacionesMaterialService especificacionesMaterialService,
                                       ProveedorMaterialService proveedorMaterialService){
        this.pedidoService = pedidoService;
        this.pedidosAdqService = pedidosAdqService;
        this.especificacionesMaterialService = especificacionesMaterialService;
        this.proveedorMaterialService = proveedorMaterialService;
    }

    public MaterialConProveedoresDTO obtenerMaterialDePedido(Long idPedido, Long idMaterial) {
        PedidoEntity pedido = pedidoService.getPedidoById(idPedido);

        DetallePedidoEntity detalle = pedido.getDetallesPedido().stream()
                .filter(d -> d.getMaterial().getId().equals(idMaterial))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Material no pertenece al pedido"));

        MaterialEntity material = detalle.getMaterial();

        MaterialConProveedoresDTO dto = new MaterialConProveedoresDTO();
        dto.setIdMaterial(material.getId());
        dto.setNombreMaterial(material.getNombre());
        dto.setCantidad(detalle.getCantidad());

        //Se obtienen las especificaciones tecnicas del material
        EspecificacionMaterialDocument detalleMongo =
                especificacionesMaterialService.getEspecificacionesByMaterialId(material.getId());
        dto.setEspecificacionesMaterial(detalleMongo);

        List<ProveedorMaterialEntity> proveedorMateriales =
                proveedorMaterialService.getProveedoresPorMaterialId(material.getId());

        List<ManejarAdquisicionesDTO> proveedores = proveedorMateriales.stream().map(pm -> {
            ManejarAdquisicionesDTO proveedorDTO = new ManejarAdquisicionesDTO();
            proveedorDTO.setIdProveedor(pm.getProveedor().getId());
            proveedorDTO.setNombreProveedor(pm.getProveedor().getNombreProveedor());
            proveedorDTO.setCondiciones(pm.getProveedor().getCondiciones());
            proveedorDTO.setPrecio(pm.getPrecio());
            proveedorDTO.setRestricciones(pm.getProveedor().getRestricciones());
            proveedorDTO.setComentarios(pm.getComentarios());
            return proveedorDTO;
        }).collect(Collectors.toList());

        dto.setProveedores(proveedores);
        return dto;
    }

    public List<PedidoListAdqDTO> obtenerPedidosPorObra(Long idObra) {
        return pedidosAdqService.getListPedidosAdqByObraId(idObra);
    }

    public List<MaterialConCantidadTotalDTO> getMaterialesProveedorPorObra(Long idProveedor, Long idObra, Long materialId) {
        List<PedidoEntity> pedidos = pedidoService.getPedidosEntityConMaterialesByObraId(idObra);
        List<ProveedorMaterialEntity> asociaciones = proveedorMaterialService.getMaterialesByProveedorId(idProveedor);

        Set<Long> idsMaterialesProveedor = asociaciones.stream()
                .map(pm -> pm.getMaterial().getId())
                .collect(Collectors.toSet());

        Map<Long, MaterialConCantidadTotalDTO> materialesMap = new HashMap<>();

        for (PedidoEntity pedido : pedidos) {
            for (DetallePedidoEntity detalle : pedido.getDetallesPedido()) {
                MaterialEntity material = detalle.getMaterial();
                Long idMaterial = material.getId();

                if (idsMaterialesProveedor.contains(idMaterial)) {
                    MaterialConCantidadTotalDTO existente = materialesMap.get(idMaterial);

                    if (existente == null) {
                        MaterialConCantidadTotalDTO nuevo = new MaterialConCantidadTotalDTO();
                        nuevo.setIdMaterial(idMaterial);
                        nuevo.setNombre(material.getNombre());
                        nuevo.setCantidad(detalle.getCantidad());
                        materialesMap.put(idMaterial, nuevo);
                    } else {
                        existente.setCantidad(existente.getCantidad() + detalle.getCantidad());
                    }
                }
            }
        }

        // Convertir a lista y ordenar el material seleccionado al principio
        List<MaterialConCantidadTotalDTO> lista = new ArrayList<>(materialesMap.values());
        lista.sort(Comparator.comparing(dto -> !dto.getIdMaterial().equals(materialId)));
        return lista;
    }

    public ProveedorMaterialDetalleDTO getDetalleProveedorMaterial(Long idProveedor, Long idMaterial) {
        ProveedorMaterialEntity entidad = proveedorMaterialService.getByProveedorAndMaterial(idProveedor, idMaterial);

        ProveedorMaterialDetalleDTO dto = new ProveedorMaterialDetalleDTO();
        dto.setIdMaterial(entidad.getMaterial().getId());
        dto.setNombreMaterial(entidad.getMaterial().getNombre());
        dto.setIdProveedor(entidad.getProveedor().getId());
        dto.setNombreProveedor(entidad.getProveedor().getNombreProveedor());
        dto.setCondicion(entidad.getProveedor().getCondiciones());
        dto.setPrecio(entidad.getPrecio());
        dto.setRestricciones(entidad.getProveedor().getRestricciones());
        dto.setComentarios(entidad.getComentarios());

        //Se obtienen las nuevas especificaciones tecnicas del material-proveedor
        /*EspecificacionMaterialDocument detalleMongo =
                especificacionesMaterialService.getEspecificacionesByMaterialId(material.getId());
        dto.setNuevasEspecificaciones(detalleMongo);*/

        return dto;
    }

    public ItemOrdenCompraDTO construirItemOrdenCompra(Long idProveedor, Long idMaterial, int cantidad, String observaciones) {
        ProveedorMaterialEntity relacion = proveedorMaterialService.getByProveedorAndMaterial(idProveedor, idMaterial);
        MaterialEntity material = relacion.getMaterial();

        ItemOrdenCompraDTO dto = new ItemOrdenCompraDTO();
        dto.setIdMaterial(material.getId());
        dto.setNombreMaterial(material.getNombre());
        dto.setCantidad(cantidad);
        dto.setPrecioTotal(cantidad * relacion.getPrecio());
        dto.setObservaciones(observaciones);

        return dto;
    }
}
