package usach.pingeso.badema.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pedido_archivos")
public class PedidoDocument extends ArchivoBaseDocument{
    @Id
    private String id;

    private Long idPedido;
}
