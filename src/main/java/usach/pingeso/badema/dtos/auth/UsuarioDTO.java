package usach.pingeso.badema.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private String correo;
    private String contrasena;
    private String rol;
}
