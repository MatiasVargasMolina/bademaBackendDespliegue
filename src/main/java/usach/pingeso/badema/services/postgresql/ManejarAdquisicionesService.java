package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;
import usach.pingeso.badema.dtos.adquisiciones.*;
import usach.pingeso.badema.entities.DetallePedidoEntity;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.entities.PedidoEntity;
import usach.pingeso.badema.entities.ProveedorMaterialEntity;
import usach.pingeso.badema.services.PedidosAdqService;
import usach.pingeso.badema.services.mongodb.EspecificacionesMaterialService;
import usach.pingeso.badema.services.mongodb.NuevasEspecificacionesService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManejarAdquisicionesService {

    private final PedidoService pedidoService;
    private final PedidosAdqService pedidosAdqService;
    private final EspecificacionesMaterialService especificacionesMaterialService;
    private final ProveedorMaterialService proveedorMaterialService;
    private final NuevasEspecificacionesService nuevasEspecificacionesService;

    public ManejarAdquisicionesService(PedidoService pedidoService,
                                       PedidosAdqService pedidosAdqService,
                                       EspecificacionesMaterialService especificacionesMaterialService,
                                       ProveedorMaterialService proveedorMaterialService, NuevasEspecificacionesService nuevasEspecificacionesService){
        this.pedidoService = pedidoService;
        this.pedidosAdqService = pedidosAdqService;
        this.especificacionesMaterialService = especificacionesMaterialService;
        this.proveedorMaterialService = proveedorMaterialService;
        this.nuevasEspecificacionesService = nuevasEspecificacionesService;
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

            //Agregar las nuevas especificaciones de la relación proveedor-material (Mongo)
            NuevasEspecificacionesDocument especs =
                    nuevasEspecificacionesService.getNuevasEspecificacionesByProveedorMaterialId(pm.getId());
            proveedorDTO.setNuevasEspecificaciones(especs);
            return proveedorDTO;
        }).collect(Collectors.toList());

        dto.setProveedores(proveedores);
        return dto;
    }

    public List<PedidoListAdqDTO> obtenerPedidosPorObra(Long idObra) {
        return pedidosAdqService.getListPedidosAdqByObraId(idObra);
    }

    public List<MaterialProveedorPedidoDTO> getMaterialesProveedorPorObra(Long idObra, Long idProveedor) {
        List<PedidoEntity> pedidosObra = pedidoService.getPedidosEntityConMaterialesByObraId(idObra);
        List<MaterialProveedorPedidoDTO> resultado = new ArrayList<>();

        pedidosObra.forEach(p -> {
            p.getDetallesPedido().forEach(d -> {
                //Verificar que el proveedor está relacionado con el material
                boolean proveedorRelacionado = d.getMaterial().getParidadProveedor()
                        .stream()
                        .anyMatch(pm -> pm.getProveedor().getId().equals(idProveedor));

                if (proveedorRelacionado) {
                    MaterialProveedorPedidoDTO dto = new MaterialProveedorPedidoDTO();
                    dto.setIdPedido(p.getId());
                    dto.setNombrePedido(p.getNombre());
                    dto.setIdMaterial(d.getMaterial().getId());
                    dto.setNombreMaterial(d.getMaterial().getNombre());
                    dto.setCantidadRequerida(d.getCantidad());
                    dto.setCantidadFaltante(d.getCantidad());
                    resultado.add(dto);
                }
            });
        });
        return resultado;
    }

    public ProveedorMaterialDetalleDTO getDetalleProveedorMaterial(Long idProveedor, Long idMaterial) {
        ProveedorMaterialEntity entidad = proveedorMaterialService.getByProveedorAndMaterial(idProveedor, idMaterial);

        NuevasEspecificacionesDocument esp = nuevasEspecificacionesService.getNuevasEspecificacionesByProveedorMaterialId(entidad.getId());

        ProveedorMaterialDetalleDTO dto = new ProveedorMaterialDetalleDTO();
        dto.setIdMaterial(entidad.getMaterial().getId());
        dto.setNombreMaterial(entidad.getMaterial().getNombre());
        dto.setIdProveedor(entidad.getProveedor().getId());
        dto.setNombreProveedor(entidad.getProveedor().getNombreProveedor());
        dto.setCondicion(entidad.getProveedor().getCondiciones());
        dto.setPrecio(entidad.getPrecio());
        dto.setRestricciones(entidad.getProveedor().getRestricciones());
        dto.setComentarios(entidad.getComentarios());
        dto.setNuevasEspecificaciones(esp);

        return dto;
    }
}
