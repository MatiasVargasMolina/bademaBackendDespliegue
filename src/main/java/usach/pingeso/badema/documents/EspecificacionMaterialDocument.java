package usach.pingeso.badema.documents;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "especificacion_material_archivos")
public class EspecificacionMaterialDocument {
    @Id
    private String id;

    //
    private Long materialId;

    // Aqui van todas las especificaciones dinamicas
    private Map<String, String> especificaciones = new HashMap<>();

    @JsonAnyGetter
    public Map<String, String> getEspecificaciones() {
        return especificaciones;
    }

    @JsonAnySetter
    public void setEspecificaciones(String key, String value) {
        this.especificaciones.put(key, value);
    }
}
