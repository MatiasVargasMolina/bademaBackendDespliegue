package usach.pingeso.badema.documents;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
@Document(collection = "nuevas_especificaciones_archivos")
public class NuevasEspecificacionesDocument {
    private String id;

    // Linkeado a un proveedorMaterial con las especificasciones directas
    private Long idProveedorMaterial;

    private String idEspecificacionInicial;

    // Aqui van la copia de las especificaciones con su marca si son iguals o nuevas, etc.
    private Map<String, ValorEspecificacion> especificacionesNuevas = new HashMap<>();

    @JsonAnyGetter
    public Map<String, ValorEspecificacion> getEspecificacionesNuevas() {
        return especificacionesNuevas;
    }

    @JsonAnySetter
    public void setEspecificacionesNuevas(String key, ValorEspecificacion value) {
        this.especificacionesNuevas.put(key, value);
    }
}
