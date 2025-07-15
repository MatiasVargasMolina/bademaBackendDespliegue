package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.EspecificacionMaterialDocument;

@Repository
public interface EspecificacionMaterialRepository extends MongoRepository<EspecificacionMaterialDocument, String> {
    EspecificacionMaterialDocument findByMaterialId(Long idMaterial);
}
