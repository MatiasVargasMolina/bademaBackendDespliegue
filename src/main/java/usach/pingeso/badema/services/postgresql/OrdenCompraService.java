package usach.pingeso.badema.services.postgresql;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.dtos.OrdenCompraCreateDTO;
import usach.pingeso.badema.dtos.obra.OrdenCompraListObraDTO;
import usach.pingeso.badema.entities.OrdenCompraEntity;
import usach.pingeso.badema.entities.UsuarioObraEntity;
import usach.pingeso.badema.repositories.postgresql.OrdenCompraRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenCompraService {
    final OrdenCompraRepository ordenCompraRepository;
    private final UsuarioObraService usuarioObraService;

    public OrdenCompraService(OrdenCompraRepository ordenCompraRepository, UsuarioObraService obraService) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.usuarioObraService = obraService;
    }

    private OrdenCompraEntity mapDTOCreateTOEntity(OrdenCompraCreateDTO dto, UsuarioObraEntity responsable) {
        OrdenCompraEntity orden = new OrdenCompraEntity();
        orden.setNumeroOrden(dto.getNumeroOrden());
        orden.setFechaEmision(LocalDate.now());
        orden.setFechaEntrega(dto.getFechaEntrega());
        orden.setEstado(dto.getEstado());
        orden.setResponsable(responsable);
        return orden;
    }

    public OrdenCompraEntity saveOrden(OrdenCompraCreateDTO orden){
        UsuarioObraEntity usuarioObra = usuarioObraService.getUsuarioObraById(orden.getIdResponsable());
        return ordenCompraRepository.save(mapDTOCreateTOEntity(orden, usuarioObra));
    }

    public List<OrdenCompraListObraDTO> getOrdenesCompraByIdObra(Long idObra){
        return ordenCompraRepository.getOrdenCompraByIdObra(idObra);
    }

    public OrdenCompraEntity getOrdenById(Long id){
        Optional<OrdenCompraEntity> orden = ordenCompraRepository.findById(id);
        if (orden.isPresent()) return orden.get();
        else throw new RuntimeException("Orden de compra no encontrada");
    }
}
