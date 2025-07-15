package usach.pingeso.badema.services.mongodb;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.OrdenCompraDocument;
import usach.pingeso.badema.repositories.mongodb.OrdenCompraArchivoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.IOException;
import java.util.List;

@Service
public class OrdenCompraArchivoService extends ArchivoServiceBase<OrdenCompraDocument> {
    private final OrdenCompraArchivoRepository ordenCompraRepository;
    private final PathResolverService pathResolverService;

    public OrdenCompraArchivoService(OrdenCompraArchivoRepository ordenCompraRepository, PathResolverService pathResolverService) {
        super(ordenCompraRepository);
        this.ordenCompraRepository = ordenCompraRepository;
        this.pathResolverService = pathResolverService;
    }

    public List<OrdenCompraDocument> getArchivosByIdOrdenCompra (Long idOrdenCompra) {
        return ordenCompraRepository.findByIdOrdenCompra(idOrdenCompra);
    }

    public OrdenCompraDocument guardarArchivo(MultipartFile file, Long idOrdenCompra) throws IOException {
        String uploadDir = pathResolverService.resolvePathForOrdenCompra(idOrdenCompra);
        return super.guardarArchivo(file, uploadDir, (path, name) -> {
            OrdenCompraDocument doc = new OrdenCompraDocument();
            doc.setIdOrdenCompra(idOrdenCompra);
            return doc;
        });
    }
}
