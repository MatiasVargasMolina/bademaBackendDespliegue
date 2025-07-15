package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.pedido.PedidoMaterialDTO;
import usach.pingeso.badema.entities.DetallePedidoEntity;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.entities.PedidoEntity;
import usach.pingeso.badema.repositories.postgresql.DetallePedidoRepository;

@Service
public class DetallePedidoService {
    private final DetallePedidoRepository detallePedidoRepository;
    private final PedidoService pedidoService;
    private final MaterialService materialService;

    public DetallePedidoService(DetallePedidoRepository detallePedido, PedidoService pedidoService, MaterialService materialService) {
        this.detallePedidoRepository = detallePedido;
        this.pedidoService = pedidoService;
        this.materialService = materialService;
    }

    private DetallePedidoEntity mapMaterialDTOToEntity(PedidoMaterialDTO dto, PedidoEntity pedido, MaterialEntity material) {
        DetallePedidoEntity detalle = new DetallePedidoEntity();
        detalle.setEstado(dto.getEstado());
        detalle.setComentarios(dto.getComentarios());
        detalle.setCantidad(dto.getCantidad());
        detalle.setPedido(pedido);
        detalle.setMaterial(material);
        return detalle;
    }

    public DetallePedidoEntity saveDetallePedidoWithIdPedidoAndIdMaterial(Long idPedido, PedidoMaterialDTO materialDTO, Long idMaterial){
        PedidoEntity pedido = pedidoService.getPedidoById(idPedido);
        MaterialEntity material = materialService.getMaterialById(idMaterial);
        return detallePedidoRepository.save(mapMaterialDTOToEntity(materialDTO, pedido, material));
    }
}
