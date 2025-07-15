package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.HitosObraDocument;

@Repository
public interface HitosObraRepository extends MongoRepository<HitosObraDocument, String> {
    HitosObraDocument findByIdObra(Long idObra);
}
