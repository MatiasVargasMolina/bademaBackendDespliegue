package usach.pingeso.badema.dtos.auth;

import lombok.Data;

@Data
public class RegisterDTO {
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String telefono;
}
