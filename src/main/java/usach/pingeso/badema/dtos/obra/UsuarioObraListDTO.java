package usach.pingeso.badema.dtos.obra;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioObraListDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String rol;
    private LocalDate fechaAsignacion;
}
