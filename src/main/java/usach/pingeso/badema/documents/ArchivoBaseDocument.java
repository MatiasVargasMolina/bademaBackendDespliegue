package usach.pingeso.badema.documents;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
public abstract class ArchivoBaseDocument {
    private String rutaArchivo;
    private LocalDate fechaSubidaArchivo;
    private String nombreArchivo;
}

