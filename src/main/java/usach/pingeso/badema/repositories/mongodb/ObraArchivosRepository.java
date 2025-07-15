package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.ObraDocument;

import java.util.List;

@Repository
public interface ObraArchivosRepository extends MongoRepository<ObraDocument, String> {
    List<ObraDocument> findByIdObra(Long idObra);
}
