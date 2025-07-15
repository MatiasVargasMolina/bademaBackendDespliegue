package usach.pingeso.badema.documents.malhecho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "guias_despacho")
public class GuiaDespachoDocument {

    @Id
    private String id;

    private String nombreArchivo;
    private String tipoArchivo;
    private byte[] contenido;

    private Long idDetalleOrdenCompra;
    private LocalDate fechaSubida;
}
