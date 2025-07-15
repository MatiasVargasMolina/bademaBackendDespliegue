package usach.pingeso.badema.dtos.obra;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ObraListDTO {
    private Long id;
    private String nombre;
    private String empresaContratista;
    private boolean esPublico;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private int metrosCuadrados;
    private String direccion;
    private String estado;
}
