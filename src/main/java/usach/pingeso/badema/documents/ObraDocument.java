package usach.pingeso.badema.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "obra_archivos")
public class ObraDocument extends ArchivoBaseDocument{

    @Id
    private String id;

    private Long idObra;
}
