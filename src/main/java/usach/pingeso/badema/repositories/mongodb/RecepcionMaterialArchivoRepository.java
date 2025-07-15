package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.RecepcionMaterialDocument;

import java.util.List;

@Repository
public interface RecepcionMaterialArchivoRepository extends MongoRepository<RecepcionMaterialDocument, String> {
    List<RecepcionMaterialDocument> findByIdRecepcionMaterial(Long idRecepcion);
}
