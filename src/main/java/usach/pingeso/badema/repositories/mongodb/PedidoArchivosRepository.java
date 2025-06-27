package usach.pingeso.badema.repositories.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usach.pingeso.badema.documents.PedidoDocument;

import java.util.List;

@Repository
public interface PedidoArchivosRepository extends MongoRepository<PedidoDocument, String> {
    List<PedidoDocument> findByIdPedido(Long idPedido);
}
