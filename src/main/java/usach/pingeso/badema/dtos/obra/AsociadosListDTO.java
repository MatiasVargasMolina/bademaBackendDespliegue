package usach.pingeso.badema.dtos.obra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AsociadosListDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String rut;
    private String email;
    private String telefono;
    private int rol;
}
