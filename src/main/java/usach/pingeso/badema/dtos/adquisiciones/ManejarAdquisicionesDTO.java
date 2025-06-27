package usach.pingeso.badema.dtos.adquisiciones;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ManejarAdquisicionesDTO {
    private Long idProveedor;
    private String nombreProveedor;
    private String condiciones;
    private int precio;
    private String restricciones;
    private String comentarios;
}