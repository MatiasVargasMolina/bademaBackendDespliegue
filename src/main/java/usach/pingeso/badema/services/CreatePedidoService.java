package usach.pingeso.badema.services;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.pedido.PedidoCreateDTO;
import usach.pingeso.badema.dtos.pedido.PedidoMaterialDTO;
import usach.pingeso.badema.entities.DetallePedidoEntity;
import usach.pingeso.badema.entities.MaterialEntity;
import usach.pingeso.badema.entities.PedidoEntity;
import usach.pingeso.badema.services.mongodb.EspecificacionesMaterialService;
import usach.pingeso.badema.services.postgresql.DetallePedidoService;
import usach.pingeso.badema.services.postgresql.MaterialService;
import usach.pingeso.badema.services.postgresql.PedidoService;

@Service
public class CreatePedidoService {
    private final PedidoService pedidoService;
    private final DetallePedidoService detallePedidoService;
    private final MaterialService materialService;
    private final EspecificacionesMaterialService especificacionesMaterialService;

    public CreatePedidoService(PedidoService pedidoService, DetallePedidoService detallePedidoService, MaterialService materialService, EspecificacionesMaterialService especificacionesMaterialService) {
        this.pedidoService = pedidoService;
        this.detallePedidoService = detallePedidoService;
        this.materialService = materialService;
        this.especificacionesMaterialService = especificacionesMaterialService;
    }

    public PedidoCreateDTO savePedidoWithEverything(PedidoCreateDTO pedidoCreateDTO) {
        try {
            // Crear el pedido
            PedidoEntity pedido = pedidoService.savePedido(pedidoCreateDTO);
            if (pedido == null || pedido.getId() == null) {
                throw new IllegalStateException("No se pudo crear el pedido.");
            }

            // Guardar los materiales y sus detalles
            for (PedidoMaterialDTO pedidoMaterial : pedidoCreateDTO.getMateriales()) {
                // Guardar material
                MaterialEntity material = materialService.saveMaterial(pedidoMaterial.getNombreMaterial());
                if (material == null || material.getId() == null) {
                    throw new IllegalStateException("No se pudo crear el material: " + pedidoMaterial.getNombreMaterial());
                }

                // Guardar especificaciones
                boolean exitoso = especificacionesMaterialService.insertarEspecificaciones(material.getId(), pedidoMaterial.getEspecificaciones());
                if (!exitoso) {
                    throw new IllegalStateException("No se pudieron guardar las especificaciones para el material: " + material.getId());
                }

                // Guardar detalle del pedido
                DetallePedidoEntity detallePedido = detallePedidoService.saveDetallePedidoWithIdPedidoAndIdMaterial(pedido.getId(), pedidoMaterial, material.getId());
                if (detallePedido == null || detallePedido.getId() == null) {
                    throw new IllegalStateException("No se pudo crear el detalle del pedido para el material: " + material.getId());
                }
            }

            return pedidoCreateDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el pedido completo: " + e.getMessage(), e);
        }
    }

}
