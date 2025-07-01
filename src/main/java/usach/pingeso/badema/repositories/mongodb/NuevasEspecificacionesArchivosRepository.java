package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import usach.pingeso.badema.documents.NuevasEspecificacionesDocument;

public interface NuevasEspecificacionesArchivosRepository extends MongoRepository<NuevasEspecificacionesDocument, String> {
    NuevasEspecificacionesDocument findByIdProveedorMaterial(Long idProveedorMaterial);
}
