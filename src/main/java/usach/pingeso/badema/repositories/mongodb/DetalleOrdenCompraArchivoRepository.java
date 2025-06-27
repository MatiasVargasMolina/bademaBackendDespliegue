package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.DetalleOrdenCompraDocument;

import java.util.List;

@Repository
public interface DetalleOrdenCompraArchivoRepository extends MongoRepository<DetalleOrdenCompraDocument, String> {
    List<DetalleOrdenCompraDocument> findByIdDetalleOrdenCompra(Long idDetalleOrdenCompra);
}
