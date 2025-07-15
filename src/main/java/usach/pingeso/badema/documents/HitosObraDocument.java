package usach.pingeso.badema.documents;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "histos_obra_archivos")
public class HitosObraDocument {
    @Id
    private String id;

    //
    private Long idObra;

    // Aqui van todos los hitos dinámicos
    private Map<String, LocalDate> hitos = new HashMap<>();

    @JsonAnyGetter
    public Map<String, LocalDate> getHitos() {
        return hitos;
    }

    @JsonAnySetter
    public void setHitos(String hito, LocalDate fecha) {
        this.hitos.put(hito, fecha);
    }
}
