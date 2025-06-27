package usach.pingeso.badema.documents;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orden_compra_archivos")
public class OrdenCompraDocument extends ArchivoBaseDocument{
     @Id
     private String id;

    private Long idOrdenCompra;
}
