package usach.pingeso.badema.repositories.malimplementado;

import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.malhecho.GuiaDespachoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

@Repository
public interface GuiaDespachoRepository extends MongoRepository<GuiaDespachoDocument, String> {
    List<GuiaDespachoDocument> findByIdDetalleOrdenCompra(Long idDetalleOrdenCompra);
}
