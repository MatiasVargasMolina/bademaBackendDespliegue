package usach.pingeso.badema.services.mongodb;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.DetalleOrdenCompraDocument;
import usach.pingeso.badema.repositories.mongodb.DetalleOrdenCompraArchivoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.IOException;
import java.util.List;

@Service
public class DetalleOrdenCompraArchivoService extends ArchivoServiceBase<DetalleOrdenCompraDocument> {
    private final DetalleOrdenCompraArchivoRepository detalleOrdenCompraArchivoRepository;
    private final PathResolverService pathResolverService;

    public DetalleOrdenCompraArchivoService(DetalleOrdenCompraArchivoRepository detalleOrdenCompraArchivoRepository, PathResolverService pathResolverService) {
        super(detalleOrdenCompraArchivoRepository);
        this.detalleOrdenCompraArchivoRepository = detalleOrdenCompraArchivoRepository;
        this.pathResolverService = pathResolverService;
    }

    public List<DetalleOrdenCompraDocument> getArchivosByIdDetalleOrdenCompra(Long idDetalleOrdenCompra){
        return detalleOrdenCompraArchivoRepository.findByIdDetalleOrdenCompra(idDetalleOrdenCompra);
    }

    public DetalleOrdenCompraDocument guardarArchivo(MultipartFile file, Long idDetalleOrdenCompra) throws IOException {
        String uploadDir = pathResolverService.resolvePathForDetalle(idDetalleOrdenCompra);
        return super.guardarArchivo(file, uploadDir, (path, name) -> {
            DetalleOrdenCompraDocument doc = new DetalleOrdenCompraDocument();
            doc.setIdDetalleOrdenCompra(idDetalleOrdenCompra);
            return doc;
        });
    }
}
