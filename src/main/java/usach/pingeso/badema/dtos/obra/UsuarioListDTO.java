package usach.pingeso.badema.dtos.obra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UsuarioListDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
}
