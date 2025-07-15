package usach.pingeso.badema.services.mongodb;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.PedidoDocument;
import usach.pingeso.badema.repositories.mongodb.PedidoArchivosRepository;
import usach.pingeso.badema.repositories.postgresql.PedidoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.IOException;
import java.util.List;

@Service
public class PedidoArchivoService extends ArchivoServiceBase<PedidoDocument> {

    private final PedidoArchivosRepository pedidoArchivosRepository;
    private final PedidoRepository pedidoRepository;
    private final PathResolverService pathResolverService;

    public PedidoArchivoService(
            PedidoArchivosRepository pedidoArchivosRepository,
            PedidoRepository pedidoRepository,
            PathResolverService pathResolverService) {
        super(pedidoArchivosRepository);
        this.pedidoArchivosRepository = pedidoArchivosRepository;
        this.pedidoRepository = pedidoRepository;
        this.pathResolverService = pathResolverService;
    }

    public List<PedidoDocument> getArchivosByIdPedido(Long idPedido) {
        return pedidoArchivosRepository.findByIdPedido(idPedido);
    }

    public PedidoDocument guardarArchivo(MultipartFile file, Long idPedido) throws IOException {
        pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Usa el nuevo servicio para construir el path
        String uploadDir = pathResolverService.resolvePathForPedido(idPedido);

        // Guarda el archivo con el path generado
        return super.guardarArchivo(file, uploadDir, (path, name) -> {
            PedidoDocument doc = new PedidoDocument();
            doc.setIdPedido(idPedido);
            return doc;
        });
    }
}
