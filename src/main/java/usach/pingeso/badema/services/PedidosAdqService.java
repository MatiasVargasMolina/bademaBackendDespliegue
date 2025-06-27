package usach.pingeso.badema.services;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;
import usach.pingeso.badema.dtos.adquisiciones.*;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.services.mongodb.EspecificacionesMaterialService;
import usach.pingeso.badema.services.postgresql.MaterialService;
import usach.pingeso.badema.services.postgresql.PedidoService;
import usach.pingeso.badema.services.postgresql.ProveedorService;

import java.util.*;

@Service
public class PedidosAdqService {
    private
    final PedidoService pedidoService;
    final MaterialService materialService;
    final ProveedorService proveedorService;
    final EspecificacionesMaterialService especificacionesMaterialService;

    public PedidosAdqService(PedidoService pedidoService, MaterialService materialService, ProveedorService proveedorService, EspecificacionesMaterialService especificacionesMaterialService) {
        this.pedidoService = pedidoService;
        this.materialService = materialService;
        this.proveedorService = proveedorService;
        this.especificacionesMaterialService = especificacionesMaterialService;
    }

    public List<PedidoListAdqDTO> getListPedidosAdqByObraId(Long idObra) {
        List<PedidoMaterialAdqDTO> flatList = pedidoService.getPedidosConMaterialesByObraId(idObra);

        // Mapa para agrupar materiales por pedido
        Map<Long, PedidoListAdqDTO> pedidosMap = new LinkedHashMap<>();

        for (PedidoMaterialAdqDTO item : flatList) {
            PedidoListAdqDTO pedidoDTO = pedidosMap.computeIfAbsent(item.getPedidoId(), id -> {
                PedidoListAdqDTO nuevoPedido = new PedidoListAdqDTO();
                nuevoPedido.setId(id);
                nuevoPedido.setNombre(item.getPedidoNombre());
                nuevoPedido.setNombreResponsable(item.getNombreResponsable());
                nuevoPedido.setApellidoResponsable(item.getApellidoResponsable());
                nuevoPedido.setFechaEsperada(item.getFechaEsperada());
                nuevoPedido.setMateriales(new ArrayList<>());
                return nuevoPedido;
            });

            MaterialListAdqDTO materialDTO = new MaterialListAdqDTO();
            materialDTO.setId(item.getMaterialId());
            materialDTO.setNombre(item.getMaterialNombre());
            materialDTO.setProveedoresAsignados(item.getProveedoresAsignados());

            pedidoDTO.getMateriales().add(materialDTO);
        }
        return new ArrayList<>(pedidosMap.values());
    }

    public MaterialDetalleDTO getMaterialByIdAdq(Long idMaterial){
        MaterialDetalleDTO dto = new MaterialDetalleDTO();

        // Obtener material
        MaterialEntity material = materialService.getMaterialById(idMaterial);
        dto.setId(material.getId());
        dto.setNombre(material.getNombre());

        // Obtener especificaciones
        EspecificacionMaterialDocument especificacionMaterialDocument = especificacionesMaterialService.getEspecificacionesByMaterialId(idMaterial);
        dto.setEspecificacionesMaterial(especificacionMaterialDocument);

        // Obtener proveedores asociados
        List<ProveedorDetalleDTO> proveedores = proveedorService.getProveedoresMaterialByMaterialId(idMaterial);
        dto.setProveedores(proveedores);
        return dto;
    }
}
