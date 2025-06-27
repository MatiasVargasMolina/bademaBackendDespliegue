package usach.pingeso.badema.services;

import org.springframework.stereotype.Service;
import usach.pingeso.badema.documents.HitosObraDocument;
import usach.pingeso.badema.dtos.obra.*;
import usach.pingeso.badema.services.mongodb.HitosObraService;
import usach.pingeso.badema.services.postgresql.*;

import java.util.List;

@Service
public class ObraDetalleService {

    private final ObraService obraService;
    private final HitosObraService hitosObraService;
    private final PedidoService pedidoService;
    private final UsuarioObraService usuarioObraService;
    private final SubContratoService subContratoService;
    private final AsociadoService asociadoService;
    private final OrdenCompraService ordenCompraService;

    public ObraDetalleService(ObraService obraService,
                              HitosObraService hitosObraService,
                              PedidoService pedidoService, UsuarioObraService usuarioObraService, SubContratoService subContratoService, AsociadoService asociadoService, OrdenCompraService ordenCompraService) {
        this.obraService = obraService;
        this.hitosObraService = hitosObraService;
        this.pedidoService = pedidoService;
        this.usuarioObraService = usuarioObraService;
        this.subContratoService = subContratoService;
        this.asociadoService = asociadoService;
        this.ordenCompraService = ordenCompraService;
    }

    public ObraDetalleDTO obtenerDetalle(Long id) {
        ObraDetalleDTO obra = new ObraDetalleDTO();
        ObraListDTO obraMini = obraService.getObraById(id);
        if (obraMini == null){
            return null;
        }

        obra.setId(obraMini.getId());
        obra.setNombre(obraMini.getNombre());
        obra.setEmpresaContratista(obraMini.getEmpresaContratista());
        obra.setEsPublico(obraMini.isEsPublico());
        obra.setEstado(obraMini.getEstado());
        obra.setMetrosCuadrados(obraMini.getMetrosCuadrados());
        obra.setFechaInicio(obraMini.getFechaInicio());
        obra.setFechaTermino(obraMini.getFechaTermino());

        // Hitos
        HitosObraDocument hitos = hitosObraService.findHitosByObraId(id);
        obra.setHitos(hitos);

        // Pedidos a trabajar
        List<PedidosATrabajarListDTO> pedidos = pedidoService.getPedidosATrabajar(id);
        obra.setPedidosATrabajar(pedidos);

        // Ordenes de compra
        List<OrdenCompraListObraDTO> ordenesCompra = ordenCompraService.getOrdenesCompraByIdObra(id);
        obra.setOrdenesCompra(ordenesCompra);

        // Adminisatrativos.
        List<UsuarioObraListDTO> administrativos = usuarioObraService.getUsuarioListDTOByObraId(id);
        obra.setAdministrativos(administrativos);

        // Subcontratos
        List<SubContratoListDTO> subcontratos = subContratoService.getSubContratosByObraId(id);
        obra.setSubContratados(subcontratos);

        // Asociados
        List<AsociadosListDTO> asociados = asociadoService.getAsociadosListDTOByIdObra(id);
        obra.setAsociados(asociados);

        return obra;
    }
}

