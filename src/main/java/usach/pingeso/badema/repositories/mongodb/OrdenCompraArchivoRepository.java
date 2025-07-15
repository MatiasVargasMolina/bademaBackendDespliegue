package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.OrdenCompraDocument;

import java.util.List;

@Repository
public interface OrdenCompraArchivoRepository extends MongoRepository<OrdenCompraDocument, String> {
    List<OrdenCompraDocument> findByIdOrdenCompra(Long idOrdenCompra);
}
