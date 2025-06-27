package usach.pingeso.badema.services.mongodb;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import usach.pingeso.badema.documents.RecepcionMaterialDocument;
import usach.pingeso.badema.repositories.mongodb.RecepcionMaterialArchivoRepository;
import usach.pingeso.badema.services.PathResolverService;

import java.io.IOException;
import java.util.List;

@Service
public class RecepcionMaterialArchivoService extends ArchivoServiceBase<RecepcionMaterialDocument> {
    private final RecepcionMaterialArchivoRepository recepcionMaterialArchivoRepository;
    private final PathResolverService pathResolverService;

    public RecepcionMaterialArchivoService(RecepcionMaterialArchivoRepository recepcionMaterialArchivoRepository, PathResolverService pathResolverService) {
        super(recepcionMaterialArchivoRepository);
        this.recepcionMaterialArchivoRepository = recepcionMaterialArchivoRepository;
        this.pathResolverService = pathResolverService;
    }

    public List<RecepcionMaterialDocument> findByRecepcionId(Long recepcionId) {
        return recepcionMaterialArchivoRepository.findByIdRecepcionMaterial(recepcionId);
    }

    public RecepcionMaterialDocument guardarArchivo(MultipartFile file, Long idRecepcion) throws IOException {
        String uploadDir = pathResolverService.resolvePathForRecepcion(idRecepcion);
        return super.guardarArchivo(file, uploadDir, (path, name) -> {
            RecepcionMaterialDocument doc = new RecepcionMaterialDocument();
            doc.setIdRecepcionMaterial(idRecepcion);
            return doc;
        });
    }
}
