package usach.pingeso.badema.dtos.auth;

import lombok.Data;

@Data
public class LoginDTO {
    private String contrasena;
    private String correo;
}
