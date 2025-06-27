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
@Document(collection = "recepcion_material_archivos")
public class RecepcionMaterialDocument extends ArchivoBaseDocument{
    @Id
    private String id;

    private Long idRecepcionMaterial;
}
