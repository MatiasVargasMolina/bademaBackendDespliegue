package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.adquisiciones.PedidoMaterialAdqDTO;
import usach.pingeso.badema.dtos.obra.PedidosATrabajarListDTO;
import usach.pingeso.badema.dtos.pedido.PedidoCreateDTO;
import usach.pingeso.badema.dtos.pedido.PedidoDetallePopDTO;
import usach.pingeso.badema.entities.PedidoEntity;
import usach.pingeso.badema.entities.UsuarioObraEntity;
import usach.pingeso.badema.repositories.postgresql.PedidoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    final PedidoRepository pedidoRepository;
    private final UsuarioObraService usuarioObraService;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioObraService obraService){
        this.pedidoRepository = pedidoRepository;
        this.usuarioObraService = obraService;
    }

    private PedidoEntity mapDTOCreateTOEntity(PedidoCreateDTO dto, UsuarioObraEntity responsable){
        PedidoEntity pedido = new PedidoEntity();
        pedido.setFechaPedido(LocalDate.now());
        pedido.setEstado(dto.getEstado());
        pedido.setFechaEstimadaLlegada(dto.getFechaEstimadaLlegada());
        pedido.setNombre(dto.getNombre());
        pedido.setResponsable(responsable);
        return pedido;
    }

    public PedidoEntity savePedido(PedidoCreateDTO pedido){
        UsuarioObraEntity usuarioObra = usuarioObraService.getUsuarioObraByObraAndUsuarioId(pedido.getIdObra() ,pedido.getIdResponsable());
        return pedidoRepository.save(mapDTOCreateTOEntity(pedido, usuarioObra));
    }

    public List<PedidosATrabajarListDTO> getPedidosATrabajar (Long idObra){
        return pedidoRepository.getPedidosAAtrabajar(idObra);
    }

    public List<PedidoEntity> getPedidosEntityConMaterialesByObraId(Long idObra) {
        return pedidoRepository.findPedidosByObraId(idObra);
    }

    public List<PedidoMaterialAdqDTO> getPedidosConMaterialesByObraId(Long idObra){
        return pedidoRepository.getPedidosConMaterialesByObraId(idObra);
    }

    public PedidoEntity getPedidoById(Long id){
        Optional<PedidoEntity> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) return pedido.get();
        else throw new RuntimeException("Pedido no encontrado");
    }

    public PedidoDetallePopDTO getDetallePedidoPop(Long idPedido) {
        PedidoEntity pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoDetallePopDTO dto = new PedidoDetallePopDTO();
        dto.setId(pedido.getId());
        dto.setNombre(pedido.getNombre());
        dto.setEstado(pedido.getEstadoDescripcion());
        dto.setMotivoRechazo(pedido.getMotivoRechazo());

        String nombreCompleto = pedido.getResponsable().getUsuario().getNombre() + " " +
                pedido.getResponsable().getUsuario().getApellido();
        dto.setResponsable(nombreCompleto);
        dto.setFechaCreacion(pedido.getFechaPedido());
        dto.setFechaEstimadaLlegada(pedido.getFechaEstimadaLlegada());

        return dto;
    }
}
