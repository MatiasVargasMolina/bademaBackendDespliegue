package usach.pingeso.badema.dtos.obra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubContratoDTO {
    private String nombre;
    private String apellidos;
    private String rut;
    private String email;
    private String telefono;
    private String areaTrabajo;
    private Long idObra;
}
