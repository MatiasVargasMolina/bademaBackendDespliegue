package usach.pingeso.badema.dtos.obra;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ObraCreateDTO {
    private Long idUsuario;
    private String nombre;
    private String empresaContratista;
    private boolean esPublico;
    private int metrosCuadrados;
    private String direccion;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private HitoDTO hitos;
}
