package usach.pingeso.badema.documents;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "detalle_orden_compra_archivos")
public class DetalleOrdenCompraDocument extends ArchivoBaseDocument{
    @Id
    private String id;

    private Long idDetalleOrdenCompra;
    private LocalDate fechaExpiracion;
}
